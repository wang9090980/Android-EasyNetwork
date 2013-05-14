package me.xiaopan.networkeasy;

import java.lang.reflect.Type;

import me.xiaopan.networkeasy.util.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.google.gson.GsonBuilder;

/**
 * 默认的JSON响应处理器
 * @author xiaopan
 */
public abstract class JsonHttpResponseHandler<T> extends Handler implements HttpResponseHandler {
	private static final int MESSAGE_START = 0;
	private static final int MESSAGE_SUCCESS = 1;
	private static final int MESSAGE_FAILURE = 2;
	private static final int MESSAGE_EXCEPTION = 3;
	private static final int MESSAGE_END = 4;
	private Class<?> responseClass;
	private Type responseType;
	
	public JsonHttpResponseHandler(Class<?> responseClass){
		this.responseClass = responseClass;
	}
	
	public JsonHttpResponseHandler(Type responseType){
		this.responseType = responseType;
	}
	
	@Override
	public void sendStartMessage() {
		sendEmptyMessage(MESSAGE_START);
	}

	@Override
	public void sendHandleResponseMessage(HttpResponse httpResponse) throws Throwable {
		if(httpResponse.getStatusLine().getStatusCode() < 300 ){
			Object result = null;
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
				/* 读取返回的JSON字符串并转换成对象 */
				String jsonString = EntityUtils.toString(new BufferedHttpEntity(httpEntity), Utils.getResponseCharset(httpResponse));
				if(jsonString != null && !"".equals(jsonString)){
					if(responseClass != null){	//如果是要转换成一个对象
						ResponseBodyKey responseBodyKey = responseClass.getAnnotation(ResponseBodyKey.class);
						if(responseBodyKey != null && responseBodyKey.value() != null && !"".equals(responseBodyKey.value())){
							result = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(new JSONObject(jsonString).getString(responseBodyKey.value()), responseClass);
						}else{
							result = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonString, responseClass);
						}
					}else if(responseType != null){	//如果是要转换成一个集合
						result = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonString, responseType);
					}
				}
			}
			sendMessage(obtainMessage(MESSAGE_SUCCESS, result));
		}else{
			sendMessage(obtainMessage(MESSAGE_FAILURE, httpResponse));
		}
	}
	
	@Override
	public void sendExceptionMessage(Throwable e) {
		sendMessage(obtainMessage(MESSAGE_EXCEPTION, e));
	}

	@Override
	public void sendEndMessage() {
		sendEmptyMessage(MESSAGE_END);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what) {
			case MESSAGE_START: onStart(); break;
			case MESSAGE_SUCCESS: onSuccess((T) msg.obj); break;
			case MESSAGE_FAILURE: onFailure((HttpResponse) msg.obj); break;
			case MESSAGE_EXCEPTION: onException((Throwable) msg.obj); break;
			case MESSAGE_END: onEnd(); break;
		}
	}
	
	public abstract void onStart();
	public abstract void onSuccess(T responseObject);
	public abstract void onFailure(HttpResponse httpResponse);
	public abstract void onException(Throwable e);
	public abstract void onEnd();
}
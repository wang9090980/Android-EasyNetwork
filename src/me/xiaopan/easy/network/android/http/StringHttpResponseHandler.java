/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.xiaopan.easy.network.android.http;


import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * 默认的字符串Http响应处理器
 */
public abstract class StringHttpResponseHandler extends HttpResponseHandler {

	@Override
	public void start(final Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onStart();
            }
        });
	}

	@Override
	public void handleResponse(final Handler handler, final HttpResponse httpResponse, final boolean isCache, final boolean isRefreshCacheAndCallback) throws Throwable {
		if(httpResponse.getStatusLine().getStatusCode() > 100 && httpResponse.getStatusLine().getStatusCode() < 300 ){
			/* 读取内容并转换成字符串 */
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
                final String responseContent = EntityUtils.toString(new BufferedHttpEntity(httpEntity), HttpUtils.getResponseCharset(httpResponse));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(responseContent, isCache, isRefreshCacheAndCallback);
                    }
                });
			}else{
                throw new Exception("没有响应体");
			}
		}else{
            throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), "异常状态码："+httpResponse.getStatusLine().getStatusCode());
		}
	}

	@Override
	public void exception(final Handler handler, final Throwable e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(e);
            }
        });
	}

	public abstract void onStart();
	public abstract void onSuccess(String responseContent, boolean isCache, boolean isRefreshCacheAndCallback);
	public abstract void onFailure(Throwable throwable);
}

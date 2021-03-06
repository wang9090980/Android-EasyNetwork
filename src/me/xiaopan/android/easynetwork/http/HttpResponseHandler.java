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
package me.xiaopan.android.easynetwork.http;

import org.apache.http.HttpResponse;

import android.os.Handler;

/**
 * 响应处理器
 */
public abstract class HttpResponseHandler{
    /**
     * 当请求开始
     * @param handler 消息处理器
     */
    public abstract void start(final Handler handler);
    
    /**
     * 判断是否可以缓存，当需要缓存HttpResponse的时候会先调用此方法判断是否可以缓存，例如：实现者可以在此方法里过滤掉状态码不是200的响应。默认为状态码大于等于200并且 小于300就返回true
     * @param handler 消息处理器
     * @param httpResponse Http响应
     * @return 是否可以缓存
     */
    public boolean isCanCache(final Handler handler, final HttpResponse httpResponse){
    	return httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300;
    }

    /**
     * 处理响应，值得注意的是此方法当在开启缓存模式并且开启刷新缓存以及开启了刷新后再次回调的时候总共会回调两次，你可以通过isCache、isRefreshCacheAndCallback参数来区分并做不同的处理
     * @param handler 消息处理器
     * @param httpResponse Http响应
     * @param isCache 是否是缓存数据
     * @param isRefreshCacheAndCallback 是否还要刷新本地缓存并回调，当此参数为true时，还会再调用一次handleResponse()方法
     * @throws Throwable 当发生异常时会进入exception()方法
     */
    public abstract void handleResponse(final Handler handler, final HttpResponse httpResponse, final boolean isCache, final boolean isRefreshCacheAndCallback) throws Throwable;

    /**
     * 在请求的过程中发生异常，值得注意的是在读取本地缓存发生异常的话不会回调此方法，因为会自动改为从网络获取数据，所以一旦回调此方法就意味着整个请求已经结束了
     * @param handler 消息处理器
     * @param e 异常
     */
    public abstract void exception(final Handler handler, final Throwable e);
}

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

package me.xiaopan.easy.network.android.examples.net.request;

import me.xiaopan.easy.network.android.examples.net.BaseRequest;
import me.xiaopan.easy.network.android.http.annotation.Host;
import me.xiaopan.easy.network.android.http.enums.MethodType;

/**
 * 天气请求
 */
@Host("http://m.weather.com.cn")
@me.xiaopan.easy.network.android.http.annotation.Method(MethodType.POST)
public class WeatherRequest extends BaseRequest{
	
}
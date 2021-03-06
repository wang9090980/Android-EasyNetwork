# ![Logo](https://github.com/xiaopansky/Android-EasyNetwork/raw/master/res/drawable-mdpi/ic_launcher.png) Android-EasyNetwork

这是一个参考了android-async-http项目的Android网络访问库，旨在用最简单、最快捷的方式来访问网络！

##Features
>* 重新封装了HttpRequest，使用更方便；
>* 支持以请求对象的方式来发送Http请求；
>* 支持缓存Http Response，缓存信息还可以配置过期时间；
>* 默认提供单例模式；

##Downloads
**[android-easy-network-2.1.2.jar](https://github.com/xiaopansky/Android-EasyNetwork/raw/master/releases/android-easy-network-2.1.2.jar)**

**[android-easy-network-2.1.2-with-src.jar](https://github.com/xiaopansky/Android-EasyNetwork/raw/master/releases/android-easy-network-2.1.2-with-src.jar)**

##Change Log
###2.1.2
>* 修复ResponseCache注解没有加运行时标记的BUG

###2.1.1
>* 注解的序列化名称注解由SerializedName替换为Param

###2.1.0

##Depend
>* **[gson-2.2.2.jar](https://github.com/xiaopansky/Android-EasyNetwork/raw/master/libs/gson-2.2.2.jar)** 可选的。如果你要使用JsonHttpResponseHandler和缓存功能的话就必须引入此类库 

##Usage Guide

###发送请求

####使用普通方式发送请求
>* 调用``EasyHttpClient.execute(Context, String, HttpUriRequest, ResponseCache, HttpResponseHandler)``方法；
>* 使用重新封装的HttpRequest（包括``HttpGetRequest``、``HttpPostRequest``、``HttpPutRequest``、``HttpDeleteRequest``）调用``EasyHttpClient.get(Context, HttpGetRequest, HttpResponseHandler)``、``EasyHttpClient.post(Context, HttpPostRequest, HttpResponseHandler)``、``EasyHttpClient.put(Context, HttpPutRequest, HttpResponseHandler)``、``EasyHttpClient.delete(Context, HttpDeleteRequest, HttpResponseHandler)``等方法。

####使用请求对象发送请求
当你调用EasyHttpClient的``execute(Context, Request, HttpResponseHandler)``方法去执行一个请求的时候，会要求你传一个实现了``Request``接口的对象，此对象被称作请求对象，``EasyHttpClient``将通过此请求对象解析出请求方式、请求地址、请求头、请求参数等信息。具体配置如下：
>* 请求名称的配置：
    1. 在请求对象上加上``@Name``注解即可，例如：``@Name("百度搜索")``。请求名称将用于在log中区分不同的请求。

>* 请求方式的配置：
    1. 在请求对象上加上``@Method``注解即可，例如：``@Method(MethodType.POST)``；
    2. 目前支持GET、POST、PUT、DELETE四种请求，缺省值是``MethodType.GET``。

>* 请求地址的配置：
    1. 使用``@Url``注解来指定完整的请求地址。例如：``@Url("http://m.weather.com.cn/data/101010100.html")``；
    2. 你还可以选择使用``@Host``加``@Path``注解来指定完整的请求地址，其中``@Host``负责不可变部分（例如``http://m.weather.com.cn``）；``@Path``负责变化部分（例如：``data/101010100.html``）；值得注意的是请不要在``@Host``的末尾或者``@Path``的开头加``/``，因为在解析的时候会自动加上；
    3. 以上``@Url``、``@Host``以及``@Path``注解都是可以继承的，因此你可以弄一个BaseRequest然后把请求地址的不可变部分用``@Host``注解加在BaseRequest上，然后其他的请求都继承BaseRequest，这样一来其它的请求就只需添加``@Path``注解即可，同时也可以保证主机地址只会在一个地方定义；
    4. ``@Url``的优先级高于``@Host``加``@Path``。

>* 请求头的配置：
    1. 你可以在请求对象中定义一个字段，字段类型可以是Header、Header[]或者Collection<Header>，然后在此字段上加上``@Header`注解即可；
    
>* 请求参数的配置：
    1. 将请求对象中需要转换成请求参数的字段加上``@Param``注解即可；
    2. 默认请求参数名称是字段的名称，如果你想自定义名称就给``@Param``注解附上值，例如：``@Param("wd")``
    3. 默认使用字段的toString()方法来获取请求参数值，但以下几种类型的字段将会被特殊处理：
        * Map
            对于``Map``类型的字段``EasyHttpClient``会将其每一对键值对都转换成请求参数，而每一对键值对的键将作为参数名，键值对的值将作为参数值；
        * File
            对于``File``类型的字段EasyHttpClient将使用``RequestParams``的``put(String key, File file)``方法将其添加到``RequestParams``中；
        * ArrayList 
            对于``ArrayList``类型的字段EasyHttpClient将使用``RequestParams``的``put(String key, ArrayList<String> values)``方法将其添加到``RequestParams``中；
        * Boolean 
            对于``Boolean``类型的字段你可以通过``@True``和``@False``注解来指定当字段值是``true``或``false``的时候其对应的转换成请求参数时的参数值；
        * Enum
            对于``Enum``类型的参数你可以使用``@Param``注解来指定其参数值，如果没有```@Param``注解将使用Enum对象的name来作为参数值。

>* HttpResponse缓存的配置：
    1. 使用``@ResponseCache``注解来配置响应缓存，``@ResponseCache``有四个参数
    2. periodOfValidity：int型，指定缓存有效期，单位毫秒，小于等于0表示永久有效，默认值为0；
    3. isRefreshCache：boolean型，指定当本地缓存可用的时候，是否依然从网络加载新的数据来刷新本地缓存，默认值为false；
    4. isRefreshCallback：boolean型，指定当刷新本地缓存完成的时候是否再次回调HttpResponseHandler.handleResponse()，默认值为false；
    5. cacheDirectory：String型，指定缓存目录，默认值为``""``。

更加详细的配置方式请参考示例程序。

####缓存Http Response
>* 在使用普通方式发送请求的时候你可以传一个ResponseCache类型的参数来定义缓存配置;
>* 使用请求对象的时候你可以在请求对象上加上@ResponseCache注解来定义缓存配置。

###处理响应
不管你用何种方式发送请求，都会要求传一个HttpResponseHandler，因此你需要继承HttpResponseHandler抽象类来处理Http响应，HttpResponseHandler的三个抽象方法说明如下：
>* ``start(Handler)``：开始发送请求的时候会回调此方法；
>* ``handleResponse(Handler, HttpResponse, boolean, boolean)``：当请求发送成功需要处理响应的时候会回调此方法；
>* ``exception(Handler, Throwable )``：当在整个过程发生异常的话会回调此方法。

另外
>* HttpResponseHandler还有一个方法isCanCache(Handler, HttpResponse)用来判定是否可以缓存，默认是当状态码大于等于200小于300就允许缓存，你可以重写此方法来改变缓存判定策略。
>* HttpResponseHandler的每一个方法都有Handler，因此你可以借助Handler在主线程更新视图，具体使用可以参考JsonHttpResponseHandler；
>* 已经提供了BinaryHttpResponseHandler、JsonHttpResponseHandler、StringHttpResponseHandler三种实现，可以满足大部分需求了。

###示例：

####请求方式配置示例：
```java
/**
 * 基本请求，可以将一些每个请求都必须有的参数定义在此
 */
@Method(MethodType.POST)
public class BaseRequest implements Request {

}
```
你可以定义一个BaseRequest，其它所有请求都继承于BaseRequest，然后在BaseRequest上定义所有请求的请求方式

####请求地址配置示例：

#####@Url使用示例：
```java
/**
 * 北京天气请求
 */
@Url("http://m.weather.com.cn/data/101010100.html")
public class BeijingWeatherRequest implements Request {

}
```

#####@Host加@Path使用示例：
```java
/**
 * 天气请求基类
 */
@Host("http://m.weather.com.cn")
public class WeatherRequest implements Request{

}
```
```java
/**
 * 北京天气请求
 */
@Path("data/101010100.html")
public class BeijingWeatherRequest extends WeatherRequest {

}
```
```java
/**
 * 上海天气请求
 */
@Path("data/101020100.html")
public class ShanghaiWeatherRequest extends WeatherRequest {

}
```

####请求头配置示例：
```java
/**
 * 基本请求
 */
@Method(MethodType.POST)
public class BaseRequest implements Request {
    @Header
    private org.apache.http.Header header = new BasicHeader("Connection", "Keep-Alive");

    @Header
    private org.apache.http.Header[] headers = new org.apache.http.Header[]{new BasicHeader("Connection", "Keep-Alive"), new BasicHeader("Content-Length", "22")};

    @Header
    private List<org.apache.http.Header> headerList;

    public BaseRequest() {
        headerList = new ArrayList<org.apache.http.Header>(2);
        headerList.add(new BasicHeader("Connection", "Keep-Alive"));
        headerList.add(new BasicHeader("Content-Length", "22"));
    }
}
```
示例中的三种方式任意一种都可以。

####请求参数配置示例：
```java
/**
 * 百度搜索请求
 */
@Url("http://www.baidu.com/s")
@Name("百度搜索")
public class BaiduSearchRequest implements Request {
    @Param
    public String rsv_spt = "1";

    @Param
    public String issp = "1";

    @Param
    public String rsv_bp = "0";

    @Param
	public String ie = "utf-8";

	@Param
	public String tn = "98012088_3_dg";

	@Param
	public String rsv_sug3 = "4";

	@Param
	public String rsv_sug = "0";

	@Param
	public String rsv_sug1 = "3";

	@Param
	public String rsv_sug4 = "481";

	@Param("wd")
	public String keyword;

	/**
	 * 创建一个百度搜索请求
	 * @param keyword 搜索关键字
	 */
	public BaiduSearchRequest(String keyword){
		this.keyword = keyword;
	}
}
```

####HttpResponse缓存配置示例：
```java
@Url("http://www.qiushibaike.com/article/52638010")
@ResponseCache(periodOfValidity = 1000 * 60 * 60 * 24, isRefreshCache = true)
public class QiuBaiRequest extends BaseRequest{
    @Param
    private String list = "8hr";

    @Param
    private String s = "4618412";
}
```

####一个完整的发送请求并处理响应的示例：
```java
EasyHttpClient.getInstance().execute(getBaseContext(), new BeijingWeatherRequest(), new JsonHttpResponseHandler<Weather>(Weather.class) {
    @Override
    public void onStart() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Weather responseObject, boolean isCache, boolean isRefreshCacheAndCallback) {
        text.setText(Html.fromHtml("<h2>" + responseObject.getCity() + "</h2>"
                + "<br>" + responseObject.getDate_y() + " " + responseObject.getWeek()
                + "<br>" + responseObject.getTemp1() + " " + responseObject.getWeather1()
                + "<p><br>风力：" + responseObject.getWind1()
                + "<br>紫外线：" + responseObject.getIndex_uv()
                + "<br>紫外线（48小时）：" + responseObject.getIndex48_uv()
                + "<br>穿衣指数：" + responseObject.getIndex() + "，" + responseObject.getIndex_d()
                + "<br>穿衣指数（48小时）：" + responseObject.getIndex48() + "，" + responseObject.getIndex48_d()
                + "<br>舒适指数：" + responseObject.getIndex_co()
                + "<br>洗车指数：" + responseObject.getIndex_xc()
                + "<br>旅游指数：" + responseObject.getIndex_tr()
                + "<br>晨练指数：" + responseObject.getIndex_cl()
                + "<br>晾晒指数：" + responseObject.getIndex_ls()
                + "<br>过敏指数：" + responseObject.getIndex_ag() + "</p>"
        ));
        findViewById(R.id.loading).setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Throwable throwable) {
        text.setText(throwable.getMessage());
        findViewById(R.id.loading).setVisibility(View.GONE);
    }
});
```

###单例模式
你只需调用``EasyHttpClient.getInstance()``即可获取EasyHttpClient的实例。

##License
```java
/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

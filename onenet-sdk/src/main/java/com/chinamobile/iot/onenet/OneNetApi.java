package com.chinamobile.iot.onenet;

import android.app.Application;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.chinamobile.iot.onenet.http.Config;
import com.chinamobile.iot.onenet.http.HttpExecutor;
import com.chinamobile.iot.onenet.http.RetryInterceptor;
import com.chinamobile.iot.onenet.http.Urls;
import com.chinamobile.iot.onenet.module.ApiKey;
import com.chinamobile.iot.onenet.module.Binary;
import com.chinamobile.iot.onenet.module.Command;
import com.chinamobile.iot.onenet.module.DataPoint;
import com.chinamobile.iot.onenet.module.DataStream;
import com.chinamobile.iot.onenet.module.Device;
import com.chinamobile.iot.onenet.module.Mqtt;
import com.chinamobile.iot.onenet.module.Trigger;
import com.chinamobile.iot.onenet.util.Assertions;
import com.chinamobile.iot.onenet.util.Meta;
import com.chinamobile.iot.onenet.util.OneNetLogger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetApi {

    public static final String LOG_TAG = "OneNetApi";

    private static String sAppKey;

    static boolean sDebug;

    private static HttpExecutor sHttpExecutor;

    /**
     * 初始化SDK
     *
     * @param application Application实例
     * @param debug       是否开启调试模式（打印 HTTP 请求和响应日志）
     */
    public static void init(Application application, boolean debug) {
        init(application, debug, null);
    }

    /**
     * 初始化SDK
     *
     * @param application Application实例
     * @param debug       是否开启调试模式（打印 HTTP 请求和响应日志）
     * @param config      HTTP 相关配置（超时时间，重试次数等）
     */
    public static void init(Application application, boolean debug, Config config) {
        try {
            sAppKey = Meta.readAppKey(application);
            String scheme = Meta.readScheme(application);
            if (!TextUtils.isEmpty(scheme) && !scheme.contains(" ")) {
                Urls.sScheme = scheme;
            }
            String host = Meta.readHost(application);
            if (!TextUtils.isEmpty(host) && !host.contains(" ")) {
                Urls.sHost = host;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sDebug = debug;

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (sDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OneNetLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor);
        }
        okHttpClientBuilder.addInterceptor(sApiKeyInterceptor);

        if (config != null) {
            if (config.connectTimeout() > 0) {
                okHttpClientBuilder.connectTimeout(config.connectTimeout(), TimeUnit.MILLISECONDS);
            }
            if (config.readTimeout() > 0) {
                okHttpClientBuilder.readTimeout(config.readTimeout(), TimeUnit.MILLISECONDS);
            }
            if (config.writeTimeout() > 0) {
                okHttpClientBuilder.writeTimeout(config.writeTimeout(), TimeUnit.MILLISECONDS);
            }
            if (config.retryCount() > 0) {
                okHttpClientBuilder.addInterceptor(new RetryInterceptor(config.retryCount()));
            }
        }

        OkHttpClient client = okHttpClientBuilder.build();
        sHttpExecutor = new HttpExecutor(client);
    }

    private static Interceptor sApiKeyInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("api-key", sAppKey);
            if (TextUtils.isEmpty(sAppKey)) {
                Log.e(LOG_TAG, "APP-KEY is messing, please config in the meta-data or call setAppKey()");
            }
            return chain.proceed(builder.build());
        }
    };

    /**
     * 设置产品ApiKey
     *
     * @param apiKey
     */
    public static void setAppKey(String apiKey) {
        sAppKey = apiKey;
    }

    /**
     * 获取产品ApiKey
     *
     * @return
     */
    public static String getAppKey() {
        return sAppKey;
    }

    private static boolean isInitialized() {
        return sHttpExecutor != null;
    }

    private static boolean isOnUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private static boolean isSchemeConfigured() {
        return !TextUtils.isEmpty(Urls.sScheme) && !Urls.sScheme.contains(" ");
    }

    private static boolean isUrlConfigured() {
        return !TextUtils.isEmpty(Urls.sHost) && !Urls.sHost.contains(" ");
    }

    private static void assertInitialized() {
        Assertions.assertCondition(isInitialized(), "You should call OneNetApi.init() in your Application!");
    }

    private static void assertUIThread() {
        Assertions.assertCondition(isOnUIThread(), "Expected to run on UI thread!");
    }

    private static void assertSchemeConfigured() {
        Assertions.assertCondition(isSchemeConfigured(), "Api scheme must be configured in AndroidManifest.xml!");
    }

    private static void assertUrlConfigured() {
        Assertions.assertCondition(isUrlConfigured(), "HOST must be configured in AndroidManifest.xml!");
    }

    /**
     * HTTP GET method
     *
     * @param url
     * @param callback
     */
    public static void get(String url, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        sHttpExecutor.get(url, new OneNetApiCallbackAdapter(callback));
    }

    /**
     * HTTP POST method
     *
     * @param url
     * @param requestBodyString
     * @param callback
     */
    public static void post(String url, String requestBodyString, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBodyString);
        sHttpExecutor.post(url, requestBody, new OneNetApiCallbackAdapter(callback));
    }

    /**
     * HTTP POST method
     *
     * @param url
     * @param file
     * @param callback
     */
    public static void post(String url, File file, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        sHttpExecutor.post(url, requestBody, new OneNetApiCallbackAdapter(callback));
    }

    /**
     * HTTP POST method
     *
     * @param url
     * @param content
     * @param callback
     */
    public static void post(String url, byte[] content, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), content);
        sHttpExecutor.post(url, requestBody, new OneNetApiCallbackAdapter(callback));
    }

    /**
     * HTTP PUT method
     *
     * @param url
     * @param requestBodyString
     * @param callback
     */
    public static void put(String url, String requestBodyString, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBodyString);
        sHttpExecutor.put(url, requestBody, new OneNetApiCallbackAdapter(callback));
    }

    /**
     * HTTP DELETE method
     *
     * @param url
     * @param callback
     */
    public static void delete(String url, OneNetApiCallback callback) {
        assertInitialized();
        assertSchemeConfigured();
        assertUrlConfigured();
        sHttpExecutor.delete(url, new OneNetApiCallbackAdapter(callback));
    }

    /******************** 设备相关api ********************/

    /**
     * 注册设备（该方法用于让设备终端自己在平台创建设备，不适用于APP）
     *
     * @param registerCode      设备注册码
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art262.html#68">
     *                          http://www.heclouds.com/doc/art262.html#68</a>
     * @param callback          回调接口对象
     */
    public static void registerDevice(String registerCode, String requestBodyString, OneNetApiCallback callback) {
        post(Device.urlForRegistering(registerCode), requestBodyString, callback);
    }

    /**
     * 新增设备
     *
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art262.html#68">
     *                          http://www.heclouds.com/doc/art262.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addDevice(String requestBodyString, OneNetApiCallback callback) {
        post(Device.urlForAdding(), requestBodyString, callback);
    }

    /**
     * 更新设备
     *
     * @param deviceId          设备ID
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art262.html#68">
     *                          http://www.heclouds.com/doc/art262.html#68</a>
     * @param callback          回调接口对象
     */
    public static void updateDevice(String deviceId, String requestBodyString, OneNetApiCallback callback) {
        put(Device.urlForUpdating(deviceId), requestBodyString, callback);
    }

    /**
     * 精确查询单个设备
     *
     * @param deviceId 设备ID
     * @param callback 回调接口对象
     */
    public static void querySingleDevice(String deviceId, OneNetApiCallback callback) {
        get(Device.urlForQueryingSingle(deviceId), callback);
    }

    /**
     * 模糊查询设备
     *
     * @param params   URL参数 详见<a href="http://www.heclouds.com/doc/art262.html#68">
     *                 http://www.heclouds.com/doc/art262.html#68</a>
     * @param callback 回调接口对象
     */
    public static void fuzzyQueryDevices(Map<String, String> params, OneNetApiCallback callback) {
        get(Device.urlForfuzzyQuerying(params), callback);
    }

    /**
     * 删除设备
     *
     * @param deviceId 设备ID
     * @param callback 回调接口对象
     */
    public static void deleteDevice(String deviceId, OneNetApiCallback callback) {
        delete(Device.urlForDeleting(deviceId), callback);
    }

    /******************** END ********************/

    /******************** 数据流相关api ********************/

    /**
     * 新增数据流
     *
     * @param deviceId          设备ID
     * @param requestBodyString HTTP内容 详见 <a href="http://www.heclouds.com/doc/art261.html#68">
     *                          http://www.heclouds.com/doc/art261.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addDataStream(String deviceId, String requestBodyString, OneNetApiCallback callback) {
        post(DataStream.urlForAdding(deviceId), requestBodyString, callback);
    }

    /**
     * 更新数据流
     *
     * @param deviceId          设备ID
     * @param dataStreamId      数据流ID
     * @param requestBodyString HTTP内容 详见 <a href="http://www.heclouds.com/doc/art261.html#68">
     *                          http://www.heclouds.com/doc/art261.html#68</a>
     * @param callback          回调接口对象
     */
    public static void updateDataStream(String deviceId, String dataStreamId, String requestBodyString, OneNetApiCallback callback) {
        put(DataStream.urlForUpdating(deviceId, dataStreamId), requestBodyString, callback);
    }

    /**
     * 查询单个数据流
     *
     * @param deviceId     设备ID
     * @param dataStreamId 数据流ID
     * @param callback     回调接口对象
     */
    public static void querySingleDataStream(String deviceId, String dataStreamId, OneNetApiCallback callback) {
        get(DataStream.urlForQueryingSingle(deviceId, dataStreamId), callback);
    }

    /**
     * 查询多个数据流
     *
     * @param deviceId 设备ID
     * @param callback 回调接口对象
     */
    public static void queryMultiDataStreams(String deviceId, OneNetApiCallback callback) {
        get(DataStream.urlForQueryingMulti(deviceId, null), callback);
    }

    /**
     * 查询多个数据流
     *
     * @param deviceId      设备ID
     * @param dataStreamIds 数据流ID数组
     * @param callback      回调接口对象
     */
    public static void queryMultiDataStreams(String deviceId, String[] dataStreamIds, OneNetApiCallback callback) {
        get(DataStream.urlForQueryingMulti(deviceId, dataStreamIds), callback);
    }

    /**
     * 删除数据流
     *
     * @param deviceId     设备ID
     * @param dataStreamId 数据流ID
     * @param callback     回调接口对象
     */
    public static void deleteDatastream(String deviceId, String dataStreamId, OneNetApiCallback callback) {
        delete(DataStream.urlForDeleting(deviceId, dataStreamId), callback);
    }

    /******************** END ********************/

    /******************** 数据点相关api ********************/

    /**
     * 新增数据点(数据点类型为3)
     *
     * @param deviceId          设备ID
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art260.html#68">
     *                          http://www.heclouds.com/doc/art260.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addDataPoints(String deviceId, String requestBodyString, OneNetApiCallback callback) {
        post(DataPoint.urlForAdding(deviceId, null), requestBodyString, callback);
    }

    /**
     * 新增数据点
     *
     * @param deviceId          设备ID
     * @param type              数据点类型
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art260.html#68">
     *                          http://www.heclouds.com/doc/art260.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addDataPoints(String deviceId, String type, String requestBodyString, OneNetApiCallback callback) {
        post(DataPoint.urlForAdding(deviceId, type), requestBodyString, callback);
    }

    /**
     * 查询数据点
     *
     * @param deviceId 设备ID
     * @param params   URL参数 详见<a href="http://www.heclouds.com/doc/art260.html#68">
     *                 http://www.heclouds.com/doc/art260.html#68</a>
     * @param callback 回调接口对象
     */
    public static void queryDataPoints(String deviceId, Map<String, String> params, OneNetApiCallback callback) {
        get(DataPoint.urlForQuerying(deviceId, params), callback);
    }

    /******************** END ********************/

    /******************** 触发器相关api ********************/

    /**
     * 新增触发器
     *
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art259.html#68">
     *                          http://www.heclouds.com/doc/art259.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addTrigger(String requestBodyString, OneNetApiCallback callback) {
        post(Trigger.urlForAdding(), requestBodyString, callback);
    }

    /**
     * 更新触发器
     *
     * @param triggerId         触发器ID
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art259.html#68">
     *                          http://www.heclouds.com/doc/art259.html#68</a>
     * @param callback          回调接口对象
     */
    public static void updateTrigger(String triggerId, String requestBodyString, OneNetApiCallback callback) {
        put(Trigger.urlForUpdating(triggerId), requestBodyString, callback);
    }

    /**
     * 查询单个触发器
     *
     * @param triggerId 触发器ID
     * @param callback  回调接口对象
     */
    public static void querySingleTrigger(String triggerId, OneNetApiCallback callback) {
        get(Trigger.urlForQueryingSingle(triggerId), callback);
    }

    /**
     * 模糊查询触发器
     *
     * @param name     指定触发器名称
     * @param page     指定页码
     * @param perPager 指定每页输出个数，最多100
     * @param callback 回调接口对象
     */
    public static void fuzzyQueryTriggers(String name, int page, int perPager, OneNetApiCallback callback) {
        get(Trigger.urlForfuzzyQuerying(name, page, perPager), callback);
    }

    /**
     * 查询当前产品的所有触发器
     *
     * @param callback 回调接口对象
     */
    public static void fuzzyQueryTriggers(OneNetApiCallback callback) {
        get(Trigger.urlForfuzzyQuerying(null, 0, 0), callback);
    }

    /**
     * 删除触发器
     *
     * @param triggerId 触发器ID
     * @param callback  回调接口对象
     */
    public static void deleteTrigger(String triggerId, OneNetApiCallback callback) {
        delete(Trigger.urlForDeleting(triggerId), callback);
    }

    /******************** END ********************/

    /******************** 二进制相关api ********************/

    /**
     * 新增二进制数据
     *
     * @param deviceId         设备ID
     * @param dataStreamId     数据流ID
     * @param requestBodyBytes HTTP内容，普通二进制数据、文件、图像等（最大限制为800k）
     * @param callback         回调接口对象
     */
    public static void addBinaryData(String deviceId, String dataStreamId, byte[] requestBodyBytes, OneNetApiCallback callback) {
        post(Binary.urlForAdding(deviceId, dataStreamId), requestBodyBytes, callback);
    }

    /**
     * 新增二进制数据
     *
     * @param deviceId        设备ID
     * @param dataStreamId    数据流ID
     * @param requestBodyFile HTTP内容，文件、图像等（最大限制为800k）
     * @param callback        回调接口对象
     */
    public static void addBinaryData(String deviceId, String dataStreamId, File requestBodyFile, OneNetApiCallback callback) {
        post(Binary.urlForAdding(deviceId, dataStreamId), requestBodyFile, callback);
    }

    /**
     * 新增二进制数据
     *
     * @param deviceId          设备ID
     * @param dataStreamId      数据流ID
     * @param requestBodyString HTTP内容，普通二进制数据、文件、图像等（最大限制为800k）
     * @param callback          回调接口对象
     */
    public static void addBinaryData(String deviceId, String dataStreamId, String requestBodyString, OneNetApiCallback callback) {
        post(Binary.urlForAdding(deviceId, dataStreamId), requestBodyString, callback);
    }

    /**
     * 查询二进制数据
     *
     * @param index    二进制数据索引号
     * @param callback 回调接口对象
     */
    public static void queryBinaryData(String index, OneNetApiCallback callback) {
        get(Binary.urlForQuerying(index), callback);
    }

    /******************** END ********************/

    /******************** 命令相关api ********************/

    /**
     * 发送命令
     *
     * @param deviceId          设备ID
     * @param requestBodyString HTTP内容
     * @param callback          回调接口对象
     */
    public static void sendCmdToDevice(String deviceId, String requestBodyString, OneNetApiCallback callback) {
        post(Command.urlForSending(deviceId), requestBodyString, callback);
    }

    /**
     * 发送命令
     *
     * @param deviceId          设备ID
     * @param needResponse      是否需要响应
     * @param timeout           命令有效时间
     * @param type              命令类型，有CMD_REQ和PUSH_DATA
     * @param requestBodyString HTTP内容
     * @param callback          回调接口对象
     */
    public static void sendCmdToDevice(String deviceId, boolean needResponse, int timeout,
                                       Command.CommandType type, String requestBodyString,
                                       OneNetApiCallback callback) {
        post(Command.urlForSending(deviceId, needResponse, timeout, type), requestBodyString, callback);
    }

    /**
     * 查询命令状态
     *
     * @param cmdUuid  命令的UUID
     * @param callback 回调接口对象
     */
    public static void queryCmdStatus(String cmdUuid, OneNetApiCallback callback) {
        get(Command.urlForQueryingStatus(cmdUuid), callback);
    }

    /**
     * 查询命令响应
     *
     * @param cmdUuid  命令的UUID
     * @param callback 回调接口对象
     */
    public static void queryCmdResponse(String cmdUuid, OneNetApiCallback callback) {
        get(Command.urlForQueryingResponse(cmdUuid), callback);
    }

    /******************** END ********************/

    /******************** MQTT相关api ********************/

    /**
     * 按Topic发送命令
     *
     * @param topic             设备订阅的主题
     * @param requestBodyString 用户自定义数据：json、string、二进制数据（小于64K）
     * @param callback          回调接口对象
     */
    public static void sendCmdByTopic(String topic, String requestBodyString, OneNetApiCallback callback) {
        post(Mqtt.urlForSendingCmdByTopic(topic), requestBodyString, callback);
    }

    /**
     * 查询订阅指定Topic设备的列表
     *
     * @param topic    设备订阅的主题
     * @param page     指定页码
     * @param perPage  指定每页输出个数，最多1000
     * @param callback 回调接口对象
     */
    public static void queryDevicesByTopic(String topic, int page, int perPage, OneNetApiCallback callback) {
        get(Mqtt.urlForQueryingDevicesByTopic(topic, page, perPage), callback);
    }

    /**
     * 查询设备订阅的Topic列表
     *
     * @param deviceId 设备ID
     * @param callback 回调接口对象
     */
    public static void queryDeviceTopics(String deviceId, OneNetApiCallback callback) {
        get(Mqtt.urlForQueryingDeviceTopics(deviceId), callback);
    }

    /**
     * 创建产品的Topic
     *
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art256.html#68">
     *                          http://www.heclouds.com/doc/art256.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addTopic(String requestBodyString, OneNetApiCallback callback) {
        post(Mqtt.urlForAddingTopic(), requestBodyString, callback);
    }

    /**
     * 删除产品的Topic
     *
     * @param topic    主题名称
     * @param callback 回调接口对象
     */
    public static void deleteTopic(String topic, OneNetApiCallback callback) {
        delete(Mqtt.urlForDeletingTopic(topic), callback);
    }

    /**
     * 查询产品Topic
     *
     * @param callback 回调接口对象
     */
    public static void queryTopics(OneNetApiCallback callback) {
        get(Mqtt.urlForQueryingTopics(), callback);
    }

    /******************** END ********************/

    /******************** ApiKey相关api ********************/

    /**
     * 新增API key
     *
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art296.html#68">
     *                          http://www.heclouds.com/doc/art296.html#68</a>
     * @param callback          回调接口对象
     */
    public static void addApiKey(String requestBodyString, OneNetApiCallback callback) {
        post(ApiKey.urlForAdding(), requestBodyString, callback);
    }

    /**
     * 更新API key
     *
     * @param key               API key内容
     * @param requestBodyString HTTP内容 详见<a href="http://www.heclouds.com/doc/art296.html#68">
     *                          http://www.heclouds.com/doc/art296.html#68</a>
     * @param callback          回调接口对象
     */
    public static void updateApiKey(String key, String requestBodyString, OneNetApiCallback callback) {
        put(ApiKey.urlForUpdating(key), requestBodyString, callback);
    }

    /**
     * 查询API key
     *
     * @param key      API key内容
     * @param callback 回调接口对象
     */
    public static void queryApiKey(String key, OneNetApiCallback callback) {
        get(ApiKey.urlForQuerying(key, 0, 0, null), callback);
    }

    /**
     * 查询API key
     *
     * @param key      API key内容
     * @param page     指定页码
     * @param perPage  指定每页输出个数，最多100
     * @param deviceId 设备ID
     * @param callback 回调接口对象
     */
    public static void queryApiKey(String key, int page, int perPage, String deviceId, OneNetApiCallback callback) {
        get(ApiKey.urlForQuerying(key, page, perPage, deviceId), callback);
    }

    /**
     * 删除API key
     *
     * @param key      API key内容
     * @param callback 回调接口对象
     */
    public static void deleteApiKey(String key, OneNetApiCallback callback) {
        delete(ApiKey.urlForDeleting(key), callback);
    }

    /******************** END ********************/
}

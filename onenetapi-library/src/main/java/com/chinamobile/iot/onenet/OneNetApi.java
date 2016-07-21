package com.chinamobile.iot.onenet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chinamobile.iot.onenet.apikey.AddApiKey;
import com.chinamobile.iot.onenet.apikey.DeleteApiKey;
import com.chinamobile.iot.onenet.apikey.EditApiKey;
import com.chinamobile.iot.onenet.apikey.GetApiKey;
import com.chinamobile.iot.onenet.bindata.AddBinData;
import com.chinamobile.iot.onenet.bindata.DeleteBinData;
import com.chinamobile.iot.onenet.cmds.SendToEdp;
import com.chinamobile.iot.onenet.datapoint.AddDatapoint;
import com.chinamobile.iot.onenet.datapoint.DeleteDatapoints;
import com.chinamobile.iot.onenet.datapoint.GetDatapoints;
import com.chinamobile.iot.onenet.datapoint.GetHistoryDatapoints;
import com.chinamobile.iot.onenet.datastream.AddDatastream;
import com.chinamobile.iot.onenet.datastream.DeleteDatastream;
import com.chinamobile.iot.onenet.datastream.EditDatastream;
import com.chinamobile.iot.onenet.datastream.GetDatastream;
import com.chinamobile.iot.onenet.datastream.GetDatastreams;
import com.chinamobile.iot.onenet.device.AddDevice;
import com.chinamobile.iot.onenet.device.DeleteDevice;
import com.chinamobile.iot.onenet.device.EditDevice;
import com.chinamobile.iot.onenet.device.GetDevice;
import com.chinamobile.iot.onenet.device.GetDevices;
import com.chinamobile.iot.onenet.logs.GetRestAPILogs;
import com.chinamobile.iot.onenet.trigger.AddTrigger;
import com.chinamobile.iot.onenet.trigger.DeleteTrigger;
import com.chinamobile.iot.onenet.trigger.EditTrigger;
import com.chinamobile.iot.onenet.trigger.GetTrigger;

import android.content.Context;

/**
 * <p>ChinaMobile OneNet API<p>
 *
 * @author chenglei
 *
 */
public class OneNetApi {

    private static OneNetApi sInstance;

    public static boolean mDebugEable = true;

    /**
     * 请求队列
     */
    private RequestQueue mRequestQueue;

    private OneNetApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static OneNetApi getInstance(Context context) {
        if (null == sInstance) {
            synchronized (OneNetApi.class) {
                if (null == sInstance) {
                    sInstance = new OneNetApi(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void setDebugEnable(boolean debugEnable) {
        mDebugEable = debugEnable;
    }

    /**
     * 批量查看设备
     */
    public void getDevices(String apiKey, String page, String perPage,
            String keyWords, String tag, String online, String isPrivate,
            ResponseListener listener) {

        StringBuilder sb = new StringBuilder();
        if (null == page) page = "1";
        sb.append("?page=" + page);
        if (null == perPage) perPage = "30";
        sb.append("&per_page=" + perPage);
        if (keyWords != null) {
            try {
                keyWords = URLEncoder.encode(keyWords, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&key_words=" + keyWords);
        }
        if (tag != null) {
            try {
                tag = URLEncoder.encode(tag, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&tag=" + tag);
        }
        if (online != null) sb.append("&online=" + online);
        if (isPrivate != null) sb.append("&private=" + isPrivate);

        StringRequest request = new GetDevices(apiKey, sb.toString(), listener);
        mRequestQueue.add(request);
    }

    /**
     * 查看单个设备
     */
    public void getDevice(String apiKey, String deviceId, ResponseListener listener) {
        StringRequest request = new GetDevice(apiKey, deviceId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 增加设备
     */
    public void addDevice(String apiKey, String title, String desc, boolean isPrivate, String routeTo,
            JSONObject authInfo, ResponseListener listener) {

        StringRequest request = new AddDevice(apiKey, title, desc, isPrivate, routeTo, authInfo, listener);
        mRequestQueue.add(request);
    }

    /**
     * 增加设备
     *
     * @param body
     *          请参考OneNET API文档中请求内容部分 http://open.iot.10086.cn/apidoc/device/create.html
     */
    public void addDevice(String apiKey, JSONObject body, ResponseListener listener) {
        StringRequest request = new AddDevice(apiKey, body, listener);
        mRequestQueue.add(request);
    }

    /**
     * 删除设备
     */
    public void deleteDevice(String apiKey, String deviceId,
            ResponseListener listener) {

        StringRequest request = new DeleteDevice(apiKey, deviceId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 编辑设备
     */
    public void editDevice(
            String apiKey, final String deviceId, final String title,
            final String desc, final boolean isPrivate,
            ResponseListener listener) {

        StringRequest request = new EditDevice(apiKey, deviceId, title, desc, isPrivate, listener);
        mRequestQueue.add(request);
    }

    /**
     * 增加数据流
     */
    public void addDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {

        StringRequest request = new AddDatastream(
                apiKey, deviceId, streamId, unit, unitSymbol, listener);
        mRequestQueue.add(request);
    }

    /**
     * 编辑数据流
     */
    public void editDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {

        try {
            streamId = URLEncoder.encode(streamId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new EditDatastream(
                apiKey, deviceId, streamId, unit, unitSymbol, listener);
        mRequestQueue.add(request);
    }

    /**
     * 查看数据流
     */
    public void getDatastream(String apiKey, String deviceId, String streamId,
            ResponseListener listener) {

        try {
            streamId = URLEncoder.encode(streamId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new GetDatastream(
                apiKey, deviceId, streamId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 删除数据流
     */
    public void deleteDatastream(String apiKey, String deviceId, String streamId,
            ResponseListener listener) {

        try {
            streamId = URLEncoder.encode(streamId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new DeleteDatastream(
                apiKey, deviceId, streamId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 读取多个数据流
     */
    public void getDatastreams(String apiKey, String deviceId, String[] datastreamIds, ResponseListener listener) {
        StringBuilder sb = new StringBuilder();
        if (datastreamIds != null && datastreamIds.length > 0) {
            sb.append("?datastreamIds=");
            for (String id : datastreamIds) {
                if (id != null) {
                    try {
                        sb.append(URLEncoder.encode(id, "utf-8"));
                        sb.append(",");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        StringRequest request = new GetDatastreams(apiKey, deviceId, sb.toString(), listener);
        mRequestQueue.add(request);
    }

    /**
     * 上报数据点（一条数据流）
     */
    public void addDatapoint(String apiKey, String deviceId, String datastreamId, JSONArray datapoints,
            ResponseListener listener) {

        StringRequest request = new AddDatapoint(apiKey, deviceId, datastreamId, datapoints, listener);
        mRequestQueue.add(request);
    }

    /**
     * 上报数据点（多条数据流）
     */
    public void addDatapoint(String apiKey, String deviceId, JSONObject data,
            ResponseListener listener) {

        StringRequest request = new AddDatapoint(apiKey, deviceId, data, listener);
        mRequestQueue.add(request);
    }

    /**
     * 读取数据点
     */
    public void getDatapoints(String apiKey, String deviceId, String datastreamId, String start, String end,
            String limit, String cursor, String interval, String method, String first, ResponseListener listener) {

        Map<String, String> params = new HashMap<String, String>();
        if (datastreamId != null)
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        if (start != null) params.put("start", start);
        if (end != null) params.put("end", end);
        if (limit != null) params.put("limit", limit);
        if (cursor != null) params.put("cursor", cursor);
        if (interval != null) params.put("interval", interval);
        if (method != null) params.put("method", method);
        if (first != null) params.put("first", first);
        String urlParams = buildUrlParams(params);

        StringRequest request = new GetDatapoints(apiKey, deviceId, urlParams, listener);
        mRequestQueue.add(request);
    }

    /**
     * 历史数据查询
     * @param start （必选）指定开始时间，时间格式ISO 8601 示例:2012-06-02T14:01:46
     */
    public void getHistoryDatapoints(String apiKey, String start, String deviceId, String datastreamId,
            String end, String limit, String cursor, String duration, String page, ResponseListener listener) {

        Map<String, String> params = new HashMap<String, String>();
        if (start != null) params.put("start", start);
        if (deviceId != null) params.put("device_id", deviceId);
        if (datastreamId != null) {
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (end != null) params.put("end", end);
        if (limit != null) params.put("limit", limit);
        if (cursor != null) params.put("cursor", cursor);
        if (duration != null) params.put("duration", duration);
        if (page != null) params.put("page", page);

        String urlParams = buildUrlParams(params);
        StringRequest request = new GetHistoryDatapoints(apiKey, urlParams, listener);
        mRequestQueue.add(request);
    }

    /**
     * 删除数据点
     */
    public void deleteDatapoints(String apiKey, String deviceId, String datastreamId, String start, String end,
            String duration, ResponseListener listener) {

        Map<String, String> params = new HashMap<String, String>();
        if (datastreamId != null)
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        if (start != null) params.put("start", start);
        if (end != null) params.put("end", end);
        if (duration != null) params.put("duration", duration);
        String urlParams = buildUrlParams(params);

        StringRequest request = new DeleteDatapoints(apiKey, deviceId, urlParams, listener);
        mRequestQueue.add(request);
    }

    /**
     * 新增触发器
     *
     * @param url
     *          触发器的HTTP请求地址
     */
    public void addTrigger(String apiKey, String url, String type, double threshold,
            String datastreamId, ArrayList<String> deviceIds, ArrayList<String> dsUUIDs,
            ResponseListener listener) {

        StringRequest request = new AddTrigger(apiKey, url, type, threshold,
                datastreamId, deviceIds, dsUUIDs, listener);
        mRequestQueue.add(request);
    }

    /**
     * 编辑触发器
     */
    public void editTrigger(String apiKey, String triggerId, String url, String type, double threshold,
                            String datastreamId, ArrayList<String> deviceIds, ArrayList<String> dsUUIDs,
                            ResponseListener listener) {

        StringRequest request = new EditTrigger(apiKey, triggerId, url, type, threshold,
                datastreamId, deviceIds, dsUUIDs, listener);
        mRequestQueue.add(request);
    }

    /**
     * 读取触发器
     */
    public void getTrigger(String apiKey, String triggerId, ResponseListener listener) {
        StringRequest request = new GetTrigger(apiKey, triggerId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 删除触发器
     */
    public void deleteTrigger(String apiKey, String triggerId, ResponseListener listener) {
        StringRequest request = new DeleteTrigger(apiKey, triggerId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 读取APIKey
     */
    public void getApikey(String masterKey, String deviceId, String keyString, ResponseListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        if (deviceId != null) params.put("dev_id", deviceId);
        if (keyString != null) params.put("key", keyString);
        String urlParams = buildUrlParams(params);
        StringRequest request = new GetApiKey(masterKey, urlParams, listener);
        mRequestQueue.add(request);
    }

    /**
     * 新增APIKey
     */
    public void addApiKey(String masterKey, String title, String deviceId, String datastreamId, ResponseListener listener) {
        StringRequest request = new AddApiKey(masterKey, title, deviceId, datastreamId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 编辑APIKey
     */
    public void editApiKey(String masterKey, String keyString, String title, String deviceId, String datastreamId,
            ResponseListener listener) {

        StringRequest request = new EditApiKey(masterKey, keyString, title, deviceId, datastreamId, listener);
        mRequestQueue.add(request);
    }

    /**
     * 删除APIKey
     */
    public void deleteApikey(String masterKey, String keyString, ResponseListener listener) {
        StringRequest request = new DeleteApiKey(masterKey, keyString, listener);
        mRequestQueue.add(request);
    }

    /**
     * 发送字符串到EDP设备
     */
    public void sendToEdp(String apiKey, String deviceId, String text, ResponseListener listener) {
        StringRequest request = new SendToEdp(apiKey, deviceId, text, listener);
        mRequestQueue.add(request);
    }

    /**
     * 发送文件到EDP设备
     *
     * @deprecated 该方法已废弃，后续版本中可能移除。<br/>
     * 请使用 {@link #sendToEdp(String, String, byte[], ResponseListener)} or
     * {@link #sendToEdp(String, String, String, ResponseListener)} 替代。<br/>
     * 由于发送内容限制为小于64KB，所以不必使用缓存，可以直接以字符串或二进制数组的形式发送。
     *
     */
    @Deprecated
    public void sendToEdp(String apiKey, String deviceId, File file, ResponseListener listener) {
        byte[] bytes = new byte[1024 * 64];
        try {
            InputStream stream = new FileInputStream(file);
            stream.read(bytes, 0, stream.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringRequest request = new SendToEdp(apiKey, deviceId, bytes, listener);
        mRequestQueue.add(request);
    }

    /**
     * 发送二进制字节流到EDP设备
     *
     * @deprecated 该方法已废弃，后续版本中可能移除。<br/>
     * 请使用 {@link #sendToEdp(String, String, byte[], ResponseListener)} or
     * {@link #sendToEdp(String, String, String, ResponseListener)} 替代。<br/>
     * 由于发送内容限制为小于64KB，所以不必使用缓存，可以直接以字符串或二进制数组的形式发送。
     *
     */
    @Deprecated
    public void sendToEdp(String apiKey, String deviceId, InputStream stream, ResponseListener listener) {
        byte[] bytes = new byte[1024 * 64];
        try {
            stream.read(bytes, 0, stream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringRequest request = new SendToEdp(apiKey, deviceId, bytes, listener);
        mRequestQueue.add(request);
    }

    /**
     * 发送二进制字节数组到EDP设备
     */
    public void sendToEdp(String apiKey, String deviceId, byte[] bytes, ResponseListener listener) {
        StringRequest request = new SendToEdp(apiKey, deviceId, bytes, listener);
        mRequestQueue.add(request);
    }

    /**
     * 上传文件
     */
    public void addBinData(String apiKey, String deviceId, String datastreamId, String desc, String at, File file, ResponseListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        if (deviceId != null) params.put("device_id", deviceId);
        if (datastreamId != null) {
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (desc != null) params.put("desc", desc);
        if (at != null) params.put("at", desc);
        String urlParams = buildUrlParams(params);
        AddBinData request = new AddBinData(apiKey, urlParams, listener);
        request.send(file);
    }

    /**
     * 上传二进制字节流
     */
    public void addBinData(String apiKey, String deviceId, String datastreamId, String desc, String at, InputStream stream, ResponseListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        if (deviceId != null) params.put("device_id", deviceId);
        if (datastreamId != null) {
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (desc != null) params.put("desc", desc);
        if (at != null) params.put("at", desc);
        String urlParams = buildUrlParams(params);
        AddBinData request = new AddBinData(apiKey, urlParams, listener);
        request.send(stream);
    }

    /**
     * 上传字符串
     */
    public void addBinData(String apiKey, String deviceId, String datastreamId, String desc, String at, String text, ResponseListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        if (deviceId != null) params.put("device_id", deviceId);
        if (datastreamId != null) {
            try {
                params.put("datastream_id", URLEncoder.encode(datastreamId, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (desc != null) params.put("desc", desc);
        if (at != null) params.put("at", desc);
        String urlParams = buildUrlParams(params);
        AddBinData request = new AddBinData(apiKey, urlParams, listener);
        request.send(text);
    }

    /**
     * 删除二进制数据
     */
    public void deleteBinData(String apiKey, String index, ResponseListener listener) {
        StringRequest request = new DeleteBinData(apiKey, index, listener);
        mRequestQueue.add(request);
    }

    /**
     * RestAPI日志查询
     */
    public void getRestAPILogs(String apiKey, String deviceId, ResponseListener listener) {
        mRequestQueue.add(new GetRestAPILogs(apiKey, deviceId, listener));
    }

    private String buildUrlParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (!params.isEmpty()) {
            sb.append("?");
            Set<String> keys = params.keySet();

            for (Iterator<String> it = keys.iterator(); it.hasNext();) {
                String key = it.next();
                String value = params.get(key);
                sb.append(key + "=" + value);
                sb.append("&");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}

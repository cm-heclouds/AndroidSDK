package com.chinamobile.iot.onenet.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetError;
import com.chinamobile.iot.onenet.OneNetResponse;
import com.chinamobile.iot.onenet.ResponseListener;
import com.chinamobile.iot.onenet.apisample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    private String mTempDeviceId;
    private Preferences mPreferences;
    private String mTempStreamId = "测试数据流";
    private String mTempTriggerId;
    private String mTempApiKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_avtivity);
        mPreferences = Preferences.getInstance(this);
        mTempDeviceId = mPreferences.getDeviceId();
        mTempTriggerId = mPreferences.getTriggerId();
        mTempApiKey = mPreferences.getApiKey();
    }

    public void addDevice(View v) {
        OneNetApi.getInstance(this).addDevice(SampleApp.sApiKey, "OneNetApi测试", "OneNetApi测试", false, "http://www.baidu.com", null, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                if (response.getErrno() == 0) {
                    // 请求成功

                    String data = response.getData();
                    if (data != null) {
                        try {
                            JSONObject dataObj = new JSONObject(data);
                            mTempDeviceId = dataObj.optString("device_id");
                            mPreferences.setDeviceId(mTempDeviceId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    // 连接服务器成功，但请求发生错误
                }
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }
        });
    }

    public void getDevice(View v) {
        OneNetApi.getInstance(this).getDevice(SampleApp.sApiKey, mTempDeviceId, new ResponseListener() {

            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }
        });
    }
    
    public void getDevices(View v) {
        OneNetApi.getInstance(this).getDevices(SampleApp.sApiKey, null, null, null, null, null, null, new ResponseListener() {

            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }

        });
    }

    public void deleteDevice(View v) {
        OneNetApi.getInstance(this).deleteDevice(SampleApp.sApiKey, mTempDeviceId, new ResponseListener() {

            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                if (0 == response.getErrno()) {
                    mTempDeviceId = null;
                    mPreferences.deleteDeviceId();
                }
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }

        });
    }

    public void editDevice(View v) {
        OneNetApi.getInstance(this).editDevice(SampleApp.sApiKey, mTempDeviceId, "OneNetApi测试1", "OneNetApi测试1", true, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }
        });
    }

    public void addDatastream(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).addDatastream(SampleApp.sApiKey, mTempDeviceId, mTempStreamId, "a", "b", new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }
        });
    }

    public void getDatastream(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }

        OneNetApi.getInstance(this).getDatastream(SampleApp.sApiKey, mTempDeviceId, mTempStreamId, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void getDatastreams(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).getDatastreams(SampleApp.sApiKey, mTempDeviceId, null, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void editDatastream(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).editDatastream(SampleApp.sApiKey, mTempDeviceId, mTempStreamId, "c", "d", new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void deleteDatastream(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).deleteDatastream(SampleApp.sApiKey, mTempDeviceId, mTempStreamId, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                if (0 == response.getErrno()) {
                    mTempStreamId = null;
                }
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void addDatapoint(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray datapoints = new JSONArray();
        JSONObject datapoint = new JSONObject();
        try {
            datapoint.put("value", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        datapoints.put(datapoint);
        OneNetApi.getInstance(this).addDatapoint(SampleApp.sApiKey, mTempDeviceId, mTempStreamId, datapoints, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void getDatapoints(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 5);
        OneNetApi.getInstance(this).getDatapoints(
                SampleApp.sApiKey, mTempDeviceId, mTempStreamId, sdf.format(c.getTime()),
                null, null, null, String.valueOf(6 * 3600), new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void getHistoryDatapoints(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 5);
        OneNetApi.getInstance(this).getHistoryDatapoints(SampleApp.sApiKey, sdf.format(c.getTime()),
                mTempDeviceId, mTempStreamId, null, null, null, null, new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void deleteDatapoint(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 5);
        OneNetApi.getInstance(this).deleteDatapoints(SampleApp.sApiKey, mTempDeviceId, mTempStreamId,
                sdf.format(c.getTime()), null, String.valueOf(6 * 3600), new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void addTrigger(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).addTrigger(SampleApp.sApiKey, "http://www.baidu.com", ">", 10,
                mTempStreamId, null, null, new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                        if (0 == response.getErrno()) {
                            String data = response.getData();
                            if (data != null) {
                                try {
                                    JSONObject obj = new JSONObject(data);
                                    mTempTriggerId = obj.optString("trigger_id");
                                    mPreferences.setTriggerId(mTempTriggerId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void editTrigger(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).editTrigger(SampleApp.sApiKey, mTempTriggerId, "http://www.hao123.com", "<", 20,
                mTempStreamId, null, null, new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void getTrigger(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).getTrigger(SampleApp.sApiKey, mTempTriggerId, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void deleteTrigger(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).deleteTrigger(SampleApp.sApiKey, mTempTriggerId, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                if (0 == response.getErrno()) {
                    mPreferences.deleteTriggerId();
                    mTempTriggerId = null;
                }
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void addAPIKey(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).addApiKey(SampleApp.sApiKey, "TestApiKey", mTempDeviceId, mTempStreamId, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                if (0 == response.getErrno()) {
                    String data = response.getData();
                    if (data != null) {
                        try {
                            JSONObject obj = new JSONObject(data);
                            mTempApiKey = obj.optString("key");
                            mPreferences.setApiKey(mTempApiKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void editAPIKey(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).editApiKey(SampleApp.sApiKey, mTempApiKey, "TestApiKey2",
                mTempDeviceId, mTempStreamId, new ResponseListener() {

                    @Override
                    public void onResponse(OneNetResponse response) {
                        RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                    }

                    @Override
                    public void onError(OneNetError error) {

                    }
                });
    }

    public void getAPIKey(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).getApikey(SampleApp.sApiKey, mTempDeviceId, mTempApiKey, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

    public void deleteAPIKey(View v) {
        if (TextUtils.isEmpty(mTempDeviceId)) {
            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
            return;
        }
        OneNetApi.getInstance(this).deleteApikey(SampleApp.sApiKey, mTempApiKey, new ResponseListener() {
            @Override
            public void onResponse(OneNetResponse response) {
                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
                if (0 == response.getErrno()) {
                    mPreferences.deleteApiKey();
                    mTempApiKey = null;
                }
            }

            @Override
            public void onError(OneNetError error) {

            }
        });
    }

//    public void getLogs(View v) {
//        if (TextUtils.isEmpty(mTempDeviceId)) {
//            Toast.makeText(this, "请先添加设备", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        OneNetApi.getInstance(this).getRestAPILogs(SampleApp.sApiKey, mTempDeviceId, new ResponseListener() {
//            @Override
//            public void onResponse(OneNetResponse response) {
//                RequestLogActivity.actionLogActivity(MainActivity.this, response.getRawResponse());
//            }
//
//            @Override
//            public void onError(OneNetError error) {
//
//            }
//        });
//    }

    public void test(View v) {

//        OneNetApi.getInstance(this).getTrigger(SampleApp.sApiKey, "11307", null);
//        OneNetApi.getInstance(this).addBinData(SampleApp.sApiKey, "133898", "123", null, null, "1234567890", null);
//        OneNetApi.getInstance(this).getHistoryDatapoints(SampleApp.sApiKey, "2015-04-01T00:00:00", null, null, null, null, null, null, null);
    }

}

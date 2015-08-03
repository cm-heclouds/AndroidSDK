package com.chinamobile.iot.onenet.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetError;
import com.chinamobile.iot.onenet.OneNetResponse;
import com.chinamobile.iot.onenet.ResponseListener;
import com.chinamobile.iot.onenet.apisample.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private String mTempDeviceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_avtivity);
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
            }

            @Override
            public void onError(OneNetError error) {
                // 网络或服务器错误
            }

        });
    }

    public void test(View v) {

//        OneNetApi.getInstance(this).getTrigger(SampleApp.sApiKey, "11307", null);
//        OneNetApi.getInstance(this).addBinData(SampleApp.sApiKey, "133898", "123", null, null, "1234567890", null);
//        OneNetApi.getInstance(this).getHistoryDatapoints(SampleApp.sApiKey, "2015-04-01T00:00:00", null, null, null, null, null, null, null);
    }

}

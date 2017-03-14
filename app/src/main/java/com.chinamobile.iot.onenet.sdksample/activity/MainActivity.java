package com.chinamobile.iot.onenet.sdksample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fuzzy_query_devices).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OneNetApi.fuzzyQueryDevices(null, new OneNetApiCallback() {
            @Override
            public void onSuccess(int errno, String error, String data) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}

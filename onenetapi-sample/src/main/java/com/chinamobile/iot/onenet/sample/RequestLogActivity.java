package com.chinamobile.iot.onenet.sample;

import com.chinamobile.iot.onenet.apisample.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class RequestLogActivity extends Activity {

    private EditText mLogText;

    private static final String EXTRA_LOG = "extra_log";

    public static void actionLogActivity(Context context, String log) {
        Intent intent = new Intent(context, RequestLogActivity.class);
        intent.putExtra(EXTRA_LOG, log);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_log_activity);

        mLogText = (EditText) findViewById(R.id.log_text);

        String log = getIntent().getStringExtra(EXTRA_LOG);
        mLogText.setText(log);
    }

}

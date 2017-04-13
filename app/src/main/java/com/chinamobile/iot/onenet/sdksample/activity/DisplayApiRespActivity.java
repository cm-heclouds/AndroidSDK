package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.chinamobile.iot.onenet.sdksample.R;

public class DisplayApiRespActivity extends AppCompatActivity {

    private static final String EXTRA_LOG = "extra_log";

    private Toolbar mToolbar;
    private TextView mTextView;

    public static void actionDisplayApiResp(Context context, String log) {
        Intent intent = new Intent(context, DisplayApiRespActivity.class);
        intent.putExtra(EXTRA_LOG, log);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_api_resp);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.resp_display);
        String log = getIntent().getStringExtra(EXTRA_LOG);
        if (log != null) {
            mTextView.setText(log);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

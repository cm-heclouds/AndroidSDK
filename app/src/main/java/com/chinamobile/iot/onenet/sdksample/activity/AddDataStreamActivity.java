package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDataStreamActivity extends AppCompatActivity {

    public static final String EXTRA_DEVICE_ID = "extra_device_id";

    private Toolbar mToolbar;

    private TextInputLayout mDsIdLayout;
    private TextInputLayout mDsUnitNameLayout;
    private TextInputLayout mDsUnitSymbolLayout;

    private String mDeviceId;

    public static void actionAddDataStream(Context context, String deviceId) {
        Intent intent = new Intent(context, AddDataStreamActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_datastream);
        mDeviceId = getIntent().getStringExtra(EXTRA_DEVICE_ID);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDsIdLayout = (TextInputLayout) findViewById(R.id.ds_id);
        mDsUnitNameLayout = (TextInputLayout) findViewById(R.id.ds_unit_name);
        mDsUnitSymbolLayout = (TextInputLayout) findViewById(R.id.ds_unit_symbol);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_done:
                if (checkInput(mDsIdLayout, R.string.ds_id)) {
                    addDataStream();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkInput(TextInputLayout textInputLayout, int errorResId) {
        String text = textInputLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(text)) {
            textInputLayout.setError(getResources().getString(errorResId));
            textInputLayout.requestFocus();
            return false;
        }
        return true;
    }

    private void addDataStream() {
        String dsId = mDsIdLayout.getEditText().getText().toString();
        String unitName = mDsUnitNameLayout.getEditText().getText().toString();
        String unitSymbol = mDsUnitSymbolLayout.getEditText().getText().toString();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.putOpt("id", dsId);
            if (!TextUtils.isEmpty(unitName)) {
                requestObject.putOpt("unit", unitName);
            }
            if (!TextUtils.isEmpty(unitSymbol)) {
                requestObject.putOpt("unit_symbol", unitSymbol);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneNetApi.addDataStream(mDeviceId, requestObject.toString(), new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    int errno = resp.optInt("errno");
                    String error = resp.optString("error");
                    if (0 == errno) {
                        Toast.makeText(getApplicationContext(), R.string.added_successfully, Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_DS_LIST));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }
}

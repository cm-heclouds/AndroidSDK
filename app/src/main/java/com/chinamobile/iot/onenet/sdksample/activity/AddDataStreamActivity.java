package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

    private TextInputEditText mDsIdEditText;
    private TextInputEditText mDsUnitNameEditText;
    private TextInputEditText mDsUnitSymbolEditText;

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

        mDsIdEditText = (TextInputEditText) findViewById(R.id.ds_id);
        mDsUnitNameEditText = (TextInputEditText) findViewById(R.id.ds_unit_name);
        mDsUnitSymbolEditText = (TextInputEditText) findViewById(R.id.ds_unit_symbol);
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
                if (checkInput(mDsIdEditText, R.string.ds_id)) {
                    addDataStream();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkInput(EditText editText, int errorResId) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editText.setError(getResources().getString(errorResId));
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void addDataStream() {
        String dsId = mDsIdEditText.getText().toString();
        String unitName = mDsUnitNameEditText.getText().toString();
        String unitSymbol = mDsUnitSymbolEditText.getText().toString();

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
            public void onSuccess(int errno, String error, String data) {
                if (0 == errno) {
                    Toast.makeText(getApplicationContext(), R.string.added_successfully, Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_DS_LIST));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }
}

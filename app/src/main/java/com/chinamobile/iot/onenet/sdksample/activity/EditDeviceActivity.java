package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class EditDeviceActivity extends AppCompatActivity {

    public static final String EXTRA_DEVICE_ITEM = "extra_device_item";

    private Toolbar mToolbar;

    private TextInputLayout mDeviceTitleLayout;

    private TextInputLayout mAuthInfoLayout;
    private View mModbusAuthInfoLayout;
    private View mJTextAuthInfoLayout;

    private TextInputLayout mDtuNumberLayout;
    private TextInputLayout mDtuPasswordLayout;
    private TextInputLayout mDeviceModelLayout;
    private TextInputLayout mDeviceIdLayout;
    private RadioGroup mRadioGroup;
    private boolean mPrivate;

    private String[] mProtocols;
    private String mProtocol;

    private DeviceItem mDeviceItem;

    public static void actionEditDevice(Context context, DeviceItem deviceItem) {
        Intent intent = new Intent(context, EditDeviceActivity.class);
        intent.putExtra(EXTRA_DEVICE_ITEM, deviceItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);
        mDeviceItem = (DeviceItem) getIntent().getSerializableExtra(EXTRA_DEVICE_ITEM);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeviceTitleLayout = (TextInputLayout) findViewById(R.id.title);
        mAuthInfoLayout = (TextInputLayout) findViewById(R.id.auth_info_layout);
        mModbusAuthInfoLayout = findViewById(R.id.modbus_auth_info);
        mJTextAuthInfoLayout = findViewById(R.id.jtext_auth_info);
        mDtuNumberLayout = (TextInputLayout) findViewById(R.id.dtu_number_layout);
        mDtuPasswordLayout = (TextInputLayout) findViewById(R.id.dtu_password_layout);
        mDeviceModelLayout = (TextInputLayout) findViewById(R.id.model_layout);
        mDeviceIdLayout = (TextInputLayout) findViewById(R.id.id_layout);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mProtocols = getResources().getStringArray(R.array.protocols);
        mProtocol = mDeviceItem.getProtocol();

        mDeviceTitleLayout.getEditText().setText(mDeviceItem.getTitle());
        mRadioGroup.check(mDeviceItem.isPrivate() ? R.id.radio_private : R.id.radio_public);

        switch (mProtocol.toUpperCase()) {
            case "HTTP":
            case "EDP":
            case "MQTT":
                mAuthInfoLayout.setVisibility(View.VISIBLE);
                mModbusAuthInfoLayout.setVisibility(View.GONE);
                mJTextAuthInfoLayout.setVisibility(View.GONE);
                mAuthInfoLayout.getEditText().setText(mDeviceItem.getAuthInfo());
                break;

            case "MODBUS":
                mAuthInfoLayout.setVisibility(View.GONE);
                mModbusAuthInfoLayout.setVisibility(View.VISIBLE);
                mJTextAuthInfoLayout.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(mDeviceItem.getAuthInfo());
                    String key = object.keys().next();
                    mDtuNumberLayout.getEditText().setText(key);
                    mDtuPasswordLayout.getEditText().setText(object.optString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "JTEXT":
                mAuthInfoLayout.setVisibility(View.GONE);
                mModbusAuthInfoLayout.setVisibility(View.GONE);
                mJTextAuthInfoLayout.setVisibility(View.VISIBLE);
                mDeviceModelLayout.getEditText().setText(mDeviceItem.getActivateCode().getMt());
                mDeviceIdLayout.getEditText().setText(mDeviceItem.getActivateCode().getMid());
                break;
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mPrivate = (R.id.radio_private == checkedId);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_done:
                if (checkValid()) {
                    addDevice();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean checkValid() {
        boolean valid = checkInput(mDeviceTitleLayout, R.string.device_title_empty_error);
        if (valid) {
            switch (mProtocol.toUpperCase()) {
                case "HTTP":
                case "EDP":
                case "MQTT":
                    valid = checkInput(mAuthInfoLayout, R.string.auth_info_empty_error);
                    break;

                case "MODBUS":
                    valid = checkInput(mDtuNumberLayout, R.string.dtu_serial_number_empty_error);
                    if (valid) {
                        valid = checkInput(mDtuPasswordLayout, R.string.dtu_password_empty_error);
                    }
                    break;

                case "JTEXT":
                    valid = checkInput(mDeviceModelLayout, R.string.device_model_empty_drror);
                    if (valid) {
                        valid = checkInput(mDeviceIdLayout, R.string.device_id_empty_error);
                    }
                    break;
            }
        }
        return valid;
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

    private void addDevice() {
        JSONObject requestContent = new JSONObject();
        try {
            requestContent.putOpt("title", mDeviceTitleLayout.getEditText().getText().toString());
            requestContent.putOpt("private", mPrivate);
            switch (mProtocol.toUpperCase()) {
                case "HTTP":
                case "EDP":
                case "MQTT": {
                    String authInfoString = mAuthInfoLayout.getEditText().getText().toString();
                    if (!authInfoString.equals(mDeviceItem.getAuthInfo())) {
                        requestContent.putOpt("auth_info", authInfoString);
                    }
                    break;
                }
                case "MODBUS": {
                    JSONObject authInfo = new JSONObject();
                    authInfo.putOpt(mDtuNumberLayout.getEditText().getText().toString(), mDtuPasswordLayout.getEditText().getText().toString());
                    if (!authInfo.toString().equals(mDeviceItem.getAuthInfo())) {
                        requestContent.putOpt("auth_info", authInfo);
                    }
                    break;
                }

                case "JTEXT":
                    JSONObject activateCode = new JSONObject();
                    activateCode.putOpt("mt", mDeviceModelLayout.getEditText().getText().toString());
                    activateCode.putOpt("mid", mDeviceIdLayout.getEditText().getText().toString());
                    requestContent.putOpt("activate_code", activateCode);
                    break;
            }
            OneNetApi.updateDevice(mDeviceItem.getId(), requestContent.toString(), new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                    int errno = resp.get("errno").getAsInt();
                    if (0 == errno) {
                        Toast.makeText(getApplicationContext(), R.string.modified_successfully, Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(EditDeviceActivity.this).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_DEVICE));
                        LocalBroadcastManager.getInstance(EditDeviceActivity.this).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_DEVICE_LIST));
                        finish();
                    } else {
                        String error = resp.get("error").getAsString();
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

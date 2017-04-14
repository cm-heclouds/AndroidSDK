package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDeviceActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mDeviceTitleEditText;

    private TextInputLayout mAuthInfoLayout;
    private View mModbusAuthInfoLayout;
    private View mJTextAuthInfoLayout;

    private TextInputLayout mDtuNumberLayout;
    private TextInputLayout mDtuPasswordLayout;
    private TextInputLayout mDeviceModelLayout;
    private TextInputLayout mDeviceIdLayout;
    private AppCompatSpinner mProtocolSpinner;
    private RadioGroup mRadioGroup;
    private boolean mPrivate;

    private String[] mProtocols;
    private String mProtocol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeviceTitleEditText = (TextInputLayout) findViewById(R.id.title);
        mAuthInfoLayout = (TextInputLayout) findViewById(R.id.auth_info_layout);
        mModbusAuthInfoLayout = findViewById(R.id.modbus_auth_info);
        mJTextAuthInfoLayout = findViewById(R.id.jtext_auth_info);
        mDtuNumberLayout = (TextInputLayout) findViewById(R.id.dtu_number_layout);
        mDtuPasswordLayout = (TextInputLayout) findViewById(R.id.dtu_password_layout);
        mDeviceModelLayout = (TextInputLayout) findViewById(R.id.model_layout);
        mDeviceIdLayout = (TextInputLayout) findViewById(R.id.id_layout);
        mProtocolSpinner = (AppCompatSpinner) findViewById(R.id.protocol_spinner);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mProtocols = getResources().getStringArray(R.array.protocols);
        mProtocol = mProtocols[0];

        mProtocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProtocol = mProtocols[position];
                mAuthInfoLayout.setVisibility(position < 3 ? View.VISIBLE : View.GONE);
                mModbusAuthInfoLayout.setVisibility(3 == position ? View.VISIBLE : View.GONE);
                mJTextAuthInfoLayout.setVisibility(4 == position ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        boolean valid = checkInput(mDeviceTitleEditText, R.string.device_title_empty_error);
        if (valid) {
            if (mProtocolSpinner.getSelectedItemPosition() < 3) {
                valid = checkInput(mAuthInfoLayout, R.string.auth_info_empty_error);
            } else if (mProtocolSpinner.getSelectedItemPosition() == 3) {
                valid = checkInput(mDtuNumberLayout, R.string.dtu_serial_number_empty_error);
                if (valid) {
                    valid = checkInput(mDtuPasswordLayout, R.string.dtu_password_empty_error);
                }
            } else if (mProtocolSpinner.getSelectedItemPosition() == 4) {
                valid = checkInput(mDeviceModelLayout, R.string.device_model_empty_drror);
                if (valid) {
                    valid = checkInput(mDeviceIdLayout, R.string.device_id_empty_error);
                }
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
            requestContent.putOpt("title", mDeviceTitleEditText.getEditText().getText().toString());
            requestContent.putOpt("protocol", mProtocol);
            requestContent.putOpt("private", mPrivate);
            if (mProtocolSpinner.getSelectedItemPosition() < 3) {
                requestContent.putOpt("auth_info", mAuthInfoLayout.getEditText().getText().toString());
            } else if (mProtocolSpinner.getSelectedItemPosition() == 3) {
                JSONObject authInfo = new JSONObject();
                authInfo.putOpt(mDtuNumberLayout.getEditText().getText().toString(), mDtuPasswordLayout.getEditText().getText().toString());
                requestContent.putOpt("auth_info", authInfo);
            } else if (mProtocolSpinner.getSelectedItemPosition() == 4) {
                JSONObject activateCode = new JSONObject();
                activateCode.putOpt("mt", mDeviceModelLayout.getEditText().getText().toString());
                activateCode.putOpt("mid", mDeviceIdLayout.getEditText().getText().toString());
                requestContent.putOpt("activate_code", activateCode);
            }
            OneNetApi.addDevice(requestContent.toString(), new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                    int errno = resp.get("errno").getAsInt();
                    if (0 == errno) {
                        Toast.makeText(getApplicationContext(), R.string.added_successfully, Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(AddDeviceActivity.this).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_DEVICE_LIST));
                        finish();
                    } else {
                        String error = resp.get("error").getAsString();
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

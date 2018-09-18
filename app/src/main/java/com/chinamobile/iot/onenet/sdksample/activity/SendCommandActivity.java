package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.Command;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SendCommandActivity extends AppCompatActivity {

    public static final String EXTRA_DEVICE_ITEM = "extra_device_item";
    private Toolbar mToolbar;
    private DeviceItem mDeviceItem;

    private TextInputLayout mCommandContentLayout;
    private TextInputLayout mValidPeriodLayout;
    private RadioGroup mNeedRespGroup;

    public static void actionSendCommand(Context context, DeviceItem deviceItem) {
        Intent intent = new Intent(context, SendCommandActivity.class);
        intent.putExtra(EXTRA_DEVICE_ITEM, deviceItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_command);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_cmd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_send:
                sendCommand();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mDeviceItem = (DeviceItem) getIntent().getSerializableExtra(EXTRA_DEVICE_ITEM);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.send_cmd_activity_title, mDeviceItem.getProtocol().toUpperCase()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        mCommandContentLayout = (TextInputLayout) findViewById(R.id.command_content);
        mValidPeriodLayout = (TextInputLayout) findViewById(R.id.valid_time_layout);
        mNeedRespGroup = (RadioGroup) findViewById(R.id.radio_group_need_resp);

        if (!"EDP".equalsIgnoreCase(mDeviceItem.getProtocol())) {
            findViewById(R.id.need_resp_layout).setVisibility(View.GONE);
            findViewById(R.id.valid_time_layout).setVisibility(View.GONE);
        }
    }

    private void sendCommand() {
        if ("EDP".equalsIgnoreCase(mDeviceItem.getProtocol())) {
            if (mCommandContentLayout.getEditText().getText().toString().trim().length() == 0) {
                mCommandContentLayout.setError(getResources().getString(R.string.command_content));
                mCommandContentLayout.requestFocus();
                return;
            }
            if (mValidPeriodLayout.getEditText().getText().toString().trim().length() == 0) {
                mValidPeriodLayout.setError(getResources().getString(R.string.valid_period));
                mValidPeriodLayout.requestFocus();
                return;
            }
            OneNetApi.sendCmdToDevice(
                    mDeviceItem.getId(),
                    mNeedRespGroup.getCheckedRadioButtonId() == R.id.radio_group_need_resp,
                    Integer.parseInt(mValidPeriodLayout.getEditText().getText().toString()),
                    Command.CommandType.TYPE_CMD_REQ,
                    mCommandContentLayout.getEditText().getText().toString(),
                    new OneNetApiCallback() {
                        @Override
                        public void onSuccess(String response) {
                            JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                            int errno = resp.get("errno").getAsInt();
                            if (0 == errno) {
                                Toast.makeText(getApplicationContext(), R.string.send_cmd_succ, Toast.LENGTH_SHORT).show();
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
        } else {
            if (mCommandContentLayout.getEditText().getText().toString().trim().length() == 0) {
                ((TextInputLayout) mCommandContentLayout.getParent()).setError(getResources().getString(R.string.command_content));
                mCommandContentLayout.requestFocus();
                return;
            }
            OneNetApi.sendCmdToDevice(mDeviceItem.getId(), mCommandContentLayout.getEditText().getText().toString(), new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                    int errno = resp.get("errno").getAsInt();
                    if (0 == errno) {
                        Toast.makeText(getApplicationContext(), R.string.send_cmd_succ, Toast.LENGTH_SHORT).show();
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
        }
    }
}

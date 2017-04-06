package com.chinamobile.iot.onenet.sdksample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DataSimFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText mDeviceIdEditText;
    private TextInputEditText mDataStreamEditText;
    private TextInputEditText mMinValueEditText;
    private TextInputEditText mMaxValueEditText;
    private TextInputEditText mPeriodEditText;
    private Button mSendOneceButton;
    private Button mSendContinuousButton;
    private TextView mResponseLogTextView;

    private Timer mSendDataTimer;
    private boolean mSending;
    private Handler mHandler = new Handler();

    public static DataSimFragment newInstance() {
        return new DataSimFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data_sim, container, false);
        mDeviceIdEditText = (TextInputEditText) v.findViewById(R.id.device_id);
        mDataStreamEditText = (TextInputEditText) v.findViewById(R.id.datastream);
        mMinValueEditText = (TextInputEditText) v.findViewById(R.id.min_value);
        mMaxValueEditText = (TextInputEditText) v.findViewById(R.id.max_value);
        mPeriodEditText = (TextInputEditText) v.findViewById(R.id.period);
        mSendOneceButton = (Button) v.findViewById(R.id.send_onece);
        mSendContinuousButton = (Button) v.findViewById(R.id.send_continuous);
        mResponseLogTextView = (TextView) v.findViewById(R.id.response_log);

        mSendOneceButton.setOnClickListener(this);
        mSendContinuousButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        String deviceId = mDeviceIdEditText.getText().toString();
        String datastream = mDataStreamEditText.getText().toString();
        String minValueString = mMinValueEditText.getText().toString();
        String maxValueString = mMaxValueEditText.getText().toString();
        String periodString = mPeriodEditText.getText().toString();
        if (TextUtils.isEmpty(deviceId)) {
            mDeviceIdEditText.setError(getResources().getString(R.string.device_id));
            mDeviceIdEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(datastream)) {
            mDataStreamEditText.setError(getResources().getString(R.string.datastream));
            mDataStreamEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(minValueString)) {
            mMinValueEditText.setError(getResources().getString(R.string.min_value));
            mMinValueEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(maxValueString)) {
            mMaxValueEditText.setError(getResources().getString(R.string.max_value));
            mMaxValueEditText.requestFocus();
            return;
        }

        float minValue = Float.parseFloat(minValueString);
        float maxValue = Float.parseFloat(maxValueString);

        switch (v.getId()) {
            case R.id.send_onece:
                sendOnece(deviceId, datastream, minValue, maxValue);
                break;

            case R.id.send_continuous:
                if (!mSending) {
                    if (TextUtils.isEmpty(periodString)) {
                        mPeriodEditText.setError(getResources().getString(R.string.time_interval));
                        mPeriodEditText.requestFocus();
                        return;
                    }
                    int period = Integer.parseInt(periodString);
                    sendContinuous(deviceId, datastream, minValue, maxValue, period);
                } else {
                    mDeviceIdEditText.setEnabled(true);
                    mDataStreamEditText.setEnabled(true);
                    mMinValueEditText.setEnabled(true);
                    mMaxValueEditText.setEnabled(true);
                    mPeriodEditText.setEnabled(true);
                    mSendOneceButton.setEnabled(true);
                    mSendContinuousButton.setText(R.string.send_continuous);
                    if (mSendDataTimer != null) {
                        mSendDataTimer.cancel();
                    }
                    mSending = false;
                }
                break;
        }
    }

    private void sendOnece(String deviceId, String datastream, float minValue, float maxValue) {
        float value = (float) (Math.random() * (maxValue - minValue) + minValue);
        JSONObject request = new JSONObject();
        try {
            request.putOpt(datastream, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneNetApi.addDataPoints(deviceId, "3", request.toString(), new OneNetApiCallback() {
            @Override
            public void onSuccess(int errno, String error, String data) {
                displayLog(errno, error, data);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendContinuous(final String deviceId, final String datastream, final float minValue, final float maxValue, int period) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendOnece(deviceId, datastream, minValue, maxValue);
                    }
                });
            }
        };
        mSendDataTimer = new Timer(true);
        mSendDataTimer.schedule(task, 0, period * 1000);
        mSendContinuousButton.setText(R.string.stop_sending);
        mDeviceIdEditText.setEnabled(false);
        mDataStreamEditText.setEnabled(false);
        mMinValueEditText.setEnabled(false);
        mMaxValueEditText.setEnabled(false);
        mPeriodEditText.setEnabled(false);
        mSendOneceButton.setEnabled(false);
        mSending = true;
    }

    private void displayLog(int errno, String error, String data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject log = new JsonObject();
        log.addProperty("errno", errno);
        log.addProperty("error", error);
        if (!TextUtils.isEmpty(data)) {
            log.addProperty("data", data);
        }
        mResponseLogTextView.setText(gson.toJson(log));
    }
}

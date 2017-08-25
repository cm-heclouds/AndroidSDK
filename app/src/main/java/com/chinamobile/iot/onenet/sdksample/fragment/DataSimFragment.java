package com.chinamobile.iot.onenet.sdksample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 数据模拟器
 */
public class DataSimFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout mDeviceIdLayout;
    private TextInputLayout mDataStreamLayout;
    private TextInputLayout mMinValueLayout;
    private TextInputLayout mMaxValueLayout;
    private TextInputLayout mPeriodLayout;
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
        mDeviceIdLayout = (TextInputLayout) v.findViewById(R.id.device_id);
        mDataStreamLayout = (TextInputLayout) v.findViewById(R.id.datastream);
        mMinValueLayout = (TextInputLayout) v.findViewById(R.id.min_value);
        mMaxValueLayout = (TextInputLayout) v.findViewById(R.id.max_value);
        mPeriodLayout = (TextInputLayout) v.findViewById(R.id.period);
        mSendOneceButton = (Button) v.findViewById(R.id.send_onece);
        mSendContinuousButton = (Button) v.findViewById(R.id.send_continuous);
        mResponseLogTextView = (TextView) v.findViewById(R.id.response_log);

        mSendOneceButton.setOnClickListener(this);
        mSendContinuousButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        String deviceId = mDeviceIdLayout.getEditText().getText().toString();
        String datastream = mDataStreamLayout.getEditText().getText().toString();
        String minValueString = mMinValueLayout.getEditText().getText().toString();
        String maxValueString = mMaxValueLayout.getEditText().getText().toString();
        String periodString = mPeriodLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(deviceId)) {
            mDeviceIdLayout.setError(getResources().getString(R.string.device_id));
            mDeviceIdLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(datastream)) {
            mDataStreamLayout.setError(getResources().getString(R.string.datastream));
            mDataStreamLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(minValueString)) {
            mMinValueLayout.setError(getResources().getString(R.string.min_value));
            mMinValueLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(maxValueString)) {
            mMaxValueLayout.setError(getResources().getString(R.string.max_value));
            mMaxValueLayout.requestFocus();
            return;
        }

        float minValue = Float.parseFloat(minValueString);
        float maxValue = Float.parseFloat(maxValueString);

        switch (v.getId()) {
            case R.id.send_onece:
                sendOnce(deviceId, datastream, minValue, maxValue);
                break;

            case R.id.send_continuous:
                if (!mSending) {
                    if (TextUtils.isEmpty(periodString)) {
                        mPeriodLayout.setError(getResources().getString(R.string.time_interval));
                        mPeriodLayout.requestFocus();
                        return;
                    }
                    int period = Integer.parseInt(periodString);
                    sendContinuous(deviceId, datastream, minValue, maxValue, period);
                } else {
                    mDeviceIdLayout.setEnabled(true);
                    mDataStreamLayout.setEnabled(true);
                    mMinValueLayout.setEnabled(true);
                    mMaxValueLayout.setEnabled(true);
                    mPeriodLayout.setEnabled(true);
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

    private void sendOnce(String deviceId, String datastream, float minValue, float maxValue) {
        float value = (float) (Math.random() * (maxValue - minValue) + minValue);
        JSONObject request = new JSONObject();
        try {
            request.putOpt(datastream, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneNetApi.addDataPoints(deviceId, "3", request.toString(), new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                displayLog(response);
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
                        sendOnce(deviceId, datastream, minValue, maxValue);
                    }
                });
            }
        };
        mSendDataTimer = new Timer(true);
        mSendDataTimer.schedule(task, 0, period * 1000);
        mSendContinuousButton.setText(R.string.stop_sending);
        mDeviceIdLayout.setEnabled(false);
        mDataStreamLayout.setEnabled(false);
        mMinValueLayout.setEnabled(false);
        mMaxValueLayout.setEnabled(false);
        mPeriodLayout.setEnabled(false);
        mSendOneceButton.setEnabled(false);
        mSending = true;
    }

    private void displayLog(String response) {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
        }
        mResponseLogTextView.setText(response);
    }
}

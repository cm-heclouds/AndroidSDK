package com.chinamobile.iot.onenet.sdksample.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.activity.DisplayApiRespActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiTestFragment extends Fragment implements View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private LayoutInflater mInflater;

    public static ApiTestFragment newInstance() {
        return new ApiTestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_api_test, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Sending request...");

        v.findViewById(R.id.add_device).setOnClickListener(this);
        v.findViewById(R.id.update_device).setOnClickListener(this);
        v.findViewById(R.id.query_single_device).setOnClickListener(this);
        v.findViewById(R.id.fuzzy_query_devices).setOnClickListener(this);
        v.findViewById(R.id.delete_device).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        mProgressDialog.show();
        switch (v.getId()) {
            case R.id.add_device:
                addDevice();
                break;

            case R.id.update_device:
                showUpdateDeviceDialog();
                break;

            case R.id.query_single_device:
                showQuerySingleDeviceDialog();
                break;

            case R.id.fuzzy_query_devices:
                fuzzyQueryDevices();
                break;

            case R.id.delete_device:
                showDeleteDeviceDialog();
                break;

            case R.id.add_datastream:
                break;

            case R.id.update_datastream:
                break;

            case R.id.query_single_datastream:
                break;

            case R.id.query_multi_datastreams:
                break;

            case R.id.delete_datastream:
                break;

            case R.id.add_datapoint:
                break;

            case R.id.query_datapoints:
                break;

            case R.id.add_trigger:
                break;

            case R.id.update_trigger:
                break;

            case R.id.query_single_trigger:
                break;

            case R.id.fuzzy_query_triggers:
                break;

            case R.id.delete_trigger:
                break;

            case R.id.send_cmd:
                break;

            case R.id.query_cmd_status:
                break;

            case R.id.query_cmd_resp:
                break;

        }
    }

    private void displayLog(int errno, String error, String data) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JSONObject log = new JSONObject();
            log.putOpt("errno", errno);
            log.putOpt("error", error);
            if (!TextUtils.isEmpty(data)) {
                JSONObject dataObj = new JSONObject(data);
                log.putOpt("data", dataObj);
            }
            JsonParser jsonParser = new JsonParser();
            DisplayApiRespActivity.actionDisplayApiResp(getActivity(), gson.toJson(jsonParser.parse(log.toString())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class Callback implements OneNetApiCallback {
        @Override
        public void onSuccess(int errno, String error, String data) {
            displayLog(errno, error, data);
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailed(Exception e) {
            DisplayApiRespActivity.actionDisplayApiResp(getActivity(), e.toString());
            mProgressDialog.dismiss();
        }
    }

    private void showUpdateDeviceDialog() {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_id, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId)) {
                            updateDevice(deviceId);
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showQuerySingleDeviceDialog() {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_id, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId)) {
                            querySingleDevice(deviceId);
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showDeleteDeviceDialog() {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_id, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId)) {
                            deleteDevice(deviceId);
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void addDevice() {
        JsonObject object = new JsonObject();
        object.addProperty("title", "Test Add Device");
        object.addProperty("protocol", "EDP");
        object.addProperty("auth_info", "test_auth_info");
        OneNetApi.addDevice(object.toString(), new Callback());
    }

    private void updateDevice(String deviceId) {
        JsonObject object = new JsonObject();
        object.addProperty("title", "Test Update Device");
        OneNetApi.updateDevice(deviceId, object.toString(), new Callback());
    }

    private void querySingleDevice(String deviceId) {
        OneNetApi.querySingleDevice(deviceId, new Callback());
    }

    private void fuzzyQueryDevices() {
        OneNetApi.fuzzyQueryDevices(null, new Callback());
    }

    private void deleteDevice(String deviceId) {
        OneNetApi.deleteDevice(deviceId, new Callback());
    }

    private void addDatastream(String deviceId) {

    }

    private void updateDatastream(String deviceId, String datastreamId) {

    }

    private void querySingleDatastream(String deviceId, String datastreamId) {

    }

    private void queryMultiDatastreams(String deviceId) {

    }

    private void deleteDatastram(String deviceId, String datastreamId) {

    }
}

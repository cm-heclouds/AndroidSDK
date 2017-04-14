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
        v.findViewById(R.id.add_datastream).setOnClickListener(this);
        v.findViewById(R.id.update_datastream).setOnClickListener(this);
        v.findViewById(R.id.query_single_datastream).setOnClickListener(this);
        v.findViewById(R.id.query_multi_datastreams).setOnClickListener(this);
        v.findViewById(R.id.delete_datastream).setOnClickListener(this);
        v.findViewById(R.id.add_datapoint).setOnClickListener(this);
        v.findViewById(R.id.query_datapoints).setOnClickListener(this);
        v.findViewById(R.id.add_trigger).setOnClickListener(this);
        v.findViewById(R.id.update_trigger).setOnClickListener(this);
        v.findViewById(R.id.query_single_trigger).setOnClickListener(this);
        v.findViewById(R.id.fuzzy_query_triggers).setOnClickListener(this);
        v.findViewById(R.id.delete_trigger).setOnClickListener(this);
        v.findViewById(R.id.add_binary_data).setOnClickListener(this);
        v.findViewById(R.id.query_binary_data).setOnClickListener(this);
        v.findViewById(R.id.send_cmd).setOnClickListener(this);
        v.findViewById(R.id.query_cmd_status).setOnClickListener(this);
        v.findViewById(R.id.query_cmd_resp).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_device:
                mAddDeviceFunction.apply();
                break;

            case R.id.update_device:
                showInputDeviceIdDialog(mUpdateDeviceFunction);
                break;

            case R.id.query_single_device:
                showInputDeviceIdDialog(mQuerySingleDeviceFunction);
                break;

            case R.id.fuzzy_query_devices:
                mFuzzyQueryDevicesFunction.apply();
                break;

            case R.id.delete_device:
                showInputDeviceIdDialog(mDeleteDeviceFunction);
                break;

            case R.id.add_datastream:
                showInputDataStreamIdDialog(mAddDataStreamFunction);
                break;

            case R.id.update_datastream:
                showInputDataStreamIdDialog(mUpdateDataStreamFunction);
                break;

            case R.id.query_single_datastream:
                showInputDataStreamIdDialog(mQuerySingleDatastreamFunction);
                break;

            case R.id.query_multi_datastreams:
                showInputDeviceIdDialog(mQueryMultiDataStreamFunction);
                break;

            case R.id.delete_datastream:
                showInputDataStreamIdDialog(mDeleteDataStreamFunction);
                break;

            case R.id.add_datapoint:
                showInputDeviceIdDialog(mAddDataPointFunction);
                break;

            case R.id.query_datapoints:
                showInputDeviceIdDialog(mQueryDataPointsFunction);
                break;

            case R.id.add_trigger:
                mAddTriggerFunction.apply();
                break;

            case R.id.update_trigger:
                showInputTriggerIdDialog(mUpdateTriggerFunction);
                break;

            case R.id.query_single_trigger:
                showInputTriggerIdDialog(mQuerySingleTriggerFunction);
                break;

            case R.id.fuzzy_query_triggers:
                mFuzzyQueryTriggersFunction.apply();
                break;

            case R.id.delete_trigger:
                showInputTriggerIdDialog(mDeleteTriggerFunction);
                break;

            case R.id.add_binary_data:
                showInputDataStreamIdDialog(mAddBinDataFunction);
                break;

            case R.id.query_binary_data:
                showInputBinaryIndexDialog(mQueryBinDataFunction);
                break;

            case R.id.send_cmd:
                showInputDeviceIdDialog(mSendCmdFunction);
                break;

            case R.id.query_cmd_status:
                showInputCmdUUIDDialog(mQueryCmdStatusFunction);
                break;

            case R.id.query_cmd_resp:
                showInputCmdUUIDDialog(mQueryCmdRespFunction);
                break;

        }
    }

    private void displayLog(String response) {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
        }
        DisplayApiRespActivity.actionDisplayApiResp(getActivity(), response);
    }

    private class Callback implements OneNetApiCallback {
        @Override
        public void onSuccess(String response) {
            displayLog(response);
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailed(Exception e) {
            DisplayApiRespActivity.actionDisplayApiResp(getActivity(), e.toString());
            mProgressDialog.dismiss();
        }
    }

    private void showInputDeviceIdDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_id, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId)) {
                            function.apply(deviceId);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputDataStreamIdDialog(final Function2<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_and_datastream_id, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        final TextInputEditText dataStreamIdEditText = (TextInputEditText) contentView.findViewById(R.id.datastream_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        String dataStreamId = dataStreamIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(dataStreamId)) {
                            function.apply(deviceId, dataStreamId);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputTriggerIdDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_trigger_id, null);
        final TextInputEditText triggerIdEditText = (TextInputEditText) contentView.findViewById(R.id.trigger_id);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String triggerId = triggerIdEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(triggerId)) {
                            function.apply(triggerId);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputBinaryIndexDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_binary_index, null);
        final TextInputEditText binaryIndexEditText = (TextInputEditText) contentView.findViewById(R.id.binary_index);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String binaryIndex = binaryIndexEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(binaryIndex)) {
                            function.apply(binaryIndex);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputCmdUUIDDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_cmd_uuid, null);
        final TextInputEditText binaryIndexEditText = (TextInputEditText) contentView.findViewById(R.id.cmd_uuid);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cmdUUID = binaryIndexEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(cmdUUID)) {
                            function.apply(cmdUUID);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    interface Function {
        void apply();
    }

    interface Function1<T> {
        void apply(T t);
    }

    interface Function2<T> {
        void apply(T t1, T t2);
    }

    private Function mAddDeviceFunction = new Function() {
        @Override
        public void apply() {
            JsonObject object = new JsonObject();
            object.addProperty("title", "Test Add Device");
            object.addProperty("protocol", "EDP");
            object.addProperty("auth_info", "test_auth_info");
            OneNetApi.addDevice(object.toString(), new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mUpdateDeviceFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            JsonObject object = new JsonObject();
            object.addProperty("title", "Test Update Device");
            OneNetApi.updateDevice(deviceId, object.toString(), new Callback());
        }
    };

    private Function1<String> mQuerySingleDeviceFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.querySingleDevice(deviceId, new Callback());
        }
    };

    private Function mFuzzyQueryDevicesFunction = new Function() {
        @Override
        public void apply() {
            OneNetApi.fuzzyQueryDevices(null, new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mDeleteDeviceFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.deleteDevice(deviceId, new Callback());
        }
    };

    private Function2<String> mAddDataStreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            JsonObject object = new JsonObject();
            object.addProperty("id", dataStreamId);
            object.addProperty("unit", "TestUnit");
            object.addProperty("unit_symbol", "TestSymbol");
            OneNetApi.addDataStream(deviceId, object.toString(), new Callback());
        }
    };

    private Function2<String> mUpdateDataStreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            JsonObject object = new JsonObject();
            object.addProperty("unit", "TestUpdateUnit");
            object.addProperty("unit_symbol", "TestUpdateSymbol");
            OneNetApi.updateDataStream(deviceId, dataStreamId, object.toString(), new Callback());
        }
    };

    private Function2<String> mQuerySingleDatastreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new Callback());
        }
    };

    private Function1<String> mQueryMultiDataStreamFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryMultiDataStreams(deviceId, new Callback());
        }
    };

    private Function2<String> mDeleteDataStreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.deleteDatastream(deviceId, dataStreamId, new Callback());
        }
    };

    private Function1<String> mAddDataPointFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            JsonObject object = new JsonObject();
            object.addProperty("TestDataStream", Math.random() * 100);
            OneNetApi.addDataPoints(deviceId, "3", object.toString(), new Callback());
        }
    };

    private Function1<String> mQueryDataPointsFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryDataPoints(deviceId, null, new Callback());
        }
    };

    private Function mAddTriggerFunction = new Function() {
        @Override
        public void apply() {
            JsonObject object = new JsonObject();
            object.addProperty("title", "TestTrigger");
            object.addProperty("ds_id", "TestDataStream");
            object.addProperty("url", "http://www.abc.com");
            object.addProperty("type", ">");
            object.addProperty("threshold", 100);
            OneNetApi.addTrigger(object.toString(), new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mUpdateTriggerFunction = new Function1<String>() {
        @Override
        public void apply(String triggerId) {
            JsonObject object = new JsonObject();
            object.addProperty("title", "TestUpdateTrigger");
            object.addProperty("ds_id", "TestDataStream");
            OneNetApi.updateTrigger(triggerId, object.toString(), new Callback());
        }
    };

    private Function1<String> mQuerySingleTriggerFunction = new Function1<String>() {
        @Override
        public void apply(String triggerId) {
            OneNetApi.querySingleTrigger(triggerId, new Callback());
        }
    };

    private Function mFuzzyQueryTriggersFunction = new Function() {
        @Override
        public void apply() {
            OneNetApi.fuzzyQueryTriggers(new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mDeleteTriggerFunction = new Function1<String>() {
        @Override
        public void apply(String triggerId) {
            OneNetApi.deleteTrigger(triggerId, new Callback());
        }
    };

    private Function2<String> mAddBinDataFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.addBinaryData(deviceId, dataStreamId, "Test Add Binary Data", new Callback());
        }
    };

    private Function1<String> mQueryBinDataFunction = new Function1<String>() {
        @Override
        public void apply(String index) {
            OneNetApi.queryBinaryData(index, new Callback());
        }
    };

    private Function1<String> mSendCmdFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.sendCmdToDevice(deviceId, "Test Send Command", new Callback());
        }
    };

    private Function1<String> mQueryCmdStatusFunction = new Function1<String>() {
        @Override
        public void apply(String cmdUUID) {
            OneNetApi.queryCmdStatus(cmdUUID, new Callback());
        }
    };

    private Function1<String> mQueryCmdRespFunction = new Function1<String>() {
        @Override
        public void apply(String cmdUUID) {
            OneNetApi.queryCmdResponse(cmdUUID, new Callback());
        }
    };
}

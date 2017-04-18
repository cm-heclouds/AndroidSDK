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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.activity.DisplayApiRespActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * API测试用例
 */
public class ApiTestFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ProgressDialog mProgressDialog;
    private LayoutInflater mInflater;
    private ExpandableListView mExpandableListView;

    private List<String> mGroupTitleList = new ArrayList<>();
    private List<List<String>> mChildTitleList = new ArrayList<>();

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

        mExpandableListView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
        mExpandableListView.setGroupIndicator(null);
        initData();
        mExpandableListView.setOnChildClickListener(this);
        return v;
    }

    private void initData() {
        mGroupTitleList.add(getResources().getString(R.string.device));
        mGroupTitleList.add(getResources().getString(R.string.datastream));
        mGroupTitleList.add(getResources().getString(R.string.datapoint));
        mGroupTitleList.add(getResources().getString(R.string.trigger));
        mGroupTitleList.add(getResources().getString(R.string.binary_data));
        mGroupTitleList.add(getResources().getString(R.string.command));
        mGroupTitleList.add("MQTT");
        mGroupTitleList.add("API key");

        List<String> childDataDevice = new ArrayList<>();
        childDataDevice.add(getResources().getString(R.string.add_device));
        childDataDevice.add(getResources().getString(R.string.edit_device));
        childDataDevice.add(getResources().getString(R.string.query_single_device));
        childDataDevice.add(getResources().getString(R.string.fuzzy_query_devices));
        childDataDevice.add(getResources().getString(R.string.delete_device));
        mChildTitleList.add(childDataDevice);

        List<String> childDataDataStream = new ArrayList<>();
        childDataDataStream.add(getResources().getString(R.string.add_datastream));
        childDataDataStream.add(getResources().getString(R.string.update_datastream));
        childDataDataStream.add(getResources().getString(R.string.query_single_datastream));
        childDataDataStream.add(getResources().getString(R.string.query_multi_datastreams));
        childDataDataStream.add(getResources().getString(R.string.delete_datastream));
        mChildTitleList.add(childDataDataStream);

        List<String> childDataDataPoint = new ArrayList<>();
        childDataDataPoint.add(getResources().getString(R.string.add_datapoint));
        childDataDataPoint.add(getResources().getString(R.string.query_datapoints));
        mChildTitleList.add(childDataDataPoint);

        List<String> childDataTrigger = new ArrayList<>();
        childDataTrigger.add(getResources().getString(R.string.add_trigger));
        childDataTrigger.add(getResources().getString(R.string.update_trigger));
        childDataTrigger.add(getResources().getString(R.string.query_single_trigger));
        childDataTrigger.add(getResources().getString(R.string.fuzzy_query_triggers));
        childDataTrigger.add(getResources().getString(R.string.delete_trigger));
        mChildTitleList.add(childDataTrigger);

        List<String> childDataBinaryData = new ArrayList<>();
        childDataBinaryData.add(getResources().getString(R.string.add_binary_data));
        childDataBinaryData.add(getResources().getString(R.string.query_binary_data));
        mChildTitleList.add(childDataBinaryData);

        List<String> childDataCommand = new ArrayList<>();
        childDataCommand.add(getResources().getString(R.string.send_command));
        childDataCommand.add(getResources().getString(R.string.query_cmd_status));
        childDataCommand.add(getResources().getString(R.string.query_cmd_resp));
        mChildTitleList.add(childDataCommand);

        List<String> childDataMqtt = new ArrayList<>();
        childDataMqtt.add(getResources().getString(R.string.send_cmd_by_topic));
        childDataMqtt.add(getResources().getString(R.string.query_devices_by_topic));
        childDataMqtt.add(getResources().getString(R.string.query_device_topics));
        childDataMqtt.add(getResources().getString(R.string.add_product_topic));
        childDataMqtt.add(getResources().getString(R.string.delete_product_topic));
        childDataMqtt.add(getResources().getString(R.string.query_product_topics));
        mChildTitleList.add(childDataMqtt);

        List<String> childDataApiKey = new ArrayList<>();
        childDataApiKey.add(getResources().getString(R.string.add_api_key));
        childDataApiKey.add(getResources().getString(R.string.update_api_key));
        childDataApiKey.add(getResources().getString(R.string.query_api_keys));
        childDataApiKey.add(getResources().getString(R.string.delete_api_key));
        mChildTitleList.add(childDataApiKey);

        mExpandableListView.setAdapter(new ExpandableListAdapter());
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        switch (groupPosition) {
            case 0:
                onDeviceChildClick(childPosition);
                break;
            case 1:
                onDataStreamChildClick(childPosition);
                break;
            case 2:
                onDataPointChildClick(childPosition);
                break;
            case 3:
                onTriggerChildClick(childPosition);
                break;
            case 4:
                onBinaryDataChildClick(childPosition);
                break;
            case 5:
                onCommandChildClick(childPosition);
                break;
            case 6:
                onMqttChildClick(childPosition);
                break;
            case 7:
                onApiKeyChildClick(childPosition);
                break;
        }
        return false;
    }

    private void onDeviceChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                mAddDeviceFunction.apply();
                break;
            case 1:
                showInputDeviceIdDialog(mUpdateDeviceFunction);
                break;
            case 2:
                showInputDeviceIdDialog(mQuerySingleDeviceFunction);
                break;
            case 3:
                mFuzzyQueryDevicesFunction.apply();
                break;
            case 4:
                showInputDeviceIdDialog(mDeleteDeviceFunction);
                break;
        }
    }

    private void onDataStreamChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputDataStreamIdDialog(mAddDataStreamFunction);
                break;
            case 1:
                showInputDataStreamIdDialog(mUpdateDataStreamFunction);
                break;
            case 2:
                showInputDataStreamIdDialog(mQuerySingleDatastreamFunction);
                break;
            case 3:
                showInputDeviceIdDialog(mQueryMultiDataStreamFunction);
                break;
            case 4:
                showInputDataStreamIdDialog(mDeleteDataStreamFunction);
                break;
        }
    }

    private void onDataPointChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputDeviceIdDialog(mAddDataPointFunction);
                break;
            case 1:
                showInputDeviceIdDialog(mQueryDataPointsFunction);
                break;
        }
    }

    private void onTriggerChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                mAddTriggerFunction.apply();
                break;
            case 1:
                showInputTriggerIdDialog(mUpdateTriggerFunction);
                break;
            case 2:
                showInputTriggerIdDialog(mQuerySingleTriggerFunction);
                break;
            case 3:
                mFuzzyQueryTriggersFunction.apply();
                break;
            case 4:
                showInputTriggerIdDialog(mDeleteTriggerFunction);
                break;
        }
    }

    private void onBinaryDataChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputDataStreamIdDialog(mAddBinDataFunction);
                break;
            case 1:
                showInputBinaryIndexDialog(mQueryBinDataFunction);
                break;
        }
    }

    private void onCommandChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputDeviceIdDialog(mSendCmdFunction);
                break;
            case 1:
                showInputCmdUUIDDialog(mQueryCmdStatusFunction);
                break;
            case 2:
                showInputCmdUUIDDialog(mQueryCmdRespFunction);
                break;
        }
    }

    private void onMqttChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputTopicDialog(mSendCmdByTopicFunction);
                break;
            case 1:
                showInputTopicDialog(mQueryDevicesByTopicFunction);
                break;
            case 2:
                showInputDeviceIdDialog(mQueryDeviceTopicsFunction);
                break;
            case 3:
                mAddProductTopicFunction.apply();
                break;
            case 4:
                showInputTopicDialog(mDeleteProductTopicFunction);
                break;
            case 5:
                mQueryProductTopicsFunction.apply();
                break;
        }
    }

    private void onApiKeyChildClick(int childPosition) {
        switch (childPosition) {
            case 0:
                showInputDeviceIdDialog(mAddApiKeyFunction);
                break;
            case 1:
                showInputDeviceIdAndApiKeyDialog(mUpdateApiKeyFunction);
                break;
            case 2:
                showInputDeviceIdDialog(mQueryApiKeyFunction);
                break;
            case 3:
                showInputApiKeyDialog(mDeleteApiKeyFunction);
                break;
        }
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mGroupTitleList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildTitleList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupTitleList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChildTitleList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 10 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolderGroup holder;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.api_test_list_group, parent, false);
                holder = new ViewHolderGroup();
                holder.groupTitleView = (TextView) convertView.findViewById(R.id.group_title);
                holder.expandMoreArrow = (ImageView) convertView.findViewById(R.id.expand_more_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderGroup) convertView.getTag();
            }
            String title = mGroupTitleList.get(groupPosition);
            holder.groupTitleView.setText(title);
            holder.expandMoreArrow.setImageResource(isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_navigate_next);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderChild holder;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.api_test_list_child, parent, false);
                holder = new ViewHolderChild();
                holder.childTitleView = (TextView) convertView.findViewById(R.id.child_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderChild) convertView.getTag();
            }
            String title = mChildTitleList.get(groupPosition).get(childPosition);
            holder.childTitleView.setText(title);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class ViewHolderGroup {
        TextView groupTitleView;
        ImageView expandMoreArrow;
    }

    class ViewHolderChild {
        TextView childTitleView;
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

    private void showInputTopicDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_topic, null);
        final TextInputEditText topicEditText = (TextInputEditText) contentView.findViewById(R.id.topic);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String topic = topicEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(topic)) {
                            function.apply(topic);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputDeviceIdAndApiKeyDialog(final Function2<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_device_id_and_apikey, null);
        final TextInputEditText deviceIdEditText = (TextInputEditText) contentView.findViewById(R.id.device_id);
        final TextInputEditText apiKeyEditText = (TextInputEditText) contentView.findViewById(R.id.apikey);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceId = deviceIdEditText.getText().toString().trim();
                        String apikey = apiKeyEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(apikey)) {
                            function.apply(deviceId, apikey);
                            mProgressDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showInputApiKeyDialog(final Function1<String> function) {
        final View contentView = mInflater.inflate(R.layout.dialog_input_apikey, null);
        final TextInputEditText apiKeyEditText = (TextInputEditText) contentView.findViewById(R.id.apikey);
        new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String apikey = apiKeyEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(apikey)) {
                            function.apply(apikey);
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

    private Function1<String> mSendCmdByTopicFunction = new Function1<String>() {
        @Override
        public void apply(String topic) {
            OneNetApi.sendCmdByTopic(topic, "TestCommand", new Callback());
        }
    };

    private Function1<String> mQueryDevicesByTopicFunction = new Function1<String>() {
        @Override
        public void apply(String topic) {
            OneNetApi.queryDevicesByTopic(topic, 1, 10, new Callback());
        }
    };

    private Function1<String> mQueryDeviceTopicsFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryDeviceTopics(deviceId, new Callback());
        }
    };

    private Function mAddProductTopicFunction = new Function() {
        @Override
        public void apply() {
            JsonObject object = new JsonObject();
            object.addProperty("name", "TestAddTopic");
            OneNetApi.addTopic(object.toString(), new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mDeleteProductTopicFunction = new Function1<String>() {
        @Override
        public void apply(String topic) {
            OneNetApi.deleteTopic(topic, new Callback());
        }
    };

    private Function mQueryProductTopicsFunction = new Function() {
        @Override
        public void apply() {
            OneNetApi.queryTopics(new Callback());
            mProgressDialog.show();
        }
    };

    private Function1<String> mAddApiKeyFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            JsonObject resource = new JsonObject();
            resource.addProperty("dev_id", deviceId);

            JsonArray resources = new JsonArray();
            resources.add(resource);

            JsonArray accessMethods = new JsonArray();
            accessMethods.add("get");
            accessMethods.add("post");

            JsonObject permission = new JsonObject();
            permission.add("resources", resources);
            permission.add("access_methods", accessMethods);

            JsonArray permisstions = new JsonArray();
            permisstions.add(permission);

            JsonObject object = new JsonObject();
            object.addProperty("title", "TestApiKey");
            object.add("permissions", permisstions);

            OneNetApi.addApiKey(object.toString(), new Callback());
        }
    };

    private Function2<String> mUpdateApiKeyFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String apikey) {
            JsonObject resource = new JsonObject();
            resource.addProperty("dev_id", deviceId);

            JsonArray resources = new JsonArray();
            resources.add(resource);

            JsonArray accessMethods = new JsonArray();
            accessMethods.add("get");
            accessMethods.add("post");

            JsonObject permission = new JsonObject();
            permission.add("resources", resources);
            permission.add("access_methods", accessMethods);

            JsonArray permisstions = new JsonArray();
            permisstions.add(permission);

            JsonObject object = new JsonObject();
            object.addProperty("title", "TestUpdateApiKey");
            object.add("permissions", permisstions);

            OneNetApi.updateApiKey(apikey, object.toString(), new Callback());
        }
    };

    private Function1<String> mQueryApiKeyFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryApiKey(null, 1, 10, deviceId, new Callback());
        }
    };

    private Function1<String> mDeleteApiKeyFunction = new Function1<String>() {
        @Override
        public void apply(String apikey) {
            OneNetApi.deleteApiKey(apikey, new Callback());
        }
    };

}

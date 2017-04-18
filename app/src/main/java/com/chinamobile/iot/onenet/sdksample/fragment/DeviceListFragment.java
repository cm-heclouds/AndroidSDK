package com.chinamobile.iot.onenet.sdksample.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.activity.AddDeviceActivity;
import com.chinamobile.iot.onenet.sdksample.activity.DeviceActivity;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;
import com.chinamobile.iot.onenet.sdksample.utils.DeviceItemDeserializer;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DeviceListAdapter mAdapter;
    private int mCurrentPage = 1;
    private int mTotalCount;
    private List<DeviceItem> mDeviceItems = new ArrayList<>();
    private FloatingActionButton mFabAddDevice;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_device_list, container, false);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recyler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        mFabAddDevice = (FloatingActionButton) contentView.findViewById(R.id.fab_add_device);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DeviceListAdapter();
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceActivity.actionDevice(getContext(), mDeviceItems.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(0xFFDA4336);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getDevices(false);
        mSwipeRefreshLayout.setRefreshing(true);

        mFabAddDevice.setOnClickListener(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateApiKeyReceiver, new IntentFilter(IntentActions.ACTION_UPDATE_APIKEY));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateDeviceListReceiver, new IntentFilter(IntentActions.ACTION_UPDATE_DEVICE_LIST));
        return contentView;
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateApiKeyReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateDeviceListReceiver);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        getDevices(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_device:
                startActivity(new Intent(getActivity(), AddDeviceActivity.class));
                break;
        }
    }

    class DeviceListAdapter extends BaseQuickAdapter<DeviceItem, BaseViewHolder> {

        public DeviceListAdapter() {
            super(R.layout.device_list_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceItem item) {
            //helper.addOnClickListener()
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.id, getResources().getString(R.string.device_id) + "：" + item.getId());
            helper.setText(R.id.protocol, getResources().getString(R.string.device_access_protocol) + "：" + item.getProtocol());
            helper.setText(R.id.create_time, getResources().getString(R.string.create_time) + "：" + item.getCreateTime());
            if ("HTTP".equalsIgnoreCase(item.getProtocol())) {
                helper.setVisible(R.id.online, false);
            } else {
                helper.setVisible(R.id.online, true);
                helper.setText(R.id.online, item.isOnline() ? R.string.online : R.string.offline);
            }
        }
    }

    private void getDevices(final boolean loadMore) {
        if (loadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
        }
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("page", String.valueOf(mCurrentPage));
        urlParams.put("per_page", "10");
        OneNetApi.fuzzyQueryDevices(urlParams, new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    parseData(resp.get("data").getAsJsonObject(), loadMore);
                } else {
                    String error = resp.get("error").getAsString();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void parseData(JsonObject data, boolean loadMore) {
        if (null == data) {
            return;
        }
        mTotalCount = data.get("total_count").getAsInt();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DeviceItem.class, new DeviceItemDeserializer());
        Gson gson = gsonBuilder.create();
        JsonArray jsonArray = data.get("devices").getAsJsonArray();
        List<DeviceItem> devices = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            devices.add(gson.fromJson(element, DeviceItem.class));
        }
        if (!loadMore) {
            mDeviceItems.clear();
        }
        mDeviceItems.addAll(devices);
        mAdapter.setNewData(mDeviceItems);
    }

    class Data {
        private List<DeviceItem> devices;

        public List<DeviceItem> getDevices() {
            return devices;
        }

        public void setDevices(List<DeviceItem> devices) {
            this.devices = devices;
        }
    }

    private BroadcastReceiver mUpdateApiKeyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSwipeRefreshLayout.setRefreshing(true);
            getDevices(false);
        }
    };

    private BroadcastReceiver mUpdateDeviceListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSwipeRefreshLayout.setRefreshing(true);
            getDevices(false);
        }
    };

    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            if (mDeviceItems.size() >= mTotalCount) {
                mAdapter.loadMoreEnd();
            } else {
                getDevices(true);
            }
        }
    };
}

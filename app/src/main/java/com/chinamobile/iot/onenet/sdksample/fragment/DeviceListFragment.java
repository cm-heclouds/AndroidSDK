package com.chinamobile.iot.onenet.sdksample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.activity.AddDeviceActivity;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DeviceListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(0xFFDA4336);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getDevices(false);
        mSwipeRefreshLayout.setRefreshing(true);

        mFabAddDevice.setOnClickListener(this);
        return contentView;
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
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.id, "设备ID：" + item.getId());
            helper.setText(R.id.protocol, "设备接入协议：" + item.getProtocol());
            helper.setText(R.id.create_time, "创建时间：" + item.getCreateTime());
            if ("HTTP".equalsIgnoreCase(item.getProtocol())) {
                helper.setVisible(R.id.online, false);
            } else {
                helper.setVisible(R.id.online, true);
                helper.setText(R.id.online, item.isOnline() ? "在线" : "离线");
            }
        }
    }

    private void getDevices(boolean loadMore) {
        if (loadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
            mDeviceItems.clear();
        }
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("page", String.valueOf(mCurrentPage));
        urlParams.put("per_page", "10");
        OneNetApi.fuzzyQueryDevices(urlParams, new OneNetApiCallback() {
            @Override
            public void onSuccess(int errno, String error, String data) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (0 == errno) {
                    parseData(data);
                } else {
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

    private void parseData(String data) {
        if (null == data) {
            return;
        }
        try {
            JSONObject dataObj = new JSONObject(data);
            mTotalCount = dataObj.optInt("total_count");
            Gson gson = new Gson();
            List<DeviceItem> devices = gson.fromJson(data, Data.class).getDevices();
            mDeviceItems.addAll(devices);
            mAdapter.setNewData(mDeviceItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}

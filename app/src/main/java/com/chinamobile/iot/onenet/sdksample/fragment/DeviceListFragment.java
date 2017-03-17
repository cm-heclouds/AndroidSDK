package com.chinamobile.iot.onenet.sdksample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;

import java.util.List;

public class DeviceListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_device_list, container, false);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recyler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return contentView;
    }

    class DeviceListAdapter extends BaseQuickAdapter<DeviceItem, BaseViewHolder> {

        public DeviceListAdapter(List<DeviceItem> data) {
            super(R.layout.device_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceItem item) {

        }
    }
}

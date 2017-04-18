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
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.model.TriggerItem;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class TriggerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TriggerListAdapter mAdapter;
    private int mCurrentPage = 1;
    private int mTotalCount;
    private List<TriggerItem> mTriggerItems = new ArrayList<>();

    public static TriggerListFragment newInstance() {
        return new TriggerListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_trigger_list, container, false);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recyler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TriggerListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mRecyclerView);
        mSwipeRefreshLayout.setColorSchemeColors(0xFFDA4336);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getTriggers(false);
        mSwipeRefreshLayout.setRefreshing(true);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateApiKeyReceiver, new IntentFilter(IntentActions.ACTION_UPDATE_APIKEY));
        return contentView;
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateApiKeyReceiver);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        getTriggers(false);
    }

    class TriggerListAdapter extends BaseQuickAdapter<TriggerItem, BaseViewHolder> {

        public TriggerListAdapter() {
            super(R.layout.trigger_list_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, TriggerItem item) {
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.ds_id, getResources().getString(R.string.ds_id) + "：" + item.getDsId());
            helper.setText(R.id.create_time, getResources().getString(R.string.create_time) + "：" + item.getCreateTime());
            helper.setText(R.id.url, getResources().getString(R.string.url_address) + "：" + item.getUrl());
        }
    }

    private void getTriggers(final boolean loadMore) {
        if (loadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
        }
        OneNetApi.fuzzyQueryTriggers(null, mCurrentPage, 10, new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    JsonElement data = resp.get("data");
                    if (data != null) {
                        parseData(data.getAsJsonObject(), loadMore);
                    }
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
        mTotalCount = data.get("total_count").getAsInt();
        Gson gson = new Gson();
        List<TriggerItem> triggers = gson.fromJson(data, TriggerListFragment.Data.class).getTriggers();
        if (!loadMore) {
            mTriggerItems.clear();
        }
        mTriggerItems.addAll(triggers);
        mAdapter.setNewData(mTriggerItems);
    }

    class Data {
        private List<TriggerItem> triggers;

        public List<TriggerItem> getTriggers() {
            return triggers;
        }

        public void setTriggers(List<TriggerItem> triggers) {
            this.triggers = triggers;
        }
    }

    private BroadcastReceiver mUpdateApiKeyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSwipeRefreshLayout.setRefreshing(true);
            getTriggers(false);
        }
    };

    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            if (mTriggerItems.size() >= mTotalCount) {
                mAdapter.loadMoreEnd();
            } else {
                getTriggers(true);
            }
        }
    };
}

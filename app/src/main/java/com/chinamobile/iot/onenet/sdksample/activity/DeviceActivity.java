package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.model.DSItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeviceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String EXTRA_DEVICE_ID = "extra_device_id";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DSListAdapter mAdapter;
    private FloatingActionButton mFabAddDataStream;
    private String mDeviceId;

    public static void actionDevice(Context context, String deviceId) {
        Intent intent = new Intent(context, DeviceActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        initViews();
    }

    private void initViews() {
        mDeviceId = getIntent().getStringExtra(EXTRA_DEVICE_ID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mFabAddDataStream = (FloatingActionButton) findViewById(R.id.fab_add_ds);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DSListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(0xFFDA4336);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getDataStreams();
        mSwipeRefreshLayout.setRefreshing(true);

        mFabAddDataStream.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        getDataStreams();
    }

    @Override
    public void onClick(View v) {

    }

    class DSListAdapter extends BaseQuickAdapter<DSItem, BaseViewHolder> {
        public DSListAdapter() {
            super(R.layout.ds_list_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, DSItem item) {
            helper.setText(R.id.id, item.getId());
            helper.setText(R.id.current_value, item.getCurrentValue().toString());
            helper.setText(R.id.update_time, item.getUpdateTime());
        }
    }

    private void getDataStreams() {
        OneNetApi.queryMultiDataStreams(mDeviceId, new OneNetApiCallback() {
            @Override
            public void onSuccess(int errno, String error, String data) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (0 == errno) {
                    parseData(data);
                } else {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
                e.printStackTrace();
            }
        });
    }

    private void parseData(String data) {
//        if (null == data) {
//            return;
//        }
//        Gson gson = new Gson();
//        Collection<DSItem> dsItems = gson.fromJson(data, Collection.class);
//        ArrayList<DSItem> dsItems1 = new ArrayList<>();
//        dsItems1.addAll(dsItems);
//        mAdapter.setNewData(dsItems1);
    }
}

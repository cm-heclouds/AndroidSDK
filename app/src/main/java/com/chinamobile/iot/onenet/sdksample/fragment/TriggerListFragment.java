package com.chinamobile.iot.onenet.sdksample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.iot.onenet.sdksample.R;

public class TriggerListFragment extends Fragment {

    public static TriggerListFragment newInstance() {
        return new TriggerListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_trigger_list, container, false);
        return contentView;
    }

}

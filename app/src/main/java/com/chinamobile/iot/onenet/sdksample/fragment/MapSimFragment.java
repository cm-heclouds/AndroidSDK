package com.chinamobile.iot.onenet.sdksample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.iot.onenet.sdksample.R;

public class MapSimFragment extends Fragment {

    public static MapSimFragment newInstance() {
        return new MapSimFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_sim, container, false);
        return v;
    }
}

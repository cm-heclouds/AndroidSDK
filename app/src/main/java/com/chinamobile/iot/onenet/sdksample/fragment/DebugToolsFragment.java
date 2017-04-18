package com.chinamobile.iot.onenet.sdksample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.iot.onenet.sdksample.R;

import java.util.ArrayList;
import java.util.List;

public class DebugToolsFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>(3);
    private List<String> mTitleList = new ArrayList<>(3);

    public static DebugToolsFragment newInstance() {
        return new DebugToolsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentList.add(MapSimFragment.newInstance());
        mFragmentList.add(DataSimFragment.newInstance());
        mFragmentList.add(ApiDebugFragment.newInstance());
        mTitleList.add(getResources().getString(R.string.map_simulator));
        mTitleList.add(getResources().getString(R.string.data_simulator));
        mTitleList.add(getResources().getString(R.string.api_debug_tool));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_debug_tools, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
                mViewPager = (ViewPager) v.findViewById(R.id.view_pager);
                mViewPager.setOffscreenPageLimit(mFragmentList.size());
                mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
                mTabLayout.setupWithViewPager(mViewPager, true);
            }
        }, 300);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

}

package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.fragment.ApiTestFragment;
import com.chinamobile.iot.onenet.sdksample.fragment.DebugToolsFragment;
import com.chinamobile.iot.onenet.sdksample.fragment.DeviceListFragment;
import com.chinamobile.iot.onenet.sdksample.fragment.TriggerListFragment;
import com.chinamobile.iot.onenet.sdksample.utils.Preferences;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        String apikey = OneNetApi.getAppKey();
        if (TextUtils.isEmpty(apikey) || apikey.contains(" ")) {
            apikey = Preferences.getInstance(this).getString(Preferences.API_KEY, null);
        }
        if (TextUtils.isEmpty(apikey) || apikey.contains(" ")) {
            OneNetApi.setAppKey("");
            startActivity(new Intent(this, EditApiKeyActivity.class));
        } else {
            OneNetApi.setAppKey(apikey);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setCheckedItem(R.id.device);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, DeviceListFragment.newInstance()).commit();
    }

    NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            mDrawerLayout.closeDrawer(GravityCompat.START);

            Fragment fragment;
            int titleResId;
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.device:
                    fragment = DeviceListFragment.newInstance();
                    titleResId = R.string.device;
                    ft.replace(R.id.fragment, fragment).commit();
                    getSupportActionBar().setTitle(titleResId);
                    break;

                case R.id.trigger:
                    fragment = TriggerListFragment.newInstance();
                    titleResId = R.string.trigger;
                    ft.replace(R.id.fragment, fragment).commit();
                    getSupportActionBar().setTitle(titleResId);
                    break;

                case R.id.debug_online:
                    fragment = DebugToolsFragment.newInstance();
                    titleResId = R.string.debug_online;
                    ft.replace(R.id.fragment, fragment).commit();
                    getSupportActionBar().setTitle(titleResId);
                    break;

                case R.id.api_test:
                    fragment = ApiTestFragment.newInstance();
                    titleResId = R.string.api_test;
                    ft.replace(R.id.fragment, fragment).commit();
                    getSupportActionBar().setTitle(titleResId);
                    break;

                case R.id.edit_apikey:
                    startActivity(new Intent(MainActivity.this, EditApiKeyActivity.class));
                    break;
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

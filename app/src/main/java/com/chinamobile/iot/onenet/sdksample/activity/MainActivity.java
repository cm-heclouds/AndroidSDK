package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.fragment.DeviceListFragment;
import com.chinamobile.iot.onenet.sdksample.fragment.TriggerListFragment;
import com.chinamobile.iot.onenet.sdksample.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>(2);
    private List<String> mPagerTitles = new ArrayList<>(2);

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFragmentList.add(DeviceListFragment.newInstance());
        mFragmentList.add(TriggerListFragment.newInstance());
        mPagerTitles.add(getResources().getString(R.string.device));
        mPagerTitles.add(getResources().getString(R.string.trigger));
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);

        String apikey = Preferences.getInstance(this).getString(Preferences.API_KEY, "");
        if (0 == apikey.length()) {
            apikey = OneNetApi.getAppKey();
        }
        if (0 == apikey.length()) {
            startActivity(new Intent(this, EditApiKeyActivity.class));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_apikey:
                startActivity(new Intent(this, EditApiKeyActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
            return mPagerTitles.get(position);
        }
    };

    NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

            Fragment fragment = null;
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.home:
                    break;
                case R.id.debug:
                    break;
            }

            //ft.replace(R.id.fragment, fragment).commit();
            return true;
        }
    };

}

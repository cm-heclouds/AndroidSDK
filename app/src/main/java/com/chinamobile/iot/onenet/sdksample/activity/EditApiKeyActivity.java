package com.chinamobile.iot.onenet.sdksample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.utils.IntentActions;
import com.chinamobile.iot.onenet.sdksample.utils.Preferences;

public class EditApiKeyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mTextInputLayout;
    private Preferences mPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_api_key);

        mPreferences = Preferences.getInstance(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        String apikey = mPreferences.getString(Preferences.API_KEY, "");
        if (0 == apikey.length()) {
            apikey = OneNetApi.getAppKey();
        }
        mTextInputLayout.getEditText().setText(apikey);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_done:
                String apiKey = mTextInputLayout.getEditText().getText().toString();
                OneNetApi.setAppKey(apiKey.trim());
                mPreferences.putString(Preferences.API_KEY, apiKey);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_APIKEY));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

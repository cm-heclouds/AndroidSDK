package com.chinamobile.iot.onenet.sample;

import org.json.JSONException;
import org.json.JSONObject;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetError;
import com.chinamobile.iot.onenet.OneNetResponse;
import com.chinamobile.iot.onenet.ResponseListener;
import com.chinamobile.iot.onenet.apisample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    private static final String LOG_TAG = Login.class.getSimpleName();

    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initViews();
    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);

        mUsername.setText(Preferences.getInstance(this).getUsername());
        mPassword.setText(Preferences.getInstance(this).getPassword());
    }

    public void login(View v) {
        if (mUsername.getText().toString().trim().length() == 0) {
            mUsername.requestFocus();
            mUsername.setError("用户名不能为空");
            return;
        }
        if (mPassword.getText().toString().trim().length() == 0) {
            mPassword.requestFocus();
            mPassword.setError("密码不能为空");
            return;
        }

        if (Utils.checkNetwork(this)) {
            sendLoginRequest();
        }
    }

    private void sendLoginRequest() {
        OneNetApi.getInstance(this).login(mUsername.getText().toString(), mPassword.getText().toString(), new ResponseListener() {

            @Override
            public void onResponse(OneNetResponse response) {
                if (response.getErrno() == 0) {
                    Preferences.getInstance(Login.this).setUsername(mUsername.getText().toString());
                    Preferences.getInstance(Login.this).setPassword(mPassword.getText().toString());

                    if (response.getData() != null) {
                        try {
                            JSONObject data = new JSONObject(response.getData());
                            SampleApp.sApiKey = data.optString("api_key");
                            Log.i(LOG_TAG, "api_key =====> " + SampleApp.sApiKey);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Toast.makeText(Login.this, response.getError(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(OneNetError error) {
                Toast.makeText(Login.this, Log.getStackTraceString(error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

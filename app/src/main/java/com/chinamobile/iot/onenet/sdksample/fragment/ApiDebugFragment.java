package com.chinamobile.iot.onenet.sdksample.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.http.HttpExecutor;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.util.OneNetLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiDebugFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout mApiUrlLayout;
    private AppCompatSpinner mRequestMethodSpinner;
    private TextView mRequestParamsTextView;
    private Button mAddParamsButton;
    private Button mClearParamsButton;
    private TextInputLayout mRequestContentLayout;
    private Button mSendRequestButton;
    private TextView mResponseLogTextView;
    private AlertDialog mAddParamsDialog;
    private Map<String, String> mFormData = new HashMap<>();
    private LayoutInflater mLayoutInflater;
    private Handler mHandler = new Handler();

    public static ApiDebugFragment newInstance() {
        return new ApiDebugFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_api_debug, container, false);
        mApiUrlLayout = (TextInputLayout) v.findViewById(R.id.api_url);
        mRequestMethodSpinner = (AppCompatSpinner) v.findViewById(R.id.request_method_spinner);
        mRequestParamsTextView = (TextView) v.findViewById(R.id.request_params);
        mAddParamsButton = (Button) v.findViewById(R.id.add);
        mClearParamsButton = (Button) v.findViewById(R.id.clear);
        mRequestContentLayout = (TextInputLayout) v.findViewById(R.id.request_content);
        mSendRequestButton = (Button) v.findViewById(R.id.send);
        mResponseLogTextView = (TextView) v.findViewById(R.id.response_log);

        mAddParamsButton.setOnClickListener(this);
        mClearParamsButton.setOnClickListener(this);
        mSendRequestButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                showAddParamsDialog();
                break;

            case R.id.clear:
                mRequestParamsTextView.setText("");
                mFormData.clear();
                break;

            case R.id.send:
                sendRequest();
                break;
        }
    }

    private void showAddParamsDialog() {
        View view = mLayoutInflater.inflate(R.layout.dialog_add_request_params, null);
        final TextInputEditText nameEditText = (TextInputEditText) view.findViewById(R.id.name);
        final TextInputEditText valueEditText = (TextInputEditText) view.findViewById(R.id.value);
        mAddParamsDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.request_params)
                .setView(view)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString();
                        String value = valueEditText.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(getContext(), R.string.param_name_empty_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(value)) {
                            Toast.makeText(getContext(), R.string.param_value_empty_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mFormData.put(name, value);
                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<String, String> entry : mFormData.entrySet()) {
                            sb.append(getResources().getString(R.string.param_name));
                            sb.append(": ");
                            sb.append(entry.getKey());
                            sb.append("  ");
                            sb.append(getResources().getString(R.string.param_value));
                            sb.append(": ");
                            sb.append(entry.getValue());
                            sb.append("\n");
                        }
                        if (!mFormData.isEmpty()) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        mRequestParamsTextView.setText(sb.toString());
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();

    }

    private void sendRequest() {
        String url = mApiUrlLayout.getEditText().getText().toString();
        String requestString = mRequestContentLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(url)) {
            mApiUrlLayout.setError(getResources().getString(R.string.api_url));
            mApiUrlLayout.requestFocus();
            return;
        }
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (null == httpUrl) {
            mApiUrlLayout.setError(getResources().getString(R.string.invalid_url));
            mApiUrlLayout.requestFocus();
            return;
        }
        HttpUrl.Builder builder = httpUrl.newBuilder(url);
        for (Map.Entry<String, String> entry : mFormData.entrySet()) {
            builder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
        }
        url = builder.toString();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OneNetLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor);
        okHttpClientBuilder.addInterceptor(sApiKeyInterceptor);
        HttpExecutor httpExecutor = new HttpExecutor(okHttpClientBuilder.build());

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestString);

        switch (mRequestMethodSpinner.getSelectedItemPosition()) {
            case 0:
                httpExecutor.get(url, mCallback);
                break;

            case 1:
                httpExecutor.post(url, requestBody, mCallback);
                break;

            case 2:
                httpExecutor.put(url, requestBody, mCallback);
                break;

            case 3:
                httpExecutor.delete(url, mCallback);
                break;
        }
    }

    private static Interceptor sApiKeyInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("api-key", OneNetApi.getAppKey());
            if (TextUtils.isEmpty(OneNetApi.getAppKey())) {
                Log.e(OneNetApi.LOG_TAG, "APP-KEY is messing, please config in the meta-data or call setAppKey()");
            }
            return chain.proceed(builder.build());
        }
    };

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String responseString = response.body().string();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonParser jsonParser = new JsonParser();
                        mResponseLogTextView.setText(gson.toJson(jsonParser.parse(responseString)));
                    } catch (Exception e) {
                        mResponseLogTextView.setText(responseString);
                    }
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAddParamsDialog != null && mAddParamsDialog.isShowing()) {
            mAddParamsDialog.dismiss();
        }
    }
}

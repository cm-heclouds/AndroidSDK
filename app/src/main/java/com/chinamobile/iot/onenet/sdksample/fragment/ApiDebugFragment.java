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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * API调试工具
 */
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

        switch (mRequestMethodSpinner.getSelectedItemPosition()) {
            case 0:
                OneNetApi.get(url, mCallback);
                break;

            case 1:
                OneNetApi.post(url, requestString, mCallback);
                break;

            case 2:
                OneNetApi.put(url, requestString, mCallback);
                break;

            case 3:
                OneNetApi.delete(url, mCallback);
                break;
        }
    }

    private OneNetApiCallback mCallback = new OneNetApiCallback() {
        @Override
        public void onSuccess(final String response) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonParser jsonParser = new JsonParser();
                        mResponseLogTextView.setText(gson.toJson(jsonParser.parse(response)));
                    } catch (Exception e) {
                        mResponseLogTextView.setText(response);
                    }
                }
            });
        }

        @Override
        public void onFailed(final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseLogTextView.setText(e.toString());
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

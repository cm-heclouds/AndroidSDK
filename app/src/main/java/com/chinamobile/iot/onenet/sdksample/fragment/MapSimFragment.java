package com.chinamobile.iot.onenet.sdksample.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.sdksample.R;
import com.chinamobile.iot.onenet.sdksample.utils.GCJ2WGS;
import com.chinamobile.iot.onenet.sdksample.utils.PermissionUtil;
import com.chinamobile.iot.onenet.sdksample.view.MapContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 地图数据模拟器
 */
public class MapSimFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private TextInputLayout mDeviceIdLayout;
    private TextInputLayout mDataStreamLayout;
    private MapView mMapView;
    private AMap mAmap;
    private TextView mLocationTextView;
    private Button mSendButton;
    private TextView mResponseLogTextView;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private ProgressDialog mProgressDialog;

    private LatLng mRealLatLng;
    private LatLng mCorrectedLatLng;

    public static MapSimFragment newInstance() {
        return new MapSimFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_sim, container, false);

        mDeviceIdLayout = (TextInputLayout) v.findViewById(R.id.device_id);
        mDataStreamLayout = (TextInputLayout) v.findViewById(R.id.datastream);
        mMapView = (MapView) v.findViewById(R.id.map_view);
        mLocationTextView = (TextView) v.findViewById(R.id.location);
        mSendButton = (Button) v.findViewById(R.id.send);
        mResponseLogTextView = (TextView) v.findViewById(R.id.response_log);

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_view);
        MapContainer mapContainer = (MapContainer) v.findViewById(R.id.map_container);
        mapContainer.setScrollView(scrollView);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.setOnMapClickListener(mOnMapClickListener);
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getResources().getString(R.string.locating));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
            mLocationClient.setLocationListener(mLocationListener);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.setLocationOption(mLocationOption);
        }

        if (PermissionUtil.hasPermission(getActivity(), PERMISSIONS, REQUEST_CODE_PERMISSIONS)) {
            mLocationClient.startLocation();
            mProgressDialog.show();
        }

        mSendButton.setOnClickListener(this);

        return v;
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mProgressDialog.dismiss();
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mCorrectedLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mRealLatLng = getRealLatLng(mCorrectedLatLng);
                mAmap.animateCamera(CameraUpdateFactory.zoomTo(17));
                mAmap.animateCamera(CameraUpdateFactory.changeLatLng(mCorrectedLatLng));
                mLocationTextView.setText("lon: " + aMapLocation.getLongitude() + "° / lat: " + aMapLocation.getLatitude() + "°");
                mAmap.clear();
                mAmap.addMarker(new MarkerOptions().position(mCorrectedLatLng));
            }
        }
    };

    private AMap.OnMapClickListener mOnMapClickListener = new AMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            mCorrectedLatLng = latLng;
            mRealLatLng = getRealLatLng(mCorrectedLatLng);
            mLocationTextView.setText("lon: " + latLng.longitude + "° / lat: " + latLng.latitude + "°");
            mAmap.clear();
            mAmap.addMarker(new MarkerOptions().position(latLng));
            mAmap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.verifyPermissions(grantResults)) {
            mProgressDialog.show();
            mLocationClient.startLocation();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.rationale_message_amap)
                    .setPositiveButton(R.string.action_ok, null)
                    .show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private LatLng getRealLatLng(LatLng correctedLatLng) {
        return GCJ2WGS.convert(correctedLatLng);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                sendData();
                break;
        }
    }

    private void sendData() {
        String deviceId = mDeviceIdLayout.getEditText().getText().toString().trim();
        String datastream = mDataStreamLayout.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(deviceId)) {
            mDeviceIdLayout.setError(getResources().getString(R.string.device_id));
            mDeviceIdLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(datastream)) {
            mDataStreamLayout.setError(getResources().getString(R.string.datastream));
            mDataStreamLayout.requestFocus();
            return;
        }
        try {
            JSONObject location = new JSONObject();
            location.putOpt("lat", mRealLatLng.latitude);
            location.putOpt("lon", mRealLatLng.longitude);
            JSONObject datapoint = new JSONObject();
            datapoint.putOpt("value", location);

            JSONArray datapoints = new JSONArray();
            datapoints.put(datapoint);

            JSONObject dsObject = new JSONObject();
            dsObject.putOpt("id", datastream);
            dsObject.putOpt("datapoints", datapoints);

            JSONArray datastreams = new JSONArray();
            datastreams.put(dsObject);

            JSONObject request = new JSONObject();
            request.putOpt("datastreams", datastreams);

            OneNetApi.addDataPoints(deviceId, request.toString(), new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    displayLog(response);
                }

                @Override
                public void onFailed(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayLog(String response) {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
        }
        mResponseLogTextView.setText(response);
    }
}

package com.chinamobile.iot.onenet;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;

class Login extends BaseStringRequest {

    private static final String URL = BASE_URL + "/login";

    private String mUsername;
    private String mPassword;

    public Login(String username, String password, ResponseListener listener) {
        super(Method.POST, URL, listener);
        mUsername = username;
        mPassword = password;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", mUsername);
            obj.put("password", mPassword);
            body = obj.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}

package com.chinamobile.iot.onenet;

/**
 * OneNetApi的响应
 * 
 * @author chenglei
 *
 */
public class OneNetResponse {

    private int mErrno;

    private String mError;

    private String mData;

    private String mRawResponse;

    /**
     * 获取响应码
     */
    public int getErrno() {
        return mErrno;
    }

    public void setErrno(int errno) {
        mErrno = errno;
    }

    /**
     * 获取Api返回的错误内容
     */
    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    /**
     * 获取data
     */
    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    /**
     * 获取完整响应
     */
    public String getRawResponse() {
        return mRawResponse;
    }

    public void setRawResponse(String rawResponse) {
        mRawResponse = rawResponse;
    }

}

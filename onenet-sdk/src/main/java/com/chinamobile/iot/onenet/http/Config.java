package com.chinamobile.iot.onenet.http;

import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

public class Config {

    final int retryCount;
    final int connectTimeout;
    final int readTimeout;
    final int writeTimeout;

    private Config(Builder builder) {
        this.retryCount = builder.retryCount;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.connectTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    public int retryCount() {
        return retryCount;
    }

    public int connectTimeout() {
        return connectTimeout;
    }

    public int readTimeout() {
        return readTimeout;
    }

    public int writeTimeout() {
        return writeTimeout;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        int retryCount;
        int connectTimeout;
        int readTimeout;
        int writeTimeout;

        private Builder() {

        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public Builder connectTimeout(int connectTimeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("connectTimeout", connectTimeout, unit);
            return this;
        }

        public Builder readTimeout(int readTimeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("connectTimeout", readTimeout, unit);
            return this;
        }

        public Builder writeTimeout(int writeTimeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("connectTimeout", writeTimeout, unit);
            return this;
        }

        public Config build() {
            return new Config(this);
        }

    }

}

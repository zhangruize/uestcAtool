package com.example.cdzhangruize1.hotpursuit.utils;

import com.example.cdzhangruize1.hotpursuit.constant.Constant;

import okhttp3.Request;

public class RequestUtils {
    public static Request leanCloud(String url) {
        return new Request.Builder().url(url).addHeader(Constant.APP_ID_NAME, Constant.APP_ID)
                .addHeader(Constant.APP_KEY_NAME, Constant.APP_KEY).build();
    }
}

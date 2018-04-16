package com.example.cdzhangruize1.hotpursuit.callback;


import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class JsonCallback<T> extends BaseCallback<T> {
    private Type mType;
    Gson mGson = new Gson();

    JsonCallback(Type mType) {
        this.mType = mType;
    }

    public void onResponse() {
        Response response = getResponse();
        if (response.body() != null) {
            try {
                String json = response.body().string();
                T result = mGson.fromJson(json, mType);
                onSucceed(result);
            } catch (Exception e) {
                onFailed(e);
            }
        }
    }
}

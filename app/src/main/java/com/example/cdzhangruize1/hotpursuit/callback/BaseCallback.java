package com.example.cdzhangruize1.hotpursuit.callback;

import okhttp3.Response;

public abstract class BaseCallback<T> {
    private Response mResponse;

    public void setResponse(Response mResponse) {//在io线程传回response
        this.mResponse = mResponse;
    }

    Response getResponse() {//onResponse的处理依赖于此
        return mResponse;
    }

    abstract void onSucceed(T data);

    public abstract void onFailed(Exception e);

    public abstract void onResponse();//应该在ui线程调用，即把之前io线程得到的response最后在ui线程处理
}

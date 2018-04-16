package com.example.cdzhangruize1.hotpursuit.observable;

import android.annotation.SuppressLint;

import com.example.cdzhangruize1.hotpursuit.callback.BaseCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BasicObservable {
    private ObservableEmitter<BaseCallback> e;
    private OkHttpClient mClient = new OkHttpClient();

    @SuppressLint("CheckResult")
    public BasicObservable(final Request request) {
        Observable.create(new ObservableOnSubscribe<BaseCallback>() {
            @Override
            public void subscribe(ObservableEmitter<BaseCallback> emitter) throws Exception {
                e = emitter;
            }
        }).observeOn(Schedulers.computation()).map(new Function<BaseCallback, BaseCallback>() {
            @Override
            public BaseCallback apply(BaseCallback callback) throws Exception {
                callback.setResponse(mClient.newCall(request).execute());
                return callback;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseCallback>() {
            @Override
            public void accept(BaseCallback callback) throws Exception {
                callback.onResponse();
            }
        });

    }

    public ObservableEmitter<BaseCallback> getEmitter() {
        return e;
    }
}

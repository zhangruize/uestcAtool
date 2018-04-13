package com.example.cdzhangruize1.hotpursuit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.cdzhangruize1.hotpursuit.constant.Constant;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.pojo.JsonPojo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScraperModelUtils {
    private static final String KEY_SCRAPER_MODELS = "scraper_models";

    private ArrayList<BaseScraperModel> mData;
    private OkHttpClient mClient = new OkHttpClient();
    private Gson mGson = new Gson();
    private SharedPreferences mPreferences;
    private ObservableEmitter<Callback> mEmitter;
    private static ScraperModelUtils sInstance;

    @SuppressLint("CheckResult")
    private ScraperModelUtils() {
        Observable.create(new ObservableOnSubscribe<Callback>() {
            @Override
            public void subscribe(ObservableEmitter<Callback> emitter) throws Exception {
                mEmitter = emitter;
            }
        }).observeOn(Schedulers.computation()).map(new Function<Callback, Callback>() {
            @Override
            public Callback apply(Callback callback) throws Exception {
                Request request = new Request.Builder().url(Constant.GET_MODELS).addHeader(Constant.APP_ID_NAME, Constant.APP_ID)
                        .addHeader(Constant.APP_KEY_NAME, Constant.APP_KEY).build();
                Response response = mClient.newCall(request).execute();
                if (response.body() != null) {
                    try {
                        String json = response.body().string();
                        JsonPojo pojo = mGson.fromJson(json, JsonPojo.class);
                        callback.data = mGson.fromJson(pojo.json, new TypeToken<ArrayList<BaseScraperModel>>() {
                        }.getType());
                    } catch (Exception e) {
                        callback.onFailed();
                    }
                }
                return callback;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Callback>() {
            @Override
            public void accept(Callback callback) throws Exception {
                syncCheckStateFromLocal(callback.data);
                saveScaperModels(callback.data);
                callback.onSucceed(callback.data);
            }
        });
    }

    public static ScraperModelUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ScraperModelUtils();
            String name = context.getPackageName();
            sInstance.mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return sInstance;
    }

    public ArrayList<BaseScraperModel> getLocalScraperModels() {
        if (mData == null) {
            String json = mPreferences.getString(KEY_SCRAPER_MODELS, null);
            if (json != null) {
                mData = new ArrayList<>();
                mData = mGson.fromJson(json, new TypeToken<ArrayList<BaseScraperModel>>() {
                }.getType());
            }
        }
        return mData;
    }

    public void saveScaperModels(ArrayList<BaseScraperModel> data) {
        mData = data;
        String json = mGson.toJson(data);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_SCRAPER_MODELS, json);
        editor.apply();
    }

    public void getRemoteScraperModels(Callback callback) {
        mEmitter.onNext(callback);
    }

    private void syncCheckStateFromLocal(ArrayList<BaseScraperModel> fromRemote) {
        ArrayList<BaseScraperModel> localList = getLocalScraperModels();
        if (fromRemote != null && localList != null) {
            for (BaseScraperModel local : localList) {
                for (BaseScraperModel web : fromRemote) {
                    if (web.getName().equals(local.getName())) {
                        web.setSubscribe(local.isSubscribe());
                        break;
                    }
                }
            }
            for (BaseScraperModel web : fromRemote) {
                boolean has = false;
                for (BaseScraperModel local : localList) {
                    if (web.getName().equals(local.getName())) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    web.setIsNew(true);
                }
            }
        }
    }

    public static abstract class Callback {
        ArrayList<BaseScraperModel> data;

        public abstract void onSucceed(ArrayList<BaseScraperModel> data);

        public abstract void onFailed();
    }
}

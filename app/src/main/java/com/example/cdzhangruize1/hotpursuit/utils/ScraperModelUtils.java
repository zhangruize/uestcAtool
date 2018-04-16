package com.example.cdzhangruize1.hotpursuit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.cdzhangruize1.hotpursuit.callback.BaseCallback;
import com.example.cdzhangruize1.hotpursuit.callback.ScraperModelListCallback;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.observable.BasicObservable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.cdzhangruize1.hotpursuit.constant.Constant.GET_MODELS;
import static com.example.cdzhangruize1.hotpursuit.constant.Constant.KEY_SCRAPER_MODELS;

public class ScraperModelUtils {
    private final BasicObservable basicObservable;
    private ArrayList<BaseScraperModel> mData;
    private Gson mGson = new Gson();
    private SharedPreferences mPreferences;
    private static ScraperModelUtils sInstance;

    @SuppressLint("CheckResult")
    private ScraperModelUtils() {
        basicObservable = new BasicObservable(RequestUtils.leanCloud(GET_MODELS));
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

    public void saveScraperModels(ArrayList<BaseScraperModel> data) {
        mData = data;
        String json = mGson.toJson(data);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_SCRAPER_MODELS, json);
        editor.apply();
    }

    public void getRemoteScraperModels(ScraperModelListCallback callback) {
        basicObservable.getEmitter().onNext(wrapCallback(callback));
    }

    private BaseCallback wrapCallback(final ScraperModelListCallback callback) {
        return new ScraperModelListCallback() {
            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }

            @Override
            public void onSucceed(ArrayList<BaseScraperModel> data) {
                syncCheckStateFromLocal(data);
                saveScraperModels(data);
                callback.onSucceed(data);
            }
        };
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
}

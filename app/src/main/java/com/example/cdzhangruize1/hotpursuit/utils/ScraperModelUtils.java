package com.example.cdzhangruize1.hotpursuit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ScraperModelUtils {
    private static final String KEY_SCRAPER_MODELS = "scraper_models";

    private Gson mGson = new Gson();
    private ArrayList<BaseScraperModel> mData;
    private SharedPreferences mPreferences;
    private static ScraperModelUtils sInstance;

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
            mData = new ArrayList<>();
            String json = mPreferences.getString(KEY_SCRAPER_MODELS, null);
            if (json != null) {
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
}

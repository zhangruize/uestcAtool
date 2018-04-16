package com.example.cdzhangruize1.hotpursuit.constant;

import com.example.cdzhangruize1.hotpursuit.BuildConfig;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.pojo.JsonPojo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Constant {
    public final static String APP_KEY = "v7JwsDv1mYCTxt7bdkGWPGnK";
    public final static String APP_KEY_NAME = "X-LC-Key";
    public final static String APP_ID = "tV8mvNGlNbTlrwKElsh6JmpP-gzGzoHsz";
    public final static String APP_ID_NAME = "X-LC-Id";
    public final static String GET_MODELS = "https://leancloud.cn:443/1.1/classes/Json/5ad0506a9f54540045a6aaf1";
    public final static String GET_VERSION = "https://leancloud.cn:443/1.1/classes/Json/5ad050769f54540045a6ab33";
    public final static int APP_VERSION_CODE = BuildConfig.VERSION_CODE;
    public final static String APP_VERSION_NAME = BuildConfig.VERSION_NAME;
    public final static boolean DEBUG = BuildConfig.DEBUG;

    public final static Type TYPE_JSON_POJO = new TypeToken<JsonPojo>(){}.getType();
    public final static Type TYPE_SCRAPER_MODEL_LIST = new TypeToken<ArrayList<BaseScraperModel>>(){}.getType();

    public final static String KEY_SCRAPER_MODELS = "scraper_models";
}

package com.example.cdzhangruize1.hotpursuit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_JSON;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_SCRAPER;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

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

    public void getRemoteScraperModels(Callback callback) {        // todo 未来需要从网络端获取模型数据
        ArrayList<BaseScraperModel> temp = new ArrayList<>();
        temp.add(createTestModel1());
        temp.add(createTestModel2());

        syncCheckStateFromLocal(temp);
        callback.onSucceed(temp);
    }

    private BaseScraperModel createTestModel1() {
        BaseScraperModel model = new BaseScraperModel("文化活动", null, MODEL_TYPE_SCRAPER, 1);
        model.addLink("http://www.new1.uestc.edu.cn/?n=UestcNews.Front.Category.Page&CatId=67");
        model.setIcon("https://image.flaticon.com/sprites/new_packs/148705-essential-collection.png");
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > h3 > a", "title", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > p", "message", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > a > img", "pic", ELEMENT_TYPE_SRC));
        return model;
    }

    private BaseScraperModel createTestModel2() {
        BaseScraperModel model = new BaseScraperModel("微博", null, MODEL_TYPE_JSON, 0);
        model.addLink("https://m.weibo.cn/api/container/getIndex?containerid=1076031793285524");
        model.setIcon("https://image.flaticon.com/sprites/new_packs/148705-essential-collection.png");
        model.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['user']['screen_name']", "title"));
        model.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['text']", "message"));
        model.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['original_pic']", "pic"));
        return model;
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

    public interface Callback {
        void onSucceed(ArrayList<BaseScraperModel> data);

        void onFailed();
    }
}

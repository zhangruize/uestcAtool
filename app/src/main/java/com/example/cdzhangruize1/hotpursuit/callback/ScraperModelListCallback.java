package com.example.cdzhangruize1.hotpursuit.callback;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.pojo.JsonPojo;

import java.util.ArrayList;

import static com.example.cdzhangruize1.hotpursuit.constant.Constant.TYPE_SCRAPER_MODEL_LIST;

public abstract class ScraperModelListCallback extends JsonPojoCallback {
    @SuppressWarnings("unchecked")
    @Override
    void onSucceed(JsonPojo data) {
        try {
            ArrayList<BaseScraperModel> list = mGson.fromJson(data.json, TYPE_SCRAPER_MODEL_LIST);
            onSucceed(list);
        } catch (Exception e) {
            onFailed(e);
        }
    }

    public abstract void onSucceed(ArrayList<BaseScraperModel> data);
}

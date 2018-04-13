package com.example.cdzhangruize1.hotpursuit;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_JSON;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_SCRAPER;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

public class ScraperModelToJson {
    @Test
    public void toJson() {
        ArrayList<BaseScraperModel> toConvert = getTestModels();
        Gson gson = new Gson();
        System.out.println(gson.toJson(toConvert));
    }

    private static ArrayList<BaseScraperModel> getTestModels() {
        ArrayList<BaseScraperModel> temp = new ArrayList<>();
        BaseScraperModel model = new BaseScraperModel("文化活动", null, MODEL_TYPE_SCRAPER, 1);
        model.addLink("http://www.new1.uestc.edu.cn/?n=UestcNews.Front.Category.Page&CatId=67");
        model.setIcon("https://image.flaticon.com/sprites/new_packs/148705-essential-collection.png");
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > h3 > a", "title", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > p", "message", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new BaseScraperModel.MapRule("#Degas_news_list > ul > li:nth-child($) > a > img", "pic", ELEMENT_TYPE_SRC));

        BaseScraperModel model2 = new BaseScraperModel("微博", null, MODEL_TYPE_JSON, 0);
        model2.addLink("https://m.weibo.cn/api/container/getIndex?containerid=1076031793285524");
        model2.setIcon("https://image.flaticon.com/sprites/new_packs/148705-essential-collection.png");
        model2.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['user']['screen_name']", "title"));
        model2.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['text']", "message"));
        model2.addMapRule(new BaseScraperModel.MapRule("['data'].['cards'][$]['mblog']['original_pic']", "pic"));
        temp.add(model);
        temp.add(model2);
        return temp;
    }
}

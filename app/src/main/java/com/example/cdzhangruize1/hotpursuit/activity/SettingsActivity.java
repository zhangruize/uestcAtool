package com.example.cdzhangruize1.hotpursuit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.adapter.SettingAdapter;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.utils.ScraperModelUtils;

import java.util.ArrayList;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_JSON;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MODEL_TYPE_SCRAPER;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

public class SettingsActivity extends AppCompatActivity {
    RecyclerView mModelList;
    LinearLayoutManager mManager;
    SettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mModelList = findViewById(R.id.modelList);
        mAdapter = new SettingAdapter();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //for test todo 未来需要从网络端获取模型数据
        ArrayList<BaseScraperModel> list = new ArrayList<>();
        list.add(createTestModel1());
        list.add(createTestModel2());
        initCheckState(list);
        mAdapter.dispatchData(list);

        mModelList.setAdapter(mAdapter);
        mModelList.setLayoutManager(mManager);
    }

    private void initCheckState(ArrayList<BaseScraperModel> list) {
        ArrayList<BaseScraperModel> localList = ScraperModelUtils.getInstance(this).getLocalScraperModels();
        if (list != null && localList != null) {
            for (BaseScraperModel local : localList) {
                for (BaseScraperModel web : list) {
                    if (web.getName().equals(local.getName())) {
                        web.setSubscribe(local.isSubscribe());
                        break;
                    }
                }
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScraperModelUtils.getInstance(this).saveScaperModels(mAdapter.getData());
    }
}

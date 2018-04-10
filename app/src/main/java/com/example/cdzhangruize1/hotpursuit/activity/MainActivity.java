package com.example.cdzhangruize1.hotpursuit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.engine.ListEngine;
import com.example.cdzhangruize1.hotpursuit.engine.WebEngine;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.*;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

public class MainActivity extends AppCompatActivity {
    WebEngine mWebEngine = new WebEngine();
    ListEngine mListEngine = new ListEngine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mWebEngine.load(createTestModel2(), mListEngine, this, new WebEngine.LoadCallback2() {
            @Override
            public void onSucceed(RecyclerView view) {
                ((ViewGroup) findViewById(R.id.container)).addView(view,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailed() {
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BaseScraperModel createTestModel1() {
        BaseScraperModel model = new BaseScraperModel("文化活动", null, MODEL_TYPE_SCRAPER, 1);
        model.addLink("http://www.new1.uestc.edu.cn/?n=UestcNews.Front.Category.Page&CatId=67");
        model.addMapRule(new MapRule("#Degas_news_list > ul > li:nth-child($) > h3 > a", "title", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new MapRule("#Degas_news_list > ul > li:nth-child($) > p", "message", ELEMENT_TYPE_INNERHTML));
        model.addMapRule(new MapRule("#Degas_news_list > ul > li:nth-child($) > a > img", "pic", ELEMENT_TYPE_SRC));
        return model;
    }

    private BaseScraperModel createTestModel2() {
        BaseScraperModel model = new BaseScraperModel("微博", null, MODEL_TYPE_JSON, 0);
        model.addLink("https://m.weibo.cn/api/container/getIndex?containerid=1076031793285524");
        model.addMapRule(new MapRule("['data'].['cards'][$]['mblog']['user']['screen_name']", "title"));
        model.addMapRule(new MapRule("['data'].['cards'][$]['mblog']['text']", "message"));
        model.addMapRule(new MapRule("['data'].['cards'][$]['mblog']['original_pic']", "pic"));
        return model;
    }
}

package com.example.cdzhangruize1.hotpursuit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.engine.ListEngine;
import com.example.cdzhangruize1.hotpursuit.engine.WebEngine;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import java.util.ArrayList;
import java.util.HashMap;

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
        mWebEngine.setmLoadCallback(new WebEngine.LoadCallback() {
            @Override
            public void onSucceed(ArrayList<HashMap<String, String>> data) {
                final RecyclerView view = mListEngine.load(data, MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewGroup) findViewById(R.id.container)).addView(view,
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
        mWebEngine.load(createTestModel());
    }

    private BaseScraperModel createTestModel() {
        BaseScraperModel model = new BaseScraperModel();
        model.links.add("http://www.new1.uestc.edu.cn/?n=UestcNews.Front.Category.Page&CatId=67");
        model.xpathMaps.put("#Degas_news_list > ul > li:nth-child($) > h3 > a", "title");
        model.xpathMaps.put("#Degas_news_list > ul > li:nth-child($) > p", "message");
        return model;
    }
}

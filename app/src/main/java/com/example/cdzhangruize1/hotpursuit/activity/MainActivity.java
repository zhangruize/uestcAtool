package com.example.cdzhangruize1.hotpursuit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.engine.Engine;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseScraperModel model = new BaseScraperModel();
        model.links.add("http://www.new1.uestc.edu.cn/?n=UestcNews.Front.Category.Page&CatId=67");
        model.xpathMaps.put("#Degas_news_list > ul > li:nth-child($) > h3 > a", "title");
        model.xpathMaps.put("#Degas_news_list > ul > li:nth-child($) > p", "message");
        Engine engine = new Engine();
        engine.setModel(model);
        engine.setmLoadCallback(new Engine.LoadCallback() {
            @Override
            public void onSucceed(ArrayList<HashMap<String, String>> data) {
                Log.v("dd", "dd");
            }

            @Override
            public void onFailed() {

            }
        });
        engine.load();
    }
}

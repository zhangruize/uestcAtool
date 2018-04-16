package com.example.cdzhangruize1.hotpursuit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.adapter.SettingAdapter;
import com.example.cdzhangruize1.hotpursuit.callback.ScraperModelListCallback;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.utils.ScraperModelUtils;

import java.util.ArrayList;

/**
 * todo 需要考虑网络增加了新模型时，页面上的提醒，以及保存正确的订阅设置。
 */
public class SettingsActivity extends AppCompatActivity {
    RecyclerView mModelList;
    LinearLayoutManager mManager;
    SettingAdapter mAdapter;
    ScraperModelListCallback mCallback = new ScraperModelListCallback() {
        @Override
        public void onSucceed(ArrayList<BaseScraperModel> data) {
            mAdapter.dispatchData(data);
        }

        @Override
        public void onFailed(Exception e) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mModelList = findViewById(R.id.modelList);
        mAdapter = new SettingAdapter();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        ScraperModelUtils.getInstance(this).getRemoteScraperModels(mCallback);

        mModelList.setAdapter(mAdapter);
        mModelList.setLayoutManager(mManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScraperModelUtils.getInstance(this).saveScraperModels(mAdapter.getData());
    }
}

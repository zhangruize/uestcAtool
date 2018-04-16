package com.example.cdzhangruize1.hotpursuit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.adapter.HomeFragmentAdapter;
import com.example.cdzhangruize1.hotpursuit.callback.ScraperModelListCallback;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.utils.ScraperModelUtils;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_SETTING_ACTIVITY = 1;
    private View noModelLayout;
    private HomeFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    ScraperModelListCallback mCallback = new ScraperModelListCallback() {
        @Override
        public void onSucceed(ArrayList<BaseScraperModel> data) {
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if (!data.get(i).isSubscribe()) {
                        data.remove(i);
                        i--;
                    }
                }
            }
            if (data == null || data.size() == 0) {
                noModelLayout.setVisibility(View.VISIBLE);
            } else {
                noModelLayout.setVisibility(View.GONE);
            }
            resetTabs(data);
            mSectionsPagerAdapter.dispatchData(data);
        }

        @Override
        public void onFailed(Exception e) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        fetchModels();
        checkVersion();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new HomeFragmentAdapter(this, getSupportFragmentManager());

        mViewPager = findViewById(R.id.homeViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.homeTabLayout);
        noModelLayout = findViewById(R.id.noModelLayout);
        noModelLayout.setVisibility(View.GONE);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void fetchModels() {
        ArrayList<BaseScraperModel> localData = ScraperModelUtils.getInstance(this).getLocalScraperModels();
        if (localData == null) {
            ScraperModelUtils.getInstance(this).getRemoteScraperModels(mCallback);
        } else {
            mCallback.onSucceed(localData);
        }
    }

    private void checkVersion() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//todo 未来还有反馈、检查更新
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            toSettingActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSettingActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_SETTING_ACTIVITY);
    }

    private void resetTabs(ArrayList<BaseScraperModel> data) {
        tabLayout.removeAllTabs();
        if (data != null) {
            for (BaseScraperModel model : data) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(model.getName());
                tabLayout.addTab(tab);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTING_ACTIVITY:
                fetchModels();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noModelLayout:
                toSettingActivity();
                break;
        }
    }
}

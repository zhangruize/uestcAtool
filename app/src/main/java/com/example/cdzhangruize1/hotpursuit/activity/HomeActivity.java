package com.example.cdzhangruize1.hotpursuit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.adapter.HomeFragmentAdapter;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.example.cdzhangruize1.hotpursuit.utils.ScraperModelUtils;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ScraperModelUtils.Callback {

    private HomeFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        ArrayList<BaseScraperModel> localData = ScraperModelUtils.getInstance(this).getLocalScraperModels();
        if (localData == null) {
            ScraperModelUtils.getInstance(this).getRemoteScraperModels(this);
        } else {
            onSucceed(localData);
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new HomeFragmentAdapter(this, getSupportFragmentManager());

        mViewPager = findViewById(R.id.homeViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.homeTabLayout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
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
        startActivity(intent);
    }

    @Override
    public void onSucceed(ArrayList<BaseScraperModel> data) {
        mSectionsPagerAdapter.dispatchData(data);
    }

    @Override
    public void onFailed() {

    }
}

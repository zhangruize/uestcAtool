package com.example.cdzhangruize1.hotpursuit.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.cdzhangruize1.hotpursuit.fragment.BasicFragment;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import java.util.ArrayList;

public class HomeFragmentAdapter extends FragmentPagerAdapter {//todo 未来考虑回收机制，还有内存泄漏检测
    private String mName;
    private Context context;
    private ArrayList<BaseScraperModel> mData;

    public HomeFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        mName = hashCode() + "";
    }

    @Override
    public Fragment getItem(int position) {
        BasicFragment fragment = (BasicFragment) Fragment.instantiate(context, BasicFragment.class.getName());
        fragment.setModel(mData.get(position),context);
        return fragment;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    public void dispatchData(ArrayList<BaseScraperModel> data) {
        mData = data;
        notifyDataSetChanged();
    }
}

package com.example.cdzhangruize1.hotpursuit.engine;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.model.BaseListStyle;
import com.example.cdzhangruize1.hotpursuit.model.ListStyle1;

import java.util.ArrayList;
import java.util.HashMap;

public class ListEngine {
    private int mCurrentStyleIndex = 0;
    private ArrayList<BaseListStyle> mStyles = new ArrayList<>();
    private static ListEngine sInstance;

    public static ListEngine getInstance() {
        if (sInstance == null) {
            sInstance = new ListEngine();
        }
        return sInstance;
    }

    private ListEngine() {
        init();
    }

    private void init() {
        mStyles.add(new ListStyle1());
    }

    public RecyclerView generateView(ArrayList<HashMap<String, String>> data, Context context) {
        RecyclerView list = new RecyclerView(context);
        BasicListAdapter adapter = new BasicListAdapter(nextStyle());
        adapter.setData(data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);
        return list;
    }

    private BaseListStyle nextStyle() {
        mCurrentStyleIndex %= mStyles.size();
        return mStyles.get(mCurrentStyleIndex++);
    }
}

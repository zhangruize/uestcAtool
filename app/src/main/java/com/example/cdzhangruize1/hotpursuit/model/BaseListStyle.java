package com.example.cdzhangruize1.hotpursuit.model;

import java.util.HashMap;

abstract public class BaseListStyle {
    private int mLayoutId;
    private HashMap<String, Integer> mField2Id = new HashMap<>();

    BaseListStyle() {
        init();
    }

    abstract void init();

    public String[] getSupportFields() {
        if (mField2Id != null) {
            int size = mField2Id.size();
            if (size > 0) {
                String[] r = new String[size];
                mField2Id.keySet().toArray(r);
                return r;
            }
        }
        return null;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public int getId(String key) {
        return mField2Id.get(key);
    }

    protected void setLayoutId(int id) {
        mLayoutId = id;
    }

    protected void addFields(String key, int id) {
        mField2Id.put(key, id);
    }
}

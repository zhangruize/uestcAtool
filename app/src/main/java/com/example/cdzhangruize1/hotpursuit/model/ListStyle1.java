package com.example.cdzhangruize1.hotpursuit.model;

import com.example.cdzhangruize1.hotpursuit.R;

public class ListStyle1 extends BaseListStyle {
    @Override
    void init() {
        setLayoutId(R.layout.list_style1);
        addFields("title", R.id.title);
        addFields("pic", R.id.pic);
        addFields("message", R.id.message);
    }
}

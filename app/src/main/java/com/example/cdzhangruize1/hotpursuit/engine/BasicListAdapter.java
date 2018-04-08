package com.example.cdzhangruize1.hotpursuit.engine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cdzhangruize1.hotpursuit.model.BaseListStyle;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.BasicViewHolder> {
    private ArrayList<HashMap<String, String>> mData;
    private BaseListStyle mStyle;

    public BasicListAdapter(BaseListStyle style) {//应当确保此style和将要传入的mData兼容
        this.mStyle = style;
    }

    public void setData(ArrayList<HashMap<String, String>> data) {
        this.mData = data;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return mData;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BasicViewHolder(LayoutInflater.from(parent.getContext()).inflate(mStyle.getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        HashMap<String, String> itemData = mData.get(position);
        for (String field : mStyle.getSupportFields()) {
            View view = holder.itemView.findViewById(mStyle.getId(field));
            String value = itemData.get(field);
            if (value != null) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(value);
                } else if (view instanceof ImageView) {
                    Glide.with(holder.itemView).load(value).into((ImageView) view);
                }
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class BasicViewHolder extends RecyclerView.ViewHolder {
        BasicViewHolder(View itemView) {
            super(itemView);
        }
    }

}

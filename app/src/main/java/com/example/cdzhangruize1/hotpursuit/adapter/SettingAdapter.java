package com.example.cdzhangruize1.hotpursuit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    private ArrayList<BaseScraperModel> mData;

    public void dispatchData(ArrayList<BaseScraperModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public ArrayList<BaseScraperModel> getData() {
        return mData;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        return new SettingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        BaseScraperModel model = mData.get(position);
        holder.switchView.setChecked(model.isSubscribe());
        holder.nameView.setText(model.getName());
        if (model.getIcon() != null) {
            Glide.with(holder.iconView).load(model.getIcon()).into(holder.iconView);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class SettingViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        SwitchCompat switchView;
        TextView nameView;
        ImageView iconView;

        SettingViewHolder(View itemView) {
            super(itemView);
            switchView = itemView.findViewById(R.id.settingSwitch);
            switchView.setOnCheckedChangeListener(this);
            nameView = itemView.findViewById(R.id.settingItemName);
            iconView = itemView.findViewById(R.id.settingItemIcon);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
            BaseScraperModel model = mData.get(position);
            model.setSubscribe(isChecked);
        }
    }
}

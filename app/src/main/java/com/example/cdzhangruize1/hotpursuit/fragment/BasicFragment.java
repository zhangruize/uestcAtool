package com.example.cdzhangruize1.hotpursuit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cdzhangruize1.hotpursuit.R;
import com.example.cdzhangruize1.hotpursuit.engine.ListEngine;
import com.example.cdzhangruize1.hotpursuit.engine.LoadTask;
import com.example.cdzhangruize1.hotpursuit.engine.WebEngine;
import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

/**
 * 支持下拉刷新
 */
public class BasicFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, WebEngine.LoadCallback2 {
    View mRoot;
    View mFailedLayout;
    SwipeRefreshLayout mSwipeLayout;
    ViewGroup mListContainer;
    LoadTask mTask;

    public BasicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.basic_fragment, container, false);
            mSwipeLayout = mRoot.findViewById(R.id.basicFragmentContainer);
            mFailedLayout = mRoot.findViewById(R.id.basicFragmentFailedLayout);
            mListContainer = mRoot.findViewById(R.id.recyclerViewContainer);
            mSwipeLayout.setOnRefreshListener(this);
            mFailedLayout.setVisibility(View.GONE);
        }
        return mRoot;
    }

    @Override
    public void onRefresh() {
        if (mTask != null) {
            WebEngine.getInstance().load(mTask);
        }
    }

    public void setModel(BaseScraperModel model, Context context) {
        if (model != null) {
            if (mTask != null) {
                mTask.setModel(model);
            } else {
                mTask = new LoadTask(model, this, ListEngine.getInstance(), context);
            }
        }
    }

    @Override
    public void onSucceed(RecyclerView view) {
        mSwipeLayout.setRefreshing(false);
        mFailedLayout.setVisibility(View.GONE);
        if (view != null) {
            if (view.getParent() == null) {
                mListContainer.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                view.getAdapter().notifyDataSetChanged();
            }
        } else {
            //todo error
        }
    }

    @Override
    public void onFailed() {
        mSwipeLayout.setRefreshing(false);
        mFailedLayout.setVisibility(View.VISIBLE);
    }
}

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
    RecyclerView mListView;

    public BasicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(true);
        if (mTask != null) {
            WebEngine.getInstance().load(mTask);
        }
    }

    public void setModel(BaseScraperModel model, Context context) {
        if (model != null) {
            if (mTask != null) {
                if (!mTask.getModel().getName().equals(model.getName())) {//如果新的模型和上一个不一样
                    if (mListView != null) {//隐藏列表。防止其他问题
                        mListView.setVisibility(View.GONE);
                    }
                    mTask.setModel(model);
                    onRefresh();
                } else {
                    mSwipeLayout.setVisibility(View.VISIBLE);//如果新的模型和之前的模型相同。则确保可见性。
                }
            } else {
                mTask = new LoadTask(model, this, ListEngine.getInstance(), context);
            }
        }
    }

    @Override
    public void onSucceed(RecyclerView view) {
        mListView = view;
        mSwipeLayout.setRefreshing(false);
        mFailedLayout.setVisibility(View.GONE);
        if (view != null) {
            if (view.getParent() == null) {//如果还没有添加到容器里
                mListContainer.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {//已经被添加到容器里，此时更新列表数据
                view.getAdapter().notifyDataSetChanged();
            }
            mListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailed() {
        mSwipeLayout.setRefreshing(false);
        mFailedLayout.setVisibility(View.VISIBLE);
    }

    public void reset() {
        mSwipeLayout.setVisibility(View.GONE);
    }
}

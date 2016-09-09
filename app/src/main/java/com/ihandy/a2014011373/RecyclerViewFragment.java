package com.ihandy.a2014011373;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumy on 16/8/27.
 * Every tab of fragment
 */
public class RecyclerViewFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    public NewsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String categoryKey;
    private Context mContext;
    private List<News> mContentItems = new ArrayList<>();
    public SwipyRefreshLayout mSwipyRefreshLayout;

    public static RecyclerViewFragment newInstance(String key) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.categoryKey = key;
        return fragment;
    }

    public void setContentItems(List<News> contentItems){
        this.mContentItems = contentItems;
    }

    public void setContext(Context context){
        this.mContext = context;
    }

    public void notifyDataChange(){
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public List<News> getNewsList(){
        return mContentItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        if (mContentItems.size() == 0){
            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        mAdapter = new NewsRecyclerViewAdapter(mContentItems,mContext);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (PresenterSingleton.getInstance(mContext).checkNetworkConnection() == false){
            mSwipyRefreshLayout.setRefreshing(false);
            Toast.makeText(mContext,"Network is not available.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (direction == SwipyRefreshLayoutDirection.TOP){
            PresenterSingleton.getInstance(mContext).updateNews(categoryKey,
                    mContentItems,this);
        }else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
            PresenterSingleton.getInstance(mContext).getPreviousNews(categoryKey,
                    mContentItems,this);
        }
    }
}

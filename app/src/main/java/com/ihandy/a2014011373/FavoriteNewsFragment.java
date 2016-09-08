package com.ihandy.a2014011373;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteNewsFragment extends Fragment {
    FavoriteNewsAdapter mAdapter;
    RecyclerView mRecyclerView;
    Context mContext;

    public static FavoriteNewsFragment newInstance(Context context){
        FavoriteNewsFragment fragment = new FavoriteNewsFragment();
        fragment.mContext = context;
        return fragment;
    }

    public FavoriteNewsAdapter getmAdapter(){
        return mAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewForFavoriteNews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FavoriteNewsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_news, container, false);
    }
}

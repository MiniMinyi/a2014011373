package com.ihandy.a2014011373;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FavoriteNews extends AppCompatActivity {
    public static List<News> favoriteNews = new ArrayList<News>();
    private static FavoriteNewsAdapter mAdapter = null;
    private static FavoriteNewsFragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (fragment == null)
            fragment = FavoriteNewsFragment.newInstance(this);
        mAdapter = fragment.getmAdapter();
    }

    public static void notifyDataChange(){
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }
}

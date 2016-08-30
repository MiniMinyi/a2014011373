package com.ihandy.a2014011373;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;

import java.util.ArrayList;

/**
 * Created by liumy on 16/8/27.
 */
public class MainActivity extends DrawerActivity{
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    public static ArrayList<Pair<String,RecyclerViewFragment>> tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material_view_pager);

        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        setupAdapter();

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * @TODO change tab title and coresponding RecyclerViewFragment in this function
     */
    private void setupAdapter(){
        tabList = new ArrayList<>();
        tabList.add(new Pair<>("Bussiness",RecyclerViewFragment.newInstance(1)));
        tabList.add(new Pair<>("Sports",RecyclerViewFragment.newInstance(2)));
        tabList.add(new Pair<>("Entertainment",RecyclerViewFragment.newInstance(3)));
        tabList.add(new Pair<>("World",RecyclerViewFragment.newInstance(4)));
        NewsPagerAdapter mNewsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.getViewPager().setAdapter(mNewsPagerAdapter);
        mViewPager.setMaterialViewPagerListener(mNewsPagerAdapter.newMaterialViewPagerListener());
    }

    public Drawable createDrawableFromResource(int drawableId){
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),drawableId);
        return drawable;
    }
}

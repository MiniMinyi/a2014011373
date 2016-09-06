package com.ihandy.a2014011373;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.ihandy.a2014011373.manage_category.ManageCategoryActivity;

import java.util.ArrayList;

/**
 * Created by liumy on 16/8/27.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    public static final int START_MANAGE_CATEGORY = 1;
    private MaterialViewPager mViewPager;
    public static NewsPagerAdapter mNewsPagerAdapter;
    private Toolbar toolbar;
    public static ArrayList<CategoryTab> tabList = null;
    public static RequestQueueSingleton mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = RequestQueueSingleton.getInstance(this);
        setUpViewPager();
    }

    private void setUpViewPager(){
        setContentView(R.layout.activity_main_material_view_pager);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();
        toolbar.setTitle("");
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
                    if (!PresenterSingleton.getInstance(MainActivity.this).checkNetworkConnection()){
                        Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Network connection is available.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //TODO change to load tablist from disk later
    private void initTabList(){
        tabList = new ArrayList<>();
        int id = 0;
        tabList.add(new CategoryTab("bussiness","Bussiness",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("elections","Elections",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("entertainment","Entertainment",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("health","Health",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("national","India",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("science","Science",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("sports","Sports",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("technology_science","Technology",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("top_stories","Top Stories",
                RecyclerViewFragment.newInstance(),id));
        ++id;
        tabList.add(new CategoryTab("world","World",
                RecyclerViewFragment.newInstance(),id));
        ++id;
    }

    private void setupAdapter(){
        if (tabList == null){
            //initTabList();
            tabList = new ArrayList<CategoryTab>();
            PresenterSingleton.getInstance(this).initCurrentCategories();
        }
        if (mNewsPagerAdapter != null){
            mNewsPagerAdapter.notifyDataSetChanged();
        }
        mNewsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.getViewPager().setAdapter(mNewsPagerAdapter);
        mViewPager.setMaterialViewPagerListener(mNewsPagerAdapter.newMaterialViewPagerListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

    }

    public Drawable createDrawableFromResource(int drawableId){
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),drawableId);
        return drawable;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("main activity","result");
        if (requestCode == START_MANAGE_CATEGORY){
            mNewsPagerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_manage_category) {
            Intent intent = new Intent(MainActivity.this,ManageCategoryActivity.class);
            startActivityForResult(intent,START_MANAGE_CATEGORY);
        } else if (id == R.id.nav_about_me) {
            Intent intent = new Intent(this,AboutMe.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

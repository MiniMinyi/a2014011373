package com.ihandy.a2014011373;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.ihandy.a2014011373.database.Category;
import com.ihandy.a2014011373.database.NewsForSave;
import com.ihandy.a2014011373.manage_category.ManageCategoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumy on 16/8/27.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    public static final int START_MANAGE_CATEGORY = 1;
    public static final int START_FAVORITE_NEWS = 2;
    private MaterialViewPager mViewPager;
    public static NewsPagerAdapter mNewsPagerAdapter;
    private Toolbar toolbar;
    public static ArrayList<CategoryTab> tabList = null;
    public static ArrayList<CategoryTab> unwatchedTabList = null;
    public static RequestQueueSingleton mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < this.databaseList().length; ++ i)
            Log.d("database list",this.databaseList()[i]);
        mRequestQueue = RequestQueueSingleton.getInstance(this);

        setContentView(R.layout.activity_main_material_view_pager);

        //set up drawer
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

        setUpViewPager();
        //queryDatabase();
    }

    private void queryDatabase(){
        Boolean isWatched = true;
        List<Category> categories = new Select().from(Category.class).where("isWatched = ?",true).execute();
        List<NewsForSave> news = new Select().from(NewsForSave.class).execute();

        Log.v("queryDatabase",String.format("categories size = %d",categories.size()));
        Log.v("queryDatabase",String.format("news count = %d",news.size()));
    }

    private void setUpViewPager(){
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        unwatchedTabList = new ArrayList<>();
        tabList = new ArrayList<>();
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

    private void setupAdapter(){
        if (!setUpFromDatabase())
            PresenterSingleton.getInstance(this).initCurrentCategories();

        mNewsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.getViewPager().setAdapter(mNewsPagerAdapter);
        mViewPager.setMaterialViewPagerListener(mNewsPagerAdapter.newMaterialViewPagerListener());
    }

    private boolean setUpTabList(ArrayList<CategoryTab> tablist, boolean isWatched, int offset){
        List<Category> categories = new Select().from(Category.class).where("isWatched = ?",isWatched).orderBy("showOrder").execute();
        if (categories != null && !categories.isEmpty()){
            for (int i = 0; i < categories.size(); ++ i){
                Category savedCategory = categories.get(i);
                RecyclerViewFragment fragment = RecyclerViewFragment.newInstance(savedCategory.json_key);
                fragment.setContext(this);
                CategoryTab category = new CategoryTab(savedCategory.json_key,
                        savedCategory.name,
                        fragment,
                        savedCategory.order+offset);
                List<News> newsList = new ArrayList<>();
                List<NewsForSave> savedNewsList = new Select().from(NewsForSave.class).
                        where("CategoryName = ?",savedCategory.name).orderBy("news_id DESC").execute();
                for (int j = 0; j < savedNewsList.size(); ++ j){
                    News news = new News();
                    NewsForSave savedNews = savedNewsList.get(j);
                    news.img_url = savedNews.img_url;
                    news.source_url = savedNews.source_url;
                    news.title = savedNews.title;
                    news.category = savedNews.category_name;
                    news.news_id = savedNews.news_id;
                    news.saved = savedNews.isSaved;
                    news.liked = savedNews.isLiked;
                    newsList.add(news);
                }
                fragment.setContentItems(newsList);
                tablist.add(category);
            }
            return true;
        }
        return false;
    }

    private boolean setUpFromDatabase(){
        boolean flag = false;
        if (setUpTabList(tabList,true,0))
            flag = true;
        setUpTabList(unwatchedTabList,false,tabList.size());
        return flag;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            FavoriteNews.notifyDataChange();
            Intent intent = new Intent(MainActivity.this,FavoriteNews.class);
            startActivityForResult(intent,START_FAVORITE_NEWS);
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

    @Override
    protected void onStop() {
        super.onStop();
        saveToDatabase();
        //queryDatabase();
    }

    private void saveCategoryList(ArrayList<CategoryTab> list, boolean isWatch){
        ActiveAndroid.beginTransaction();
        try{
            for (int i = 0; i < list.size(); ++ i){
                CategoryTab tab = list.get(i);
                Category category = new Select().from(Category.class).where("Key = ?",tab.getJsonkey()).executeSingle();
                if (category != null){
                    category.order = i;
                    category.isWatched = isWatch;
                    category.save();
                }else{
                    category = new Category();
                    category.name = tab.getTitle();
                    category.json_key = tab.getJsonkey();
                    category.isWatched = isWatch;
                    category.order = i;
                    category.save();
                }

                List<News> newsList = tab.getFragment().getNewsList();
                for (int j = 0; j < newsList.size(); ++ j){
                    News news = newsList.get(j);
                    NewsForSave savedNews = new Select().
                            from(NewsForSave.class).
                            where("CategoryName = ?",category.name).
                            where("news_id = ?",news.news_id).executeSingle();
                    if (savedNews != null) {
                        savedNews.isLiked = news.liked;
                        savedNews.isSaved = news.saved;
                        savedNews.save();
                        continue;
                    }
                    NewsForSave save_news = new NewsForSave();
                    save_news.category = category;
                    save_news.category_name = category.name;
                    save_news.img_url = news.img_url;
                    save_news.source_url = news.source_url;
                    save_news.news_id = news.news_id;
                    save_news.title = news.title;
                    save_news.isSaved = news.saved;
                    save_news.isLiked = news.liked;
                    save_news.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        }catch (Exception e){
            Log.v("saveInDatabase",e.toString());
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    private void saveToDatabase(){
        ActiveAndroid.getDatabase();
        saveCategoryList(tabList, true);
        saveCategoryList(unwatchedTabList,false);
    }
}

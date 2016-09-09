package com.ihandy.a2014011373.manage_category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.ihandy.a2014011373.PresenterSingleton;
import com.ihandy.a2014011373.R;

import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {
    public static ListFragment mListFragment, unwatchedListFragment;
    public static ItemAdapter watchedAdapter, unwatchedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        //set up tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createListFragment();
    }

    private void createListFragment(){
        watchedAdapter = new ItemAdapter(R.layout.list_item_watched_category, R.id.image, false, true, this);
        unwatchedAdapter = new ItemAdapter(R.layout.list_item_unwatched_category, R.id.image, false, false, this);
        watchedAdapter.anotherAdapter = unwatchedAdapter;
        unwatchedAdapter.anotherAdapter = watchedAdapter;
        fragmentChanged();
    }

    public void fragmentChanged() {
        watchedAdapter.count = 0;
        unwatchedAdapter.count = 0;
        mListFragment = ListFragment.newInstance(watchedAdapter, true);
        unwatchedListFragment = ListFragment.newInstance(unwatchedAdapter, false);
        showFragment(mListFragment, R.id.container);
        showFragment(unwatchedListFragment, R.id.container2);
    }

    private void showFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, "fragment").commit();
    }

    @Override
    public boolean onSupportNavigateUp(){
        setResult(RESULT_OK);
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_get_categories:
                getCategories();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCategories(){
        PresenterSingleton.getInstance(this).initCurrentCategories();
    }
}

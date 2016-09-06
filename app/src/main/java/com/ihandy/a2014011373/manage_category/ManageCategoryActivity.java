package com.ihandy.a2014011373.manage_category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ihandy.a2014011373.R;

import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {
    private ListFragment mListFragment, unwatchedListFragment;
    public static ItemAdapter watchedAdapter, unwatchedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        //set up tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        watchedAdapter = new ItemAdapter(R.layout.list_item_watched_category, R.id.image, false, true, this);
        unwatchedAdapter = new ItemAdapter(R.layout.list_item_unwatched_category, R.id.image, false, false, this);
        watchedAdapter.anotherAdapter = unwatchedAdapter;
        unwatchedAdapter.anotherAdapter = watchedAdapter;
        mListFragment = ListFragment.newInstance(watchedAdapter);
        unwatchedListFragment = ListFragment.newInstance(unwatchedAdapter);
        showFragment(mListFragment, R.id.container);
        showFragment(unwatchedListFragment, R.id.container2);
    }

    public void fragmentChanged() {
        watchedAdapter.count = 0;
        unwatchedAdapter.count = 0;
        mListFragment = ListFragment.newInstance(watchedAdapter);
        unwatchedListFragment = ListFragment.newInstance(unwatchedAdapter);
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

}

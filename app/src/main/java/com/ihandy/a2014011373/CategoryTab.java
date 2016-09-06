package com.ihandy.a2014011373;

import android.support.v4.app.Fragment;

/**
 * Created by liumy on 16/9/2.
 */
public class CategoryTab {
    private String jsonkey;
    private String title;
    private Fragment fragment;
    private long mId;

    public CategoryTab(String key, String title, Fragment fragment, long id){
        this.jsonkey = key;
        this.title = title;
        this.fragment = fragment;
        this.mId = id;
    }
    public void setId(long id){
        this.mId = id;
    }
    public long getCustomId(){
        return this.mId;
    }

    public void setJsonkey(String key){
        jsonkey = key;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setFragment(RecyclerViewFragment fragment){
        this.fragment = fragment;
    }

    public String getJsonkey(){
        return jsonkey;
    }

    public String getTitle(){
        return title;
    }

    public Fragment getFragment(){
        return fragment;
    }
}

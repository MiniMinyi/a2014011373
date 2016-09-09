package com.ihandy.a2014011373;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ihandy.a2014011373.manage_category.ManageCategoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liumy on 16/9/2.
 */
public class PresenterSingleton {
    final static public String NEWS_SITE_FOR_CATEGORIES = "http://assignment.crazz.cn/news/en/category?timestamp=%d";
    final static public String URL_FOR_NEWEST_NEWS = "http://assignment.crazz.cn/news/query?locale=en&category=%s";
    final static public String URL_FOR_OLDER_NEWS = "http://assignment.crazz.cn/news/query?locale=en&category=%s&max_news_id=%d";
    private static PresenterSingleton mInstance;
    private static Context mContext;

    private PresenterSingleton(Context context){
        mContext = context;
    }

    public static synchronized PresenterSingleton getInstance(Context context){
        if (context != null)
            mContext = context;
        if (mInstance == null){
            mInstance = new PresenterSingleton(context);
        }
        return mInstance;
    }

    //Directly change MainActivity.tabList
    public void initCurrentCategories(){
        String url = String.format(NEWS_SITE_FOR_CATEGORIES,
                System.currentTimeMillis());
        if (!checkNetworkConnection()){
            Toast.makeText(mContext,"network connection is unavailable",Toast.LENGTH_SHORT).show();
            return ;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject data = response.getJSONObject("data");
                                    JSONObject categories = data.getJSONObject("categories");
                                    Iterator<?> it = categories.keys();
                                    if (MainActivity.tabList != null){
                                        MainActivity.tabList.clear();
                                    }else {
                                        MainActivity.tabList = new ArrayList<>();
                                    }
                                    int id = 0;
                                    while (it.hasNext()){
                                        String key = it.next().toString();
                                        String title = categories.get(key).toString();
                                        RecyclerViewFragment fragment = RecyclerViewFragment.newInstance(key);
                                        fragment.setContext(mContext);
                                        List<News> newsList = new ArrayList<>();
                                        getListOfNews(key,newsList,fragment);
                                        MainActivity.tabList.add(new CategoryTab(key,title,
                                                fragment,id));
                                        ++ id;
                                    }
                                    if (ManageCategoryActivity.mListFragment != null){
                                        ManageCategoryActivity.mListFragment.mListAdapter.notifyDataSetChanged();
                                    }
                                    if (MainActivity.mNewsPagerAdapter != null){
                                        MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                                    }

                                }catch (JSONException e){
                                    Log.e("JSONException",e.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("getCurrentCategories",error.toString());
                                Toast.makeText(mContext,"No Network connection.",Toast.LENGTH_SHORT).show();
                            }
                        });
        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    /**
     *
     * @param newsArray
     * @param newsList
     * @return new added news number
     * @throws JSONException
     */
    private int setNewslistFromJSONArray(JSONArray newsArray, List<News> newsList, RecyclerViewFragment fragment) throws JSONException{
        int newlyAddNewsCount = 0;
        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject news = newsArray.getJSONObject(i);
            News oneNews = new News();
            JSONArray imgs = news.optJSONArray("imgs");
            JSONObject firstImg = null;
            if (imgs != null) firstImg = imgs.getJSONObject(0);

            try {
                JSONObject source = news.getJSONObject("source");
                oneNews.source_name = source.getString("name");
                oneNews.source_url = source.getString("url");
            }catch (JSONException e){
                Log.e("setNewsListFromJSOArray",e.toString());
            }

            oneNews.title = news.getString("title");
            oneNews.locale_category = news.getString("locale_category");
            oneNews.news_id = news.getLong("news_id");
            //if the added news isn't newest than original newest news
            if (i == newlyAddNewsCount && newsList.size() > newlyAddNewsCount &&
                    oneNews.news_id <= newsList.get(newlyAddNewsCount).news_id){
                return newlyAddNewsCount;
            }
            oneNews.origin = news.getString("origin");
            oneNews.category = news.getString("category");
            oneNews.country = news.getString("country");
            oneNews.fetched_time = news.getLong("fetched_time");
            try {
                oneNews.updated_time = news.getString("updated_time");
            }catch (JSONException e){
                Log.d("getUpdatedTime",e.toString());
            }
            if (firstImg != null)
                oneNews.img_url = firstImg.getString("url");
            newsList.add(newlyAddNewsCount,oneNews);
            if (fragment.mAdapter != null) {
                if (newlyAddNewsCount == 0) {
                    fragment.mAdapter.notifyItemRemoved(0);
                    fragment.mAdapter.notifyItemInserted(0);
                    fragment.mAdapter.notifyItemRangeChanged(0, 1);
                } else {
                    fragment.mAdapter.notifyItemInserted(newlyAddNewsCount);
                    fragment.mAdapter.notifyItemRangeChanged(newlyAddNewsCount, 1);
                }
            }
            ++newlyAddNewsCount;
            if (newlyAddNewsCount % 6 == 5) {
                MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                Log.v("setNewList",String.format("From category %s, newly add news count %d",newsList.get(0).category,newsList.size()));
            }
        }
        return newlyAddNewsCount;
    }

    private int getPreviousNewsFromJSONArray(JSONArray newsArray, List<News> newsList) throws JSONException{
        int newlyAddNewsCount = 0;
        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject news = newsArray.getJSONObject(i);
            News oneNews = new News();
            JSONArray imgs = news.optJSONArray("imgs");
            JSONObject firstImg = null;
            if (imgs != null) firstImg = imgs.getJSONObject(0);

            try {
                JSONObject source = news.getJSONObject("source");
                oneNews.source_name = source.getString("name");
                oneNews.source_url = source.getString("url");
            }catch (JSONException e){
                Log.e("setNewsListFromJSOArray",e.toString());
                continue;
            }

            oneNews.title = news.getString("title");
            oneNews.locale_category = news.getString("locale_category");
            oneNews.news_id = news.getLong("news_id");
            oneNews.origin = news.getString("origin");
            oneNews.category = news.getString("category");
            oneNews.country = news.getString("country");
            oneNews.fetched_time = news.getLong("fetched_time");
            try {
                oneNews.updated_time = news.getString("updated_time");
            }catch (JSONException e){
                Log.d("getUpdatedTime",e.toString());
            }
            if (firstImg != null)
                oneNews.img_url = firstImg.getString("url");
            newsList.add(oneNews);
            ++newlyAddNewsCount;
        }
        return newlyAddNewsCount;
    }

    private void getListOfNews(String categoryKey, final List<News> newsList, final RecyclerViewFragment fragment){
        String url = String.format(URL_FOR_NEWEST_NEWS,categoryKey);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray newsArray = data.getJSONArray("news");
                                    fragment.setContentItems(newsList);
                                    setNewslistFromJSONArray(newsArray,newsList,fragment);
                                    if (MainActivity.mNewsPagerAdapter != null){
                                        MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                                    }

                                }catch (JSONException e){
                                    Log.e("getListOfNews",e.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (fragment.mSwipyRefreshLayout != null) {
                                    fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    Toast.makeText(fragment.getContext(), "Couldn't update news.", Toast.LENGTH_SHORT).show();
                                }
                                Log.e("getListOfNews",error.toString());
                            }
                        });
        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    public void updateNews(String categoryKey, final List<News> newsList, final RecyclerViewFragment fragment){
        String url = String.format(URL_FOR_NEWEST_NEWS,categoryKey);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            int newCount = 0;
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray newsArray = data.getJSONArray("news");
                                    newCount = setNewslistFromJSONArray(newsArray,newsList,fragment);

                                    if (newCount == 0) {
                                        fragment.mSwipyRefreshLayout.setRefreshing(false);
                                        Toast.makeText(fragment.getContext(), "No news update.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(fragment.getContext(), String.format("%d news updated", newCount),
                                                Toast.LENGTH_SHORT).show();
                                        fragment.notifyDataChange();
                                        fragment.mSwipyRefreshLayout.setRefreshing(false);
                                        if (MainActivity.mNewsPagerAdapter != null){
                                            MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }catch (JSONException e){
                                    Log.e("getListOfNews",e.toString());
                                    if (newCount == 0) {
                                        fragment.mSwipyRefreshLayout.setRefreshing(false);
                                        Toast.makeText(fragment.getContext(), "No news update.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(fragment.getContext(), String.format("%d news updated", newCount),
                                                Toast.LENGTH_SHORT).show();
                                        fragment.notifyDataChange();
                                        fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (fragment.mSwipyRefreshLayout != null) {
                                    fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    Toast.makeText(fragment.getContext(), "Couldn't update news.", Toast.LENGTH_SHORT).show();
                                }
                                Log.e("getListOfNews",error.toString());
                            }
                        });
        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    public void getPreviousNews(String categoryKey,final List<News> newsList, final RecyclerViewFragment fragment){
        String url = String.format(URL_FOR_OLDER_NEWS,categoryKey,newsList.get(newsList.size()-1).news_id - 1);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray newsArray = data.getJSONArray("news");
                                    int addCount = getPreviousNewsFromJSONArray(newsArray,newsList);
                                    if (addCount == 0){
                                        Toast.makeText(fragment.getContext(),"No older news",Toast.LENGTH_SHORT).show();
                                        fragment.mSwipyRefreshLayout.setRefreshing(false);
                                        return;
                                    }
                                    Toast.makeText(fragment.getContext(),String.format("Fetch %d previous news.",addCount),Toast.LENGTH_SHORT).show();
                                    fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    fragment.notifyDataChange();
                                    //MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                                }catch (JSONException e){
                                    Toast.makeText(fragment.getContext(),"No older news",Toast.LENGTH_SHORT).show();
                                    fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    Log.e("getListOfNews",e.toString());
                                    return;
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (fragment.mSwipyRefreshLayout != null) {
                                    fragment.mSwipyRefreshLayout.setRefreshing(false);
                                    Toast.makeText(fragment.getContext(), "Couldn't update news.", Toast.LENGTH_SHORT).show();
                                }
                                Log.e("getListOfNews",error.toString());
                            }
                        });
        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else{
            return false;
        }
    }

}

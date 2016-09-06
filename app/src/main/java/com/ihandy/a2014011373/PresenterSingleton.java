package com.ihandy.a2014011373;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
                                        MainActivity.tabList = new ArrayList<CategoryTab>();
                                    }
                                    int id = 0;
                                    while (it.hasNext()){
                                        String key = it.next().toString();
                                        String title = categories.get(key).toString();
                                        RecyclerViewFragment fragment = RecyclerViewFragment.newInstance();
                                        fragment.setContext(mContext);
                                        List<News> newsList = new ArrayList<News>();
                                        getListOfNews(key,newsList,fragment);
                                        MainActivity.tabList.add(new CategoryTab(key,title,
                                                fragment,id));
                                        ++ id;
                                    }
                                    if (MainActivity.mNewsPagerAdapter != null){
                                        MainActivity.mNewsPagerAdapter.notifyDataSetChanged();
                                    }
                                    if (ManageCategoryActivity.mListFragment != null){
                                        ManageCategoryActivity.mListFragment.mListAdapter.notifyDataSetChanged();
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
                            }
                        });
        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
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
                                    if (data == null){
                                        return;
                                    }
                                    JSONArray newsArray = data.getJSONArray("news");
                                    for (int i = 0; i < newsArray.length(); i++) {
                                        JSONObject news = newsArray.getJSONObject(i);
                                        JSONArray imgs = news.optJSONArray("imgs");
                                        JSONObject firstImg = null;
                                        if (imgs != null) firstImg = imgs.getJSONObject(0);
                                        JSONObject source = news.getJSONObject("source");
                                        News oneNews = new News();
                                        oneNews.title = news.getString("title");
                                        oneNews.locale_category = news.getString("locale_category");
                                        oneNews.news_id = news.getLong("news_id");
                                        oneNews.origin = news.getString("origin");
                                        oneNews.category = news.getString("category");
                                        oneNews.country = news.getString("country");
                                        oneNews.fetched_time = news.getLong("fetched_time");
                                        oneNews.updated_time = news.getLong("updated_time");
                                        if (firstImg != null)
                                            oneNews.img_url = firstImg.getString("url");
                                        if (source != null){
                                            oneNews.source_name = source.getString("name");
                                            oneNews.source_url = source.getString("url");
                                        }
                                        newsList.add(oneNews);
                                    }
                                    fragment.setContentItems(newsList);
                                    fragment.notifyDataChange();
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

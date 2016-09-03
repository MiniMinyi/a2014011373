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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by liumy on 16/9/2.
 */
public class PresenterSingleton {
    final static public String NEWS_SITE_FOR_CATEGORIES = "http://assignment.crazz.cn/news/en/category?timestamp=%d";
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
                                        MainActivity.tabList.add(new CategoryTab(key,title,
                                                RecyclerViewFragment.newInstance(),id));
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

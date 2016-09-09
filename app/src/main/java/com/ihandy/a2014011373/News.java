package com.ihandy.a2014011373;

import android.widget.CheckBox;

import com.thefinestartist.finestwebview.FinestWebView;

/**
 * Created by liumy on 16/9/3.
 * Contains
 */
public class News {
    String category = null;
    String country = null;
    long fetched_time = 0;
    String img_url = null;
    String locale_category = null;
    long news_id = 0;
    String origin = null;
    String source_name = null;
    String source_url = null;
    String title = null;
    String updated_time;
    CheckBox like_checkbox_in_main = null;
    CheckBox saved_checkbox_in_main = null;
    CheckBox saved_checkbox_in_favorite = null;
    FinestWebView.Builder webViewBuilder = null;
}

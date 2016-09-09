package com.ihandy.a2014011373;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.FinestWebViewActivity;

/**
 * Created by liumy on 16/9/8.
 * Add menu share via here.
 */
public class MyFinestWebViewActivity extends FinestWebViewActivity {
    protected String source_url;
    protected String img_url;
    protected String news_title;

    @Override
    protected void initializeOptions() {
        super.initializeOptions();
        Intent intent = getIntent();
        if (intent == null)
            return;
        MyFinestWebView.Builder builder = (MyFinestWebView.Builder) intent.getSerializableExtra("builder");
        source_url = builder.source_url;
        img_url = builder.img_url;
        news_title = builder.news_title;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuShareVia){
            Toast.makeText(v.getContext(),img_url,Toast.LENGTH_SHORT).show();
        }else
            super.onClick(v);
    }
}

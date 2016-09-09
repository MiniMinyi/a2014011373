package com.ihandy.a2014011373;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.mob.commons.SHARESDK;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.FinestWebViewActivity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
            Toast.makeText(v.getContext(),"Change share here",Toast.LENGTH_SHORT).show();

            ShareSDK.initSDK(this);
            OnekeyShare oks = new OnekeyShare();
            oks.disableSSOWhenAuthorize();

            oks.setTitle(news_title);
            oks.setTitleUrl(source_url);
            oks.setText(news_title + "\n source url: " + source_url);
//            oks.setImagePath("sre/drawable/bg1.jpg");
            oks.setImageUrl(img_url);
            oks.setUrl(source_url);
            oks.setComment("我是测试评论文本");
            oks.setSite(getString(R.string.app_name));
            oks.setSiteUrl(source_url);
            oks.show(this);

//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my share text.");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share");
//            shareIntent.setType("text/plain");
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(Intent.createChooser(shareIntent, "分享到"));
        }else
            super.onClick(v);
    }
}

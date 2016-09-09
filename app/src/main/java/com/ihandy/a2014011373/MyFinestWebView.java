package com.ihandy.a2014011373;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.thefinestartist.Base;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.FinestWebViewActivity;
import com.thefinestartist.finestwebview.listeners.BroadCastManager;
import com.thefinestartist.utils.content.Ctx;

/**
 * Created by liumy on 16/9/8.
 */
public class MyFinestWebView extends FinestWebView {
    public static class Builder extends FinestWebView.Builder{
        protected String source_url;
        protected String img_url;
        protected String news_title;

        protected Builder setSourceUrl(String sourceUrl){
            this.source_url = sourceUrl;
            return this;
        }

        protected Builder setImgUrl(String imgUrl){
            this.img_url = imgUrl;
            return this;
        }

        protected Builder setNewsTitle(String NewsTitle){
            this.news_title = NewsTitle;
            return this;
        }


        public Builder(@NonNull Activity activity) {
            super(activity);
        }

        /**
         * If you use context instead of activity, FinestWebView won't be able to override activity animation.
         * Try to create builder with Activity if it's possible.
         */
        public Builder(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void show(String url, String data) {
            this.url = url;
            this.data = data;
            this.key = System.identityHashCode(this);

            if (!listeners.isEmpty()) new BroadCastManager(context, key, listeners);

            Intent intent = new Intent(context, MyFinestWebViewActivity.class);
            intent.putExtra("builder", this);

            Ctx.startActivity(intent);

            if (context instanceof Activity)
                ((Activity) context).overridePendingTransition(animationOpenEnter, animationOpenExit);
        }
    }
}

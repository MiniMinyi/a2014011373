package com.ihandy.a2014011373;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.listeners.WebViewListener;

import java.util.List;

/**
 * Created by liumy on 16/8/27.
 * RecyclerViewAdapter to display every news
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<News> contents;
    NetworkImageView mNetworkImageView;
    TextView mNewsTitle;
    TextView mNewsCategory;
    ImageLoader mImageLoader;
    Context mContext;

    public NewsRecyclerViewAdapter(List<News> contents, Context context) {
        this.contents = contents;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contents.size() > 0? contents.size() : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (contents.size() == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_news_not_found, parent, false);
            return new RecyclerView.ViewHolder(view){
            };
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_news, parent, false);
        mNetworkImageView = (NetworkImageView) view.findViewById(R.id.news_img);
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();
        final News news = contents.get(viewType);
        if (news.img_url != null){
            mNetworkImageView.setImageUrl(news.img_url,mImageLoader);
            mNetworkImageView.setDrawingCacheEnabled(true);
        }
        mNewsTitle = (TextView)view.findViewById(R.id.news_title);
        if (news.title != null){
            mNewsTitle.setText(news.title);
        }
        mNewsCategory = (TextView)view.findViewById(R.id.category_label);
        mNewsCategory.setText("");

        final CheckBox saved_button = (CheckBox)view.findViewById(R.id.saved);
        news.saved_checkbox_in_main = saved_button;

        CardView cardView = (CardView)view.findViewById(R.id.card_view);
        news.webViewBuilder
                = new MyFinestWebView.Builder(mContext).
                setImgUrl(news.img_url).
                setSourceUrl(news.source_url).
                setNewsTitle(news.title).
                titleDefault(news.title).
                webViewBuiltInZoomControls(true).
                webViewDisplayZoomControls(true).
                webViewJavaScriptEnabled(false).
                setCustomAnimations(R.anim.slide_left_in,R.anim.hold,R.anim.hold,R.anim.slide_right_out).
                webViewCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK).
                addWebViewListener(new WebViewListener() {
                    String mTitle = null;
                    /* Title check and progress == 100 achieve at the same time can save_check_box be checked */
                    @Override
                    public void onReceivedTitle(String title) {
                        super.onReceivedTitle(title);
                        mTitle = title;
                    }

                    @Override
                    public void onProgressChanged(int progress) {
                        super.onProgressChanged(progress);
                        if (progress == 100 && mTitle != null && !mTitle.contains("Not Availabe")) {
                            news.saved_checkbox_in_main.setChecked(true);
                            if (news.saved_checkbox_in_favorite != null)
                                news.saved_checkbox_in_favorite.setChecked(true);
                        }
                    }
                });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news.source_url == null){
                    Toast.makeText(v.getContext(),"No source url found.",Toast.LENGTH_SHORT).show();
                    return;
                }
                news.webViewBuilder.show(news.source_url);
            }
        });

        CheckBox like_button = (CheckBox)view.findViewById(R.id.like);
        news.like_checkbox_in_main = like_button;
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();
                if (checked){
                    FavoriteNews.favoriteNews.add(news);
                }else{
                    FavoriteNews.favoriteNews.remove(news);
                }
            }
        });
        return new RecyclerView.ViewHolder(view){

        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}

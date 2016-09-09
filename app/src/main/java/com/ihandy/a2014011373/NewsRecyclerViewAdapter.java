package com.ihandy.a2014011373;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.finestwebview.listeners.WebViewListener;

import java.util.List;

/**
 * Created by liumy on 16/8/27.
 * RecyclerViewAdapter to display every news
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
    List<News> contents;
    Context mContext;
    ImageLoader mImageLoader;

    public NewsRecyclerViewAdapter(List<News> contents, Context context) {
        this.contents = contents;
        this.mContext = context;
        this.mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();
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
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (contents.size() == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_news_not_found, parent, false);
            return new NewsRecyclerViewAdapter.ViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_news, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mNewsCategory = (TextView)view.findViewById(R.id.category_label);
        viewHolder.mNewsTitle = (TextView)view.findViewById(R.id.news_title);
        viewHolder.mNetworkImageView = (NetworkImageView) view.findViewById(R.id.news_img);

        News news = contents.get(viewType);
        CheckBox saved_button = (CheckBox)view.findViewById(R.id.saved);
        viewHolder.saved_button = saved_button;
        news.saved_checkbox_in_main = saved_button;
        CheckBox like_button = (CheckBox)view.findViewById(R.id.like);
        viewHolder.like_button = like_button;
        news.like_checkbox_in_main = like_button;

        viewHolder.cardView = (CardView)view.findViewById(R.id.card_view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        if (contents.size() == 0){
            return;
        }
        final News news = contents.get(position);
        if (news.img_url != null){
            holder.mNetworkImageView.setImageUrl(news.img_url,mImageLoader);
            holder.mNetworkImageView.setDrawingCacheEnabled(true);
        }
        if (news.title != null){
            holder.mNewsTitle.setText(news.title);
        }

        holder.mNewsCategory.setText("");


        news.webViewBuilder
                = new MyFinestWebView.Builder(mContext).
                setImgUrl(news.img_url).
                setSourceUrl(news.source_url).
                setNewsTitle(news.title).
                titleDefault(news.title).
                webViewBuiltInZoomControls(true).
                webViewDisplayZoomControls(true).
                webViewJavaScriptEnabled(true).
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
                            news.saved = true;
                            if (news.saved_checkbox_in_favorite != null)
                                news.saved_checkbox_in_favorite.setChecked(true);
                        }
                    }
                });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news.source_url == null){
                    Toast.makeText(v.getContext(),"No source url found.",Toast.LENGTH_SHORT).show();
                    return;
                }
                news.webViewBuilder.show(news.source_url);
            }
        });

        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();
                if (checked){
                    news.liked = true;
                    FavoriteNews.favoriteNews.add(news);
                }else{
                    news.liked = false;
                    FavoriteNews.favoriteNews.remove(news);
                }
            }
        });

        if (news.saved)
            holder.saved_button.setChecked(true);

        if (news.liked) {
            holder.like_button.setChecked(true);
            FavoriteNews.favoriteNews.add(news);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView mNetworkImageView;
        public TextView mNewsTitle;
        public TextView mNewsCategory;
        public CheckBox saved_button;
        public CheckBox like_button;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
        }
    }
}

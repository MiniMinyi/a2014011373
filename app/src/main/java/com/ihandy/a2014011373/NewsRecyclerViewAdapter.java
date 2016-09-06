package com.ihandy.a2014011373;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.List;

/**
 * Created by liumy on 16/8/27.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<News> contents;
    NetworkImageView mNetworkImageView;
    TextView mNewsTitle;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_small, parent, false);
        mNetworkImageView = (NetworkImageView) view.findViewById(R.id.news_img);
        //mNetworkImageView.setDefaultImageResId(R.drawable.img_not_found);
        if (contents.size() == 0){
            return new RecyclerView.ViewHolder(view){
            };
        }
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();
        final News news = contents.get(viewType);
        if (news.img_url != null){
            mNetworkImageView.setImageUrl(news.img_url,mImageLoader);
        }
        mNewsTitle = (TextView)view.findViewById(R.id.news_title);
        if (news.title != null){
            mNewsTitle.setText(news.title);
        }
        if (news.source_url != null){
            CardView cardView = (CardView)view.findViewById(R.id.card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FinestWebView.Builder(mContext).
                            titleDefault(news.title).
                            webViewBuiltInZoomControls(true).
                            webViewDisplayZoomControls(true).
                            webViewJavaScriptEnabled(false).
                            show(news.source_url);
                }
            });
        }
        return new RecyclerView.ViewHolder(view){

        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


}

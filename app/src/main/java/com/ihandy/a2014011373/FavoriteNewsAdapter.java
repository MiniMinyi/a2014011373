package com.ihandy.a2014011373;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.finestwebview.FinestWebView;


/**
 * Created by liumy on 16/9/7.
 */
public class FavoriteNewsAdapter extends RecyclerView.Adapter<FavoriteNewsAdapter.ViewHolder>{
    Context mContext;

    public FavoriteNewsAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public FavoriteNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_news,parent,false);
        final News news = FavoriteNews.favoriteNews.get(viewType);
        return new ViewHolder(view,news);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final News news = FavoriteNews.favoriteNews.get(position);
        //set image
        holder.img.setImageUrl(news.img_url,
                RequestQueueSingleton.getInstance(mContext).getImageLoader());
        //set category

        holder.mNewsCategory.setText(String.format("Category: %s",news.category));
        //set title
        holder.title.setText(news.title);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news.source_url != null)
                    news.webViewBuilder.show(news.source_url);
                else{
                    Toast.makeText(v.getContext(),"No source url found.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.like_button.setChecked(true);
        holder.saved_button.setChecked(news.saved_checkbox_in_main.isChecked());
        final int pos = position;
        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();
                if (!checked){
                    news.like_checkbox_in_main.setChecked(false);
                    news.liked = false;
                    FavoriteNews.favoriteNews.remove(pos);
                    FavoriteNewsAdapter.this.notifyItemRemoved(pos);
                    FavoriteNewsAdapter.this.notifyItemRangeChanged(pos, FavoriteNews.favoriteNews.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return FavoriteNews.favoriteNews.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public News mNews;
        public NetworkImageView img;
        public TextView mNewsCategory;
        public CardView cardView;
        public TextView title;
        public CheckBox like_button;
        public CheckBox saved_button;
        public ViewHolder(View view, News news) {
            super(view);
            mNews = news;
            img = (NetworkImageView) view.findViewById(R.id.news_img);
            mNewsCategory = (TextView)view.findViewById(R.id.category_label);
            cardView = (CardView)view.findViewById(R.id.card_view);
            title = (TextView)view.findViewById(R.id.news_title);
            like_button = (CheckBox)view.findViewById(R.id.like);
            saved_button = (CheckBox)view.findViewById(R.id.saved);
            mNews.saved_checkbox_in_favorite = saved_button;
        }
    }
}

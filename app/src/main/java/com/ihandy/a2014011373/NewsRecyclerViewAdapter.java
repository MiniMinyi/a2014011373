package com.ihandy.a2014011373;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liumy on 16/8/27.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> contents;

    public NewsRecyclerViewAdapter(List<Object> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_small, parent, false);;
        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


}

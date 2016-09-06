/**
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ihandy.a2014011373.manage_category;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ihandy.a2014011373.MainActivity;
import com.ihandy.a2014011373.R;
import com.ihandy.a2014011373.RecyclerViewFragment;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ItemAdapter extends DragItemAdapter<Pair<String, RecyclerViewFragment>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    public int count = 0;
    private boolean isWatched;
    public ItemAdapter anotherAdapter;
    private ManageCategoryActivity activity;

    public ItemAdapter(int layoutId, int grabHandleId, boolean dragOnLongPress, boolean isWatched, ManageCategoryActivity activity) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        this.isWatched = isWatched;
        this.activity = activity;
        setHasStableIds(true);
        if(isWatched) {
            setItemList(MainActivity.tabList);
        }
        else {
            setItemList(MainActivity.unwatchedTabList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        Button button = (Button) view.findViewById(R.id.button);
        button.setTag(getItemId(count));
        count ++;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<String, RecyclerViewFragment> pair = (Pair<String, RecyclerViewFragment>) removeItem(getPositionForItemId((long) v.getTag()));
                anotherAdapter.addItem(0, pair);
                activity.fragmentChanged();
                //Toast.makeText(v.getContext(), " " + v.getTag(), Toast.LENGTH_SHORT).show();getPositionForItemId((long) v.getTag())
            }
        });
        return new ViewHolder(view);
        //else
    }

    public void addMyItem(Pair<String, RecyclerViewFragment> pair, int position) {
        addItem(position, pair);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).first;
        holder.mText.setText(text);
        holder.itemView.setTag(text);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).second.getCustomId();
    }

    public class ViewHolder extends DragItemAdapter<Pair<String, RecyclerViewFragment>, ItemAdapter.ViewHolder>.ViewHolder {
        public TextView mText;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}

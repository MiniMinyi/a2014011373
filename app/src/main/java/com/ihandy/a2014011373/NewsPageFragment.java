package com.ihandy.a2014011373;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by liumy on 16/8/26.
 */

//每一个tab由一个Fragment来显示，Fragment中的内容根据它的category再创建
public class NewsPageFragment extends Fragment{
    String mContent;

    NewsPageFragment setContent(String content){
        this.mContent = content;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_main,container,false);
        TextView textView = (TextView)view.findViewById(R.id.testTextView);
        textView.setText(mContent);
        return view;
    }

}

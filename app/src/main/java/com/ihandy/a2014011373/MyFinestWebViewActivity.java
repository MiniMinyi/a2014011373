package com.ihandy.a2014011373;

import android.view.View;
import android.widget.Toast;

import com.thefinestartist.finestwebview.FinestWebViewActivity;

/**
 * Created by liumy on 16/9/8.
 * Add menu share via here.
 */
public class MyFinestWebViewActivity extends FinestWebViewActivity {
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuShareVia){
            Toast.makeText(v.getContext(),"Change share here",Toast.LENGTH_SHORT).show();
        }else
            super.onClick(v);
    }
}

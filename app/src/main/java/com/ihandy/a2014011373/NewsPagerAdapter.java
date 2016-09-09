package com.ihandy.a2014011373;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

/**
 * Created by liumy on 16/8/26.
 */
public class NewsPagerAdapter extends NewFragmentStatePagerAdapter {
    MainActivity mainActivity;

    public NewsPagerAdapter(FragmentManager fm, MainActivity mainActivity){
        super(fm);
        this.mainActivity = mainActivity;
    }

    /**
     * 得到每个页面
     * @TODO return appropriate Fragment later
     */
    @Override
    public Fragment getItem(int position){
        return mainActivity.tabList.get(position).getFragment();
    }
    /**
     * 每个页面的title
     */
    @Override
    public CharSequence getPageTitle(int position){
        return (mainActivity.tabList.size() > position) ? mainActivity.tabList.get(position).getTitle():"";
    }

    /**
     * 页面总个数
     */
    @Override
    public int getCount() {
        return mainActivity.tabList.size();
    }

    /**
     * 设置每个tab的颜色和图片
     */
    class MaterialViewPagerListener implements MaterialViewPager.Listener{

        @Override
        public HeaderDesign getHeaderDesign(int page) {
            switch (page % 4) {
                case 0:
                    Drawable greenPic = mainActivity.createDrawableFromResource(R.drawable.bg1);
                    return HeaderDesign.fromColorResAndDrawable(R.color.green,greenPic);
                case 1:
                    Drawable bluePic = mainActivity.createDrawableFromResource(R.drawable.bg2);
                    return HeaderDesign.fromColorResAndDrawable(R.color.blue,bluePic);
                case 2:
                    Drawable cyanPic = mainActivity.createDrawableFromResource(R.drawable.bg3);
                    return HeaderDesign.fromColorResAndDrawable(R.color.cyan,cyanPic);
                case 3:
                    Drawable redPic = mainActivity.createDrawableFromResource(R.drawable.bg4);
                    return HeaderDesign.fromColorResAndDrawable(R.color.red,redPic);
            }

            //execute others actions if needed (ex : modify your header logo)

            return null;
        }
    }

    public MaterialViewPagerListener newMaterialViewPagerListener(){
        return new MaterialViewPagerListener();
    }
}

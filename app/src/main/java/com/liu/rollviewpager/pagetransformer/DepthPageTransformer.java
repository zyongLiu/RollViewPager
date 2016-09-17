package com.liu.rollviewpager.pagetransformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Liu on 2016-09-14.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer{
    private static final float MIN_SCALE=0.75f;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(View view, float v) {
        int pagerWidth=view.getWidth();
        if (v<-1){
            view.setAlpha(0);
        }else if (v<=0){
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }else if (v<=1){
            view.setAlpha(1-v);
            view.setTranslationX(pagerWidth*-v);
            float scaleFactor=MIN_SCALE+(1-MIN_SCALE)*(1- Math.abs(v));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }else {
            view.setAlpha(0);
        }
    }
}

package com.liu.rollviewpager.pagetransformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Liu on 2016-09-15.
 */
public class RotateDownPageTransformer implements ViewPager.PageTransformer{
    private static final float ROT_MAX=20.0f;
    private float mRot;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(View view, float v) {
        Log.i("TAG",view+","+v+"");
        if (v<-1){
            view.setRotation(0);
        }
//        else if (v<0){
//            mRot=ROT_MAX*v;
//            view.setPivotX(view.getWidth()/2);
//            view.setPivotY(view.getHeight());
//            view.setRotation(mRot);
//        }
        else if (v<=1){
            mRot=ROT_MAX*v;
            view.setPivotX(view.getWidth()/2);
            view.setPivotY(view.getHeight());
            view.setRotation(mRot);
        }else {
            view.setRotation(0);
        }
    }
}

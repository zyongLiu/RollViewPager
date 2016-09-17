package com.liu.rollviewpager;

import android.app.Application;

import com.squareup.picasso.Picasso;

/**
 * Created by Liu on 2016-09-15.
 */
public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.with(this).areIndicatorsEnabled();
    }
}

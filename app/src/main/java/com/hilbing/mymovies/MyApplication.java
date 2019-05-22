package com.hilbing.mymovies;

import android.app.Application;

public class MyApplication extends Application {

    //Class to have access to resources

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = (MyApplication) getApplicationContext();
    }

    public static MyApplication getContext(){
        return mContext;
    }
}

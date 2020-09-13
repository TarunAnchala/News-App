package com.assigment.newsapp.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;
    private static String packName;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        packName=context.getPackageName();
    }

    public static Context getAppContext(){
        return context;
    }

    public static String getPackName(){
        return packName;
    }
}

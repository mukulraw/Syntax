package com.syntax.note;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;
    private String TAG ="myApp";
    @Override
    public void onCreate()
    {
      super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}

package com.example.verygoodgraphics.android.demo;

import static com.verygoodgraphics.android.container.main.initVggRuntime;

import android.app.Application;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initVggRuntime();
    }

}

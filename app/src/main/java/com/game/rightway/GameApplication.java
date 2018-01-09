package com.game.rightway;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class GameApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String ADMOB_APP_ID = "ca-app-pub-1630263344353342~9856246327";
        MobileAds.initialize(this, ADMOB_APP_ID);
    }
}

package com.game.rightway.helpers;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public final class AdHelper {

    public static void showIntersitialAd(Context c, InterstitialAd mInterstitialAd) {
        if (Utils.RANDOM.nextBoolean()) {
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    //.addTestDevice("722876127F5FE158800DE4BC81C71020")
                    .build());
        }
    }
}

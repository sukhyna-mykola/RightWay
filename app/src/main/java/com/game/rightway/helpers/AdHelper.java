package com.game.rightway.helpers;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public final class AdHelper {

    public static void showIntersitialAd(Context c, InterstitialAd mInterstitialAd) {
        if (Utils.RANDOM.nextBoolean()) {
            mInterstitialAd
                    .loadAd(new AdRequest.Builder()
                    .build());
        }
    }
}

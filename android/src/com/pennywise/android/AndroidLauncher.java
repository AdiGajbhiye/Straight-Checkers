package com.pennywise.android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pennywise.AdsController;
import com.pennywise.Checkers;

public class AndroidLauncher extends AndroidApplication implements AdsController {

    protected AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // initialize(new Checkers(), config);

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new Checkers(this), config);

        setupAds();

        // Add the libgdx view
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(adView, params);

        setContentView(layout);
    }

    private  void setupAds() {
        adView = new AdView(this);
        adView.setVisibility(View.INVISIBLE);
        adView.setBackgroundColor(0xff000000); // black
        adView.setAdUnitId(getString(R.string.adUnitId));
        adView.setAdSize(AdSize.SMART_BANNER);
    }

    @Override
    public void showBannerAd() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.VISIBLE);
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                adRequestBuilder.addTestDevice("85253FE6D4C2A3DEBA982785D93C3ABF");
                adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                adView.loadAd(adRequestBuilder.build());
            }
        });
    }

    @Override
    public void hideBannerAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.INVISIBLE);
            }
        });
    }
}

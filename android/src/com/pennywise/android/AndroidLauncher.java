package com.pennywise.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pennywise.Checkers;
import com.pennywise.android.bluetooth.BluetoothManager;
import com.pennywise.managers.AdManager;

public class AndroidLauncher extends AndroidApplication implements AdManager {

    protected AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private BluetoothManager mBluetoothManager;
    private Checkers checkers;
    private boolean host = false;

    public static final String LOG = AndroidLauncher.class.getSimpleName();


    public Checkers getCheckers() {
        return checkers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        mBluetoothManager = new BluetoothManager(this);

        // Register for BluetoothAdapter broadcasts
        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setupAds();

        checkers = new Checkers(mBluetoothManager, this);
        // Create the libgdx View
        View gameView = initializeForView(checkers, config);

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

    private void setupAds() {
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
                //adRequestBuilder.addTestDevice("A171181E4E254A38F6E617040D216CC8");
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


    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        adView.pause();
    }

    public void onDestroy() {
        super.onDestroy();

        if (adView != null)
            adView.destroy();
        // Unregister broadcast listener
        this.unregisterReceiver(mReceiver);

        Gdx.app.log(LOG, "== onDestroy ==");
    }


    // Receives Bluetooth events
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            // BT discoverability state change
            if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_SCAN_MODE,
                        BluetoothAdapter.ERROR);

                switch (state) {
                    // BT is discoverable
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Gdx.app.log(LOG,
                                "BroadcastReceiver: SCAN_MODE_CONNECTABLE_DISCOVERABLE");
                        checkers.notify_BT_SCAN_MODE_CONNECTABLE_DISCOVERABLE();
                        break;
                    // BT is NOT discoverable
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Gdx.app.log(LOG, "BroadcastReceiver: SCAN_MODE_CONNECTABLE");
                        checkers.notify_BT_SCAN_MODE_CONNECTABLE();
                        break;
                    // BT is NOT discoverable
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Gdx.app.log(LOG, "BroadcastReceiver: SCAN_MODE_NONE");
                        checkers.notify_BT_SCAN_MODE_NONE();
                        break;
                }
            }
            // BT state change
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Gdx.app.log(LOG, "BroadcastReceiver: STATE_OFF");
                        checkers.notify_BT_STATE_OFF();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Gdx.app.log(LOG, "BroadcastReceiver: STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Gdx.app.log(LOG, "BroadcastReceiver: STATE_ON");
                        checkers.notify_BT_STATE_ON();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Gdx.app.log(LOG, "BroadcastReceiver: STATE_TURNING_ON");
                        break;
                }
            }
            // When discovery finds a device
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Gdx.app.log(LOG, "BroadcastReceiver: ACTION_FOUND");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Gdx.app.log(LOG, "DETECTED DEVICE = " + device);
                // Add the discovered device to bluetooth manager's list of
                // discovered devices
                mBluetoothManager.addDiscoveredDevice(device);
                // Notify game that a device was found
                checkers.notify_BT_DEVICE_FOUND();
            }
            // When discovery starts
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Gdx.app.log(LOG, "BroadcastReceiver: ACTION_DISCOVERY_STARTED");
                checkers.notify_BT_ACTION_DISCOVERY_STARTED();
            }
            // When discovery finishes
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Gdx.app.log(LOG, "BroadcastReceiver: ACTION_DISCOVERY_FINISHED");
                checkers.notify_BT_ACTION_DISCOVERY_FINISHED();
            }
        }
    };

    public void onBTMStateChange() {
        checkers.notify_BMT_STATE_CHANGE();
    }


    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}

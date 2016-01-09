package com.pennywise.android;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pennywise.Checkers;
import com.pennywise.managers.GameManager;
import com.pennywise.managers.NetworkListener;

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements GameManager,
        WifiP2pManager.ChannelListener, DeviceActionListener {

    protected AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    protected static final int PORT = 32300;

    public static final String TAG = "wifidirectdemo";
    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private NetworkListener networkListener;

    private List<WifiP2pDevice> peerList = new ArrayList<WifiP2pDevice>();
    private WifiP2pDevice targetDevice;
    private Handler mUpdateHandler;
    private WifiP2pInfo wifiInfo;
    private boolean serverThreadActive = false;
    private ServerService serverService;
    private ClientService clientService;
    private WiFiClientBroadcastReceiver clientReceiver = null;
    private WiFiServerBroadcastReceiver serverReceiver = null;
    private boolean connectedAndReadyToSendFile;
    private boolean clientThreadActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

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

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
            }
        };
    }

    public void discover(String gameName) {

        if (!isWifiP2pEnabled) {
            Toast.makeText(AndroidLauncher.this, R.string.wireless_off,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(AndroidLauncher.this, "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(AndroidLauncher.this, "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });

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
                adRequestBuilder.addTestDevice("A171181E4E254A38F6E617040D216CC8");
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
    public void advertise() {

    }

    @Override
    public void discover() {

    }

    @Override
    public void connect() {

    }

    @Override
    public void send(String data) {

    }

    @Override
    public void receiver(NetworkListener listener) {
        this.networkListener = listener;
    }

    @Override
    public void startClient() {

        if (!clientThreadActive) {
            clientReceiver = new WiFiClientBroadcastReceiver(manager, channel, this);
            registerReceiver(clientReceiver, intentFilter);
            //Create new thread, open socket, wait for connection, and transfer file
            clientService = new ClientService(mUpdateHandler);
            clientService.connectToServer(wifiInfo.groupOwnerAddress, PORT);
            clientThreadActive = true;
        }
    }

    @Override
    public void startServer() {
        //If server is already listening on port or transfering data, do not attempt to start server service
        if (!serverThreadActive) {
            serverReceiver = new WiFiServerBroadcastReceiver(manager, channel, this);
            registerReceiver(serverReceiver, intentFilter);
            //Create new thread, open socket, wait for connection, and transfer file
            serverService = new ServerService(mUpdateHandler, PORT);
            serverThreadActive = true;
        }
    }

    public void stopServer(View view) {
        serverService.tearDown();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        peerList.clear();
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }


    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(AndroidLauncher.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
            }

            @Override
            public void onSuccess() {
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showDetails(WifiP2pDevice device) {

    }

    @Override
    public void cancelDisconnect() {

        if (manager != null) {
            if (getDevice() == null
                    || getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (getDevice().status == WifiP2pDevice.AVAILABLE
                    || getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(AndroidLauncher.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(AndroidLauncher.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private static String getDeviceStatus(int deviceStatus) {
        Log.d(AndroidLauncher.TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    public void displayPeers(WifiP2pDeviceList peers) {
        peerList.clear();
        peerList.addAll(peers.getDeviceList());
        if (peerList.size() == 0) {
            Log.d(TAG, "No devices found");
        }
    }

    public WifiP2pDevice getDevice() {
        return targetDevice;
    }

    public void setDevice(WifiP2pDevice device) {
        this.targetDevice = device;
    }

    public void setTransferStatus(boolean status) {
        connectedAndReadyToSendFile = status;
    }

    public void setNetworkToReadyState(boolean status, WifiP2pInfo info, WifiP2pDevice device) {
        wifiInfo = info;
        targetDevice = device;
        connectedAndReadyToSendFile = status;
    }

    private void stopClient() {
        try {
            clientService.tearDown();
        } catch (IllegalArgumentException e) {
            //This will happen if the server was never running and the stop button was pressed.
            //Do nothing in this case.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (clientReceiver != null) {
            unregisterReceiver(clientReceiver);
            stopClient();
        }
        if (serverReceiver != null) {
            unregisterReceiver(serverReceiver);
            stopServer(null);
        }


    }

    public void setServerWifiStatus(String s) {
    }

    public void setServerStatus(String s) {
    }


}

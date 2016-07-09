package com.pennywise;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.screens.GameScreen;
import com.pennywise.checkers.screens.HostScreen;
import com.pennywise.checkers.screens.JoinScreen;
import com.pennywise.checkers.screens.LevelScreen;
import com.pennywise.checkers.screens.MenuScreen;
import com.pennywise.checkers.screens.MultiplayerScreen;
import com.pennywise.managers.AdManager;
import com.pennywise.managers.MultiplayerDirector;
import com.pennywise.multiplayer.BluetoothInterface;
import com.pennywise.multiplayer.TransmissionPackage;

public class Checkers extends Game {

    private AdManager adManager;
    public static boolean BLUETOOTH_INTERFACE_EXISTS;
    private boolean multiplayer;
    private boolean host;
    private BluetoothInterface bluetoothInterface;
    // Assets
    private AssetManager _assetManager = null;

    public Checkers() {
        BLUETOOTH_INTERFACE_EXISTS = false;
    }

    public Checkers(BluetoothInterface bluetoothInterface, AdManager adManager) {
        this.bluetoothInterface = bluetoothInterface;
        this.adManager = adManager;
        BLUETOOTH_INTERFACE_EXISTS = true;
        multiplayer = false;
        host = false;
        adManager.showBannerAd();

        _assetManager = new AssetManager();
    }

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    public AssetManager getAssetManager() {
        return _assetManager;
    }

    public BluetoothInterface getBluetoothInterface() {
        return bluetoothInterface;
    }

    public void setBluetoothInterface(BluetoothInterface bluetoothInterface) {
        this.bluetoothInterface = bluetoothInterface;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    /*
     * Event notifications on Bluetooth coming from MainActivity -->
	 * BluetoothAdapter
	 */

    public void notify_BT_SCAN_MODE_CONNECTABLE_DISCOVERABLE() {
        if (getScreen() instanceof HostScreen) {
            if (bluetoothInterface.isListening()) {
                ((HostScreen) getScreen()).getInfoLabel().setText(
                        "Game hosted, waiting for connection");
            }
            bluetoothInterface.startConnectionListening();
        }
    }

    public void notify_BT_SCAN_MODE_CONNECTABLE() {
        if (getScreen() instanceof HostScreen) {
            ((HostScreen) getScreen())
                    .getInfoLabel()
                    .setText(
                            "Bluetooth is NOT discoverable!");
            ((HostScreen) getScreen()).getBackButton().setVisible(true);

        }
    }

    public void notify_BT_SCAN_MODE_NONE() {
        if (getScreen() instanceof HostScreen) {
            // Since BT is down there is no need to be in HostScreen
            bluetoothInterface.stopConnectionListening();
            setScreen(new MultiplayerScreen(this));
        }
    }

    public void notify_BT_STATE_OFF() {
        if (getScreen() instanceof JoinScreen) {
            // Since BT is down there is no need to be in JoinScreen
            bluetoothInterface.cancelDiscovery();
            bluetoothInterface.stopConnectionToHost();
            setScreen(new MultiplayerScreen(this));
        }
    }

    public void notify_BT_STATE_ON() {
        if (getScreen() instanceof JoinScreen) {
            ((JoinScreen) getScreen()).getInfoLabel().setText("Select Opponent!");
            ((JoinScreen) getScreen()).getScanButton().setVisible(true);
            ((JoinScreen) getScreen()).getConnectButton().setVisible(true);
            ((JoinScreen) getScreen()).listDevices();
        }
    }

    public void notify_BT_DEVICE_FOUND() {
        // Ask JoinScreen to re-fetch the list of devices
        if (getScreen() instanceof JoinScreen) {
            // Sometimes ACTION_FOUND is fired even when not discovering (a
            // bug?)
            if (!bluetoothInterface.isDiscovering())
                return;
            ((JoinScreen) getScreen()).listDevices();
        }
    }

    public void notify_BT_ACTION_DISCOVERY_STARTED() {
        if (getScreen() instanceof JoinScreen) {
            ((JoinScreen) getScreen()).getInfoLabel().setText(
                    "Scanning, please wait...");
            ((JoinScreen) getScreen()).getScanButton().setText("Scanning...");
        }
    }

    public void notify_BT_ACTION_DISCOVERY_FINISHED() {
        if (getScreen() instanceof JoinScreen) {
            ((JoinScreen) getScreen()).getInfoLabel().setText(
                    "Chooose your opponent");
            ((JoinScreen) getScreen()).getScanButton().setText("Scan");
        }
    }

    public void notify_BMT_STATE_CHANGE() {
        if (getScreen() instanceof JoinScreen) {
            if (bluetoothInterface.isIdle()) {
                ((JoinScreen) getScreen()).getInfoLabel().setText(
                        "Unable to connect. Please try again.");
                ((JoinScreen) getScreen()).getConnectButton()
                        .setText("Connect");
            } else if (bluetoothInterface.isListening()) {

            } else if (bluetoothInterface.isConnecting()) {
                ((JoinScreen) getScreen()).getInfoLabel().setText(
                        "Connecting, please wait...");
                ((JoinScreen) getScreen()).getConnectButton().setText(
                        "Connecting...");
            } else if (bluetoothInterface.isConnected()) {
                setScreen(new GameScreen(this, Constants.NONE));
                // ((GameScreen) getScreen())
                //         .getInfoLabel()
                //          .setText("Waiting for host to start the game...");
            }
        } else if (getScreen() instanceof HostScreen) {
            if (bluetoothInterface.isIdle()) {
                ((HostScreen) getScreen()).getInfoLabel().setText(
                        "There seems to be a problem. Please try again :)");
            } else if (bluetoothInterface.isListening()) {
                ((HostScreen) getScreen()).getInfoLabel().setText(
                        "Waiting for opponent...");
                ((HostScreen) getScreen()).getBackButton().setVisible(true);
            } else if (bluetoothInterface.isConnecting()) {
                ((HostScreen) getScreen()).getInfoLabel().setText(
                        "Connecting...");
            } else if (bluetoothInterface.isConnected()) {

                setScreen(new GameScreen(this, Constants.NONE));
                // ((GameScreen) getScreen())
                //         .getInfoLabel()
                //         .setText("Touch screen to start the game!");
            }
        }else if (getScreen() instanceof GameScreen) {
            if (bluetoothInterface.isIdle()) {
                ((GameScreen) getScreen()).showDisconnected();
            } else if (bluetoothInterface.isListening()) {

            } else if (bluetoothInterface.isConnecting()) {

            } else if (bluetoothInterface.isConnected()) {
            }
        }
    }

    public void notify_PeerDataReceived(TransmissionPackage transmissionPackage) {
        MultiplayerDirector director = ((GameScreen) getScreen());
        director.notify_PeerDataReceived(transmissionPackage);
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}

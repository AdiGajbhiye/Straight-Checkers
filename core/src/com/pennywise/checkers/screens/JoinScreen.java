package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.pennywise.Checkers;
import com.pennywise.multiplayer.BluetoothInterface;

import java.util.Set;


public class JoinScreen extends AbstractScreen {

    public static final String LOG = JoinScreen.class.getSimpleName();

    private BluetoothInterface bluetoothInterface;

    private Label infoLabel;
    private List devicesList;
    private ScrollPane devicesListScrollPane;
    private TextButton backButton;
    private TextButton scanButton;
    private TextButton connectButton;
    private Skin skin;

    public JoinScreen(Checkers game) {
        super(game);
        skin = getSkin();
        bluetoothInterface = game.getBluetoothInterface();
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(Label infoLabel) {
        this.infoLabel = infoLabel;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public void setBackButton(TextButton backButton) {
        this.backButton = backButton;
    }

    public TextButton getScanButton() {
        return scanButton;
    }

    public void setScanButton(TextButton scanButton) {
        this.scanButton = scanButton;
    }

    public TextButton getConnectButton() {
        return connectButton;
    }

    public void setConnectButton(TextButton connectButton) {
        this.connectButton = connectButton;
    }

    @Override
    public void show() {
        infoLabel = new Label("", skin);
        infoLabel.setAlignment(Align.center);
        getTable().add(infoLabel).colspan(3).spaceBottom(10).spaceTop(10);
        getTable().row();

        // Empty list
        devicesList = new List(skin);
        devicesList.setItems(new Array<Object>());
        devicesListScrollPane = new ScrollPane(devicesList, skin);
        devicesListScrollPane.setOverscroll(false, false);
        getTable().add(devicesListScrollPane).colspan(3).align(Align.center)
                .spaceBottom(10);
        getTable().row();

        backButton = new TextButton("Back", skin);
        backButton.setVisible(false);
        getTable().add(backButton).size(150, 40).expand().bottom()
                .spaceBottom(5).spaceRight(5);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // If connection is in progress, do nothing
                if (bluetoothInterface.isConnecting()
                        || bluetoothInterface.isDiscovering())
                    return;
                bluetoothInterface.cancelDiscovery();
                bluetoothInterface.stopConnectionToHost();
                game.setScreen(new MultiplayerScreen(game));
            }
        });

        scanButton = new TextButton("Scan", skin);
        scanButton.setVisible(false);
        getTable().add(scanButton).size(150, 40).expand().bottom()
                .spaceBottom(5).spaceLeft(5).spaceRight(5);
        scanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!bluetoothInterface.isDiscovering()
                        && !bluetoothInterface.isConnecting()
                        && !bluetoothInterface.isConnected()) {
                    Gdx.app.log(LOG, "Will start device discovery");
                    bluetoothInterface.startDiscovery();
                }
            }
        });

        connectButton = new TextButton("Connect", skin);
        connectButton.setVisible(false);
        getTable().add(connectButton).size(150, 40).expand().bottom()
                .spaceBottom(5).spaceLeft(5);
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!bluetoothInterface.isDiscovering()
                        && !bluetoothInterface.isConnecting()) {
                    // To get the MAC from the string subtract the length of the
                    // MAC from the end of the string
                    // and get the substring beginning at that index.
                    String selectedDevice = (String) devicesList.getSelected();

                    if (selectedDevice != null) {
                        String mac = selectedDevice.substring(selectedDevice
                                .length() - 17);
                        Gdx.app.log(LOG, "Selected MAC =" + mac + ".");
                        bluetoothInterface.startConnectionToHost(mac);
                    }
                }
            }
        });

        // Check if the Android device supports bluetooth.
        if (bluetoothInterface.isBluetoothSupported()) {
            // Check if bluetooth is enabled. If not, enable it.
            if (!bluetoothInterface.isBluetoothEnabled()) {
                Gdx.app.log(
                        LOG,
                        "isBluetoothEnabled = "
                                + bluetoothInterface.isBluetoothEnabled());
                bluetoothInterface.enableBluetooth();
            } else {
                Gdx.app.log(
                        LOG,
                        "isBluetoothEnabled = "
                                + bluetoothInterface.isBluetoothEnabled());
                infoLabel
                        .setText("Bluetooth is enabled! Select device to connect!");
                backButton.setVisible(true);
                scanButton.setVisible(true);
                connectButton.setVisible(true);
                listDevices();
            }
        }
        // The Android device does not support bluetooth.
        else {
            infoLabel
                    .setText("Can't play multiplayer dude.\nBluetooth not supported on this device.");
        }
    }

    /**
     * Fetches the devices and places them on the screen.
     */
    public void listDevices() {
        Set<String> devices = bluetoothInterface.getDevices();
        Gdx.app.log(LOG,
                "Number of devices (paired+discovered) = " + devices.size());
        devicesList.setItems(devices.toArray());
    }

    public void dispose() {
        super.dispose();
        Gdx.app.log(LOG, "Disposing JoinScreen");
    }
}

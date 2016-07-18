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
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.persistence.Player;
import com.pennywise.checkers.core.persistence.SaveUtil;
import com.pennywise.managers.AudioManager;
import com.pennywise.multiplayer.BluetoothInterface;

import java.util.Iterator;
import java.util.Set;


public class JoinScreen extends AbstractScreen {

    public static final String LOG = JoinScreen.class.getSimpleName();

    private BluetoothInterface bluetoothInterface;

    private Label infoLabel;
    private List devicesList;
    private ScrollPane devicesListScrollPane;
    private TextButton scanButton;
    private TextButton connectButton;
    private Skin skin;
    Set<String> devices;

    public JoinScreen(Checkers game) {
        super(game);
        skin = getSkin();
        bluetoothInterface = game.getBluetoothInterface();
        game.setHost(false);
        game.setMultiplayer(true);
        setBackButtonActive(true);
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public TextButton getScanButton() {
        return scanButton;
    }

    public TextButton getConnectButton() {
        return connectButton;
    }

    @Override
    public void show() {
        infoLabel = new Label("", skin);
        infoLabel.setAlignment(Align.center);
        getTable().padTop(60);
        getTable().add(infoLabel).spaceBottom(5).spaceTop(5).colspan(2);
        getTable().row();

        // Empty list
        devicesList = new List(skin,"dimmed");
        devicesList.setItems(new Array<Object>());
        devicesList.pack();
        devicesListScrollPane = new ScrollPane(devicesList, skin);
        devicesListScrollPane.setSize(Constants.GAME_WIDTH - 20, Constants.GAME_HEIGHT * 0.55f);

        getTable().add(devicesListScrollPane).center().colspan(2)
                .spaceBottom(5).width(Constants.GAME_WIDTH - 20).height(Constants.GAME_HEIGHT * 0.60f).expand();
        getTable().row();

        scanButton = new TextButton("Scan", skin);
        getTable().add(scanButton).size(220, 70).expand()
                .padBottom(60).spaceLeft(5);
        scanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                if (!bluetoothInterface.isDiscovering()
                        && !bluetoothInterface.isConnecting()
                        && !bluetoothInterface.isConnected()) {
                    Gdx.app.log(LOG, "Will start device discovery");
                    bluetoothInterface.startDiscovery();
                }
            }
        });


        connectButton = new TextButton("Connect", skin);
        getTable().add(connectButton).size(220, 70).expand()
                .padBottom(60).spaceRight(5);
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                if (!bluetoothInterface.isDiscovering()
                        && !bluetoothInterface.isConnecting()) {

                    Player player = new Player(bluetoothInterface.getName(), 0, false);
                    SaveUtil.savePlayer(player);
                    // To get the MAC from the string subtract the length of the
                    // MAC from the end of the string
                    // and get the substring beginning at that index.
                    String selectedDevice = (String) devicesList.getSelected();

                    if (selectedDevice != null) {
                        selectedDevice = selectedDevice.trim();

                        String mac = mac(selectedDevice);
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
                        .setText("Select device to connect!");
                listDevices();
            }
        }
        // The Android device does not support bluetooth.
        else {
            infoLabel
                    .setText("Bluetooth not supported on this device.");
        }
    }

    private String mac(String deviceName) {

        int count = devices.size();
        String[] deviceNames = new String[count];
        Iterator<String> iter = devices.iterator();
        int i = 0;
        String addr = "", name = "";

        while (iter.hasNext()) {
            String entry = iter.next();
            int index = entry.indexOf(",");
            name = entry.substring(0, index);
            name = name.trim();
            if (name.equals(deviceName)) {
                addr = entry.substring(entry.length() - 17);
            }
        }

        return addr;
    }

    /**
     * Fetches the devices and places them on the screen.
     */
    public void listDevices() {
        devices = bluetoothInterface.getDevices();
        Gdx.app.log(LOG, "Number of devices (paired+discovered) = " + devices.size());
        int count = devices.size();
        String[] deviceNames = new String[count];
        Iterator<String> iter = devices.iterator();
        int i = 0;

        while (iter.hasNext()) {
            String name = iter.next();
            int index = name.indexOf(",");
            deviceNames[i++] = name.substring(0, index);
        }

        devicesList.setItems(deviceNames);
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    public void keyBackPressed() {
        bluetoothInterface.cancelDiscovery();
        bluetoothInterface.stopConnectionToHost();
        game.setScreen(new MultiplayerScreen(game));
    }
}

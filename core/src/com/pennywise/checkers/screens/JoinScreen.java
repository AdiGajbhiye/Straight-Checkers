package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
    Player player;
    private TextField playerName;

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

        player = SaveUtil.loadPlayer();

        getTable().setBackground("dialog");
        getTable().pad(10);

        Label welcomeLabel = new Label("Join Game", getSkin());
        welcomeLabel.setAlignment(Align.center);
        Table gameTitle = new Table(getSkin());
        gameTitle.setBackground("dialog");
        gameTitle.add(welcomeLabel).center().size(Constants.GAME_WIDTH * 0.88f, 80);
        getTable().top().add(gameTitle).fillX();
        getTable().row();


        Table menuContainer = new Table(getSkin());
        menuContainer.setBackground("dialog");
        getTable().add(menuContainer).fill();

        Label name = new Label("Player Name:", getSkin());
        name.setAlignment(Align.left);
        menuContainer.left().add(name).left().size(Constants.GAME_WIDTH * 0.88f, 50).padLeft(5);
        menuContainer.row();

        playerName = new TextField(player.getName(), getSkin());
        menuContainer.left().add(playerName).left().size(Constants.GAME_WIDTH * 0.88f, 70).padBottom(15).padLeft(5);

        menuContainer.row();

        infoLabel = new Label("", getSkin());
        infoLabel.setAlignment(Align.left);
        infoLabel.setWrap(true);
        menuContainer.left().add(infoLabel).left().size(Constants.GAME_WIDTH * 0.88f, 70).pad(5);

        menuContainer.row();

        // Empty list
        devicesList = new List(skin, "dimmed");
        devicesList.setItems(new Array<Object>());
        devicesList.pack();
        devicesListScrollPane = new ScrollPane(devicesList, skin);
        devicesListScrollPane.setSize(Constants.GAME_WIDTH * 0.88f, Constants.GAME_HEIGHT * 0.20f);

        menuContainer.center().add(devicesListScrollPane).center()
                .spaceBottom(5).width(Constants.GAME_WIDTH * 0.88f).height(Constants.GAME_HEIGHT * 0.30f).expand();
        menuContainer.row();

        Table footer = new Table(getSkin());
        footer.setBackground("dialog");
        menuContainer.bottom().add(footer).fillX();
        menuContainer.row();

        scanButton = new TextButton("Scan", skin);
        footer.add(scanButton).size(200, 70).expand()
                .padBottom(10).spaceLeft(5);
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
        footer.add(connectButton).size(200, 70).expand()
                .padBottom(10).spaceRight(5);
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                if (!bluetoothInterface.isDiscovering()
                        && !bluetoothInterface.isConnecting()) {

                    Player player = new Player(playerName.getText(), 0, false);
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

        Gdx.app.log(LOG, "isBluetoothEnabled = " + bluetoothInterface.isBluetoothEnabled());
        infoLabel.setText("Select device to connect!");
        listDevices();

        // Check if the Android device supports bluetooth.
        if (bluetoothInterface.isBluetoothSupported()) {
            // Check if bluetooth is enabled. If not, enable it.
            if (!bluetoothInterface.isBluetoothEnabled()) {
                Gdx.app.log(LOG, "isBluetoothEnabled = " + bluetoothInterface.isBluetoothEnabled());
                bluetoothInterface.enableBluetooth();
            } else {
            }
        }
        // The Android device does not support bluetooth.
        else {
            infoLabel.setText("Bluetooth not supported\non this device.");
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

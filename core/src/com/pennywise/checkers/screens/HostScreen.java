package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.engine.Simple;
import com.pennywise.checkers.core.persistence.Player;
import com.pennywise.checkers.core.persistence.SaveUtil;
import com.pennywise.managers.AudioManager;
import com.pennywise.multiplayer.BluetoothInterface;


public class HostScreen extends AbstractScreen {

    public static final String LOG = HostScreen.class.getSimpleName();

    private BluetoothInterface bluetoothInterface;

    private Label infoLabel;
    private TextField playerName;
    private CheckBox redPlayer;
    private CheckBox blackPlayer;
    private TextButton backButton;
    private TextButton hostButton;
    private Label colorLabel;
    private Label nameLabel;
    private Player player;


    public HostScreen(Checkers game) {
        super(game);
        bluetoothInterface = game.getBluetoothInterface();
        game.setHost(true);
        game.setMultiplayer(true);
        player = SaveUtil.loadUserData(Constants.USER_FILE);
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

    @Override
    public void show() {


        getTable().row();

        infoLabel = new Label("", getSkin(), "black-text");
        infoLabel.setAlignment(Align.center);
        getTable().add(infoLabel).spaceBottom(15).left().colspan(2);
        getTable().row();

        hostButton = new TextButton("Start Hosting", getSkin());
        getTable().add(hostButton).size(360, 70).center().colspan(2).uniform().spaceBottom(30);
        hostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                //save player data
                player.setName(bluetoothInterface.getName());
                player.setHost(true);
                player.setColor(Simple.BLACK);
                SaveUtil.saveUserData(Constants.USER_FILE, player);
                // Check if the Android device supports bluetooth.
                if (bluetoothInterface.isBluetoothSupported()) {
                    // Check if bluetooth is discoverable. If not, make it discoverable.
                    // This will also enable it, if it is disabled.
                    if (!bluetoothInterface.isBluetoothDiscoverable()) {
                        Gdx.app.log(LOG, "isBluetoothDiscoverable = "
                                + bluetoothInterface.isBluetoothDiscoverable());
                        bluetoothInterface.enableBluetoothDiscoverability();
                    } else {
                        Gdx.app.log(LOG, "isBluetoothDiscoverable = "
                                + bluetoothInterface.isBluetoothDiscoverable());
                        backButton.setVisible(true);
                        bluetoothInterface.startConnectionListening();
                    }
                }
                // The Android device does not support bluetooth.
                else {
                    infoLabel
                            .setText("Bluetooth not supported on this device.");
                }
            }
        });

        getTable().row();

        backButton = new TextButton("Back", getSkin());
        getTable().add(backButton).size(360, 70).center().colspan(2).uniform();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                bluetoothInterface.stopConnectionListening();
                game.setScreen(new MultiplayerScreen(game));
            }
        });


    }

    public void dispose() {
        super.dispose();
        Gdx.app.log(LOG, "Disposing HostScreen");
    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();

        game.setScreen(new MultiplayerScreen(game));
    }


}

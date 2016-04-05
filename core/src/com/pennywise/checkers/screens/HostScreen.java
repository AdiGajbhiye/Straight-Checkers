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

        nameLabel = new Label("Player name:", getSkin(), "black-text");
        nameLabel.setAlignment(Align.center);
        getTable().add(nameLabel).spaceBottom(15).left().colspan(2);
        getTable().row();

        playerName = new TextField("Player 1", getSkin());
        playerName.setCursorPosition(0);
        playerName.setAlignment(Align.left);
        getTable().add(playerName).size(360, 70).left().colspan(2).spaceBottom(30);
        getTable().row();

        colorLabel = new Label("Choose piece color", getSkin(), "black-text");
        colorLabel.setAlignment(Align.center);
        getTable().add(colorLabel).spaceBottom(15).left().colspan(2);
        getTable().row();

        redPlayer = new CheckBox("Red", getSkin(), "red");
        redPlayer.setChecked(true);
        redPlayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                CheckBox checkBox = (CheckBox) actor;
                if (checkBox.isChecked()) {
                    player.setColor(Simple.WHITE);
                    blackPlayer.setChecked(false);
                } else {
                    player.setColor(Simple.BLACK);
                    blackPlayer.setChecked(true);
                }
            }
        });

        getTable().add(redPlayer).size(180, 70).left().spaceBottom(30);


        blackPlayer = new CheckBox("Black", getSkin());
        blackPlayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                CheckBox checkBox = (CheckBox) actor;
                if (checkBox.isChecked()) {
                    redPlayer.setChecked(false);
                    player.setColor(Simple.BLACK);
                } else {
                    player.setColor(Simple.WHITE);
                    redPlayer.setChecked(true);
                }
            }
        });
        getTable().add(blackPlayer).size(180, 70).left().spaceBottom(30);

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
                player.setName(playerName.getText());
                player.setHost(true);
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

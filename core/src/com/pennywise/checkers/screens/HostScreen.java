package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private Player player;


    public HostScreen(Checkers game) {
        super(game);
        bluetoothInterface = game.getBluetoothInterface();
        game.setHost(true);
        game.setMultiplayer(true);
        player = SaveUtil.loadPlayer();
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    @Override
    public void show() {


        getTable().setBackground("dialog");
        getTable().pad(10);

        Label welcomeLabel = new Label("Host Game", getSkin());
        welcomeLabel.setAlignment(Align.center);
        Table gameTitle = new Table(getSkin());
        gameTitle.setBackground("dialog");
        gameTitle.add(welcomeLabel).center().size(Constants.GAME_WIDTH * 0.90f, 80);
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
        infoLabel.setWidth(Constants.GAME_WIDTH * 0.88f);
        infoLabel.setAlignment(Align.center);
        infoLabel.setWrap(true);
        menuContainer.left().add(infoLabel).pad(15);

        menuContainer.row();

        final TextButton game2Button = new TextButton("Start Hosting", getSkin());
        menuContainer.add(game2Button).size(360, 70).padTop(25).padBottom(25);
        game2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                //save player data
                player.setName(playerName.getText());
                player.setHost(true);
                player.setColor(Simple.BLACK);
                SaveUtil.savePlayer(player);
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

        menuContainer.row();

        final TextButton quit = new TextButton("Back", getSkin());
        menuContainer.add(quit).size(360, 70).padBottom(25).padTop(25);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (bluetoothInterface.isListening())
                    bluetoothInterface.stopConnectionListening();
                game.setScreen(new MultiplayerScreen(game));
            }
        });

        menuContainer.row();
        menuContainer.add().prefHeight(200);

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

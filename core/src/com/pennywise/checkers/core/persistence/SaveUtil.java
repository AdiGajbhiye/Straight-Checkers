package com.pennywise.checkers.core.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.engine.Simple;

/**
 * Created by Joshua.Nabongo on 2/4/2016.
 */
public class SaveUtil {

    private static final String FILENAME = "game.chk";

    public static boolean exists() {
        return Gdx.files.local(FILENAME).exists();
    }

    public static void save(GameObject obj) {

        FileHandle file = Gdx.files.local(FILENAME);

        Json json = new Json();
        String data = json.toJson(obj);
        file.writeString(data, false);

    }

    public static GameObject restore() {

        FileHandle file = Gdx.files.local(FILENAME);
        String data = file.readString();
        Json json = new Json();
        GameObject gameObject = json.fromJson(GameObject.class, data);
        return gameObject;

    }

    public static void savePlayer(Player player) {
        Preferences pref = Gdx.app.getPreferences(Constants.USER_FILE);
        pref.putString(Constants.NAME, player.getName());
        pref.putInteger(Constants.COLOR, player.getColor());
        pref.putBoolean(Constants.HOST, player.isHost());
        pref.flush();

    }

    public static Player loadPlayer() {
        Preferences pref = Gdx.app.getPreferences(Constants.USER_FILE);

        String name = pref.getString(Constants.NAME, "Player 1");
        int color = pref.getInteger(Constants.COLOR, Simple.BLACK);
        boolean hosting = pref.getBoolean(Constants.HOST, false);
        return new Player(name, color, hosting);

    }
}

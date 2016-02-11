package com.pennywise.checkers.core.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

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

}

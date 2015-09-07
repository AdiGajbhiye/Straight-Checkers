package com.pennywise.findwords.SaveHandler;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.pennywise.FindWords;

public class SaveHandler {

    FindWords game;

    private SaveDescriptor desc;

    private FileHandle file;

    public SaveHandler(FindWords game) {
        this.game = game;
        file = game.getFile();
        desc = new SaveDescriptor();
    }

    public void save() {
        desc.setScore(game.score);
        desc.setBest(game.best);
        desc.setSoundEnabled(game.isSoundEnabled());
        desc.setSaveCellArray(game.getGameScreen().gameField.saveCellArray);
        Json json = new Json();
        json.setOutputType(OutputType.json);
        file.writeString(Base64Coder.encodeString(json.toJson(desc)), false);
        System.out.println(json.toJson(desc));
    }

    public void load() {
        Json json = new Json();
        if (!file.exists()) {
            desc = new SaveDescriptor();
//			game.best = desc.getScore();
//			game.setSoundEnabled(desc.isSoundEnabled());
//			game.getGameScreen().gameField.saveCellArray = desc.getSaveCellArray();
        } else {
            desc = json.fromJson(SaveDescriptor.class, Base64Coder.decodeString(file.readString()));
//			game.best = desc.getScore();
//			game.setSoundEnabled(desc.isSoundEnabled());
//			game.getGameScreen().gameField.saveCellArray = desc.getSaveCellArray();
        }
        game.score = desc.getScore();
        game.best = desc.getBest();
        game.setSoundEnabled(desc.isSoundEnabled());
        game.getGameScreen().gameField.saveCellArray = desc.getSaveCellArray();
    }

}

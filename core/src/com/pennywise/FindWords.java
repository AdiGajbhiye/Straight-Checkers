package com.pennywise;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pennywise.findwords.MusicHandler.MusicHandler;
import com.pennywise.findwords.SaveHandler.SaveHandler;
import com.pennywise.findwords.screens.GameScreen;
import com.pennywise.findwords.screens.MainMenuScreen;

import java.util.Random;

public class FindWords extends Game {
    //public ActionResolver actionResolver;

    Screen mainMenuScreen;
    GameScreen gameScreen;

    private FileHandle file;
    private SaveHandler saveHandler;

    public Color background;
    public TextureAtlas atlas;
    public Skin skin;
    public TextureAtlas atlasNumber;
    public Skin skinNumber;
    public TextureAtlas atlasButton;
    public Skin skinButton;

    public int score = 0;
    public int best = 0;

    private boolean soundEnabled = true;

    public boolean isSigned = false;

    public FindWords() {

    }

    @Override
    public void create() {
        file = Gdx.files.local("bin/save.json");

        Random random = new Random();

        background = new Color(250 / 255f, 248 / 255f, 239 / 255f, 0.98f);

        atlas = new TextureAtlas("images/cell.atlas");
        skin = new Skin();
        skin.addRegions(atlas);
        atlasNumber = new TextureAtlas("images/number.atlas");
        skinNumber = new Skin();
        skinNumber.addRegions(atlasNumber);
        atlasButton = new TextureAtlas("images/button.atlas");
        skinButton = new Skin();
        skinButton.addRegions(atlasButton);

        mainMenuScreen = new MainMenuScreen(this);

        gameScreen = new GameScreen(this);

        saveHandler = new SaveHandler(this);
        saveHandler.load();

        System.out.println(score);

        if (score != 0)
            gameScreen.gameField.continueGame();
        else newGame();

        setScreen(mainMenuScreen);
        playMusic();

    }

    public void newGame() {
        score = 0;
        gameScreen = new GameScreen(this);
        gameScreen.gameField.newGame();
        setScreen(gameScreen);
    }

    public void playMusic() {
//		MusicHandler.playMusic(isMusicEnabled());
    }

    public void playClick() {
        if (soundEnabled)
            MusicHandler.playClick();
    }

    public void playSame() {
        if (soundEnabled)
            MusicHandler.playSame();
    }

    public void playSwipe() {
        if (soundEnabled)
            MusicHandler.playSwipe();
    }

    public Screen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public FileHandle getFile() {
        return file;
    }

    public void save() {
        saveHandler.save();
    }

//	public boolean isMusicEnabled() {
//		return musicEnabled;
//	}
//
//	public void setMusicEnabled(boolean musicEnabled) {
//		this.musicEnabled = musicEnabled;
//		playMusic();
//	}

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public GameScreen getGameScreen() {
//		score = 0;
        return gameScreen;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
//		MusicHandler.playMusic(false);
    }

    @Override
    public void resume() {
        super.resume();
//		MusicHandler.playMusic(isMusicEnabled());
    }
}

package com.pennywise.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joshua.Nabongo on 3/16/2016.
 */
public class AudioManager {

    private Random rnd;
    private ArrayList<Music> musicShuffleList;
    private Music currentMusic;
    private AudioManager audioManager;
    private boolean isShuffling = false;
    private float shuffleVolume = 0.5f;


    private static Sound explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
    private static Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
    private static Sound jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
    private static Sound mutate = Gdx.audio.newSound(Gdx.files.internal("sounds/mutate.wav"));
    private static Sound powerup = Gdx.audio.newSound(Gdx.files.internal("sounds/powerup.wav"));
    private static Sound randomize = Gdx.audio.newSound(Gdx.files.internal("sounds/randomize.wav"));
    private static Sound select = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
    private static Sound shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
    public static Music song = Gdx.audio.newMusic(Gdx.files.internal("sounds/DavidHasselhoff.mp3"));

    public AudioManager() {
        audioManager = new AudioManager();
        rnd = new Random();
        musicShuffleList = new ArrayList<Music>();
        currentMusic = null;
    }

    public void addMusicToShuffleList(Music music) {
        musicShuffleList.add(music);
    }

    public void playShuffle(float volume) {
        clearCurrentMusic();
        clearLoops();
        isShuffling = true;
        startShuffling(volume);
    }

    public void playMusic(Music music, boolean isLooping, float volume) {
        clearCurrentMusic();
        isShuffling = false;
        currentMusic = music;
        shuffleVolume = volume;
        audioManager.playMusic(currentMusic, isLooping, shuffleVolume);
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    private void clearLoops() {
        for (int i = 0; i < musicShuffleList.size(); i++) {
            Music m = musicShuffleList.get(i);
            m.setLooping(false);
        }
    }

    private void clearCurrentMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    private void startShuffling(float volume) {
        if (musicShuffleList.size() > 0) {
            int rndNumber = rnd.nextInt(musicShuffleList.size());
            currentMusic = musicShuffleList.get(rndNumber);
            audioManager.playMusic(currentMusic, false, volume);
        }
    }

    /**
     * This method must be checked in render() or similar all the time, for
     * shuffle list
     */
    public void checkShuffleMusicFinished() {
        if (isShuffling) {
            if (currentMusic != null) {
                if (!currentMusic.isPlaying()) {
                    startShuffling(shuffleVolume);
                }
            }
        }
    }

}

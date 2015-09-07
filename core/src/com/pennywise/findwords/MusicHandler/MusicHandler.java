package com.pennywise.findwords.MusicHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicHandler {

	private MusicHandler() {}
	
//	private static Music song = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
	private static Sound click = Gdx.audio.newSound(Gdx.files.internal("music/click.wav"));
	private static Sound same = Gdx.audio.newSound(Gdx.files.internal("music/same.wav"));
	private static Sound swipe = Gdx.audio.newSound(Gdx.files.internal("music/swipe.wav"));
	
	public static void playMusic(boolean play) {
//		song.setLooping(true);
//		if (play)
//			song.play();
//		else song.stop();
	}
	
	public static void playClick() {
		click.play();
	}
	
	public static void playSame() {
		same.play();
	}
	
	public static void playSwipe() {
		swipe.play();
	}

}

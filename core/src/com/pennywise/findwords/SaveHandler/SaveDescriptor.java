package com.pennywise.findwords.SaveHandler;


import com.pennywise.findwords.game.SaveCell;

public class SaveDescriptor {
	
	public int score;
	public int best;

	public SaveCell[][] saveCellArray;

	public boolean soundEnabled;

	public SaveDescriptor(){
		score = 0 ;
		best = 0;
		soundEnabled = true;
		saveCellArray = new SaveCell[4][4];
	}
	
	public int getBest() {
		return best;
	}

	public void setBest(int best) {
		this.best = best;
	}
	
	public SaveCell[][] getSaveCellArray() {
		return saveCellArray;
	}

	public void setSaveCellArray(SaveCell[][] saveCellArray) {
		this.saveCellArray = saveCellArray;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}
	
}

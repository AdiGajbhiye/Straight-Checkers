package com.pennywise.findwords.game;

import com.badlogic.gdx.math.Vector2;

public class SaveCell {

	public Vector2 position;
	public int value;
	
	public SaveCell(Vector2 position, int value){
		this.position = position;
		this.value = value;
	}
	
	public SaveCell(){
		
	}
}

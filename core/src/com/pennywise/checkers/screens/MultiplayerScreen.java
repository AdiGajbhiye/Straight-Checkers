package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.pennywise.Checkers;

public class MultiplayerScreen extends AbstractScreen {

	public static final String LOG = MultiplayerScreen.class.getSimpleName();

	private Label multiplayerLabel;
	private Label infoLabel;

	public MultiplayerScreen(Checkers game) {
		super(game);
	}

	@Override
	public void show() {
		//getTable().add(getLogoImage()).spaceBottom(10);
		getTable().row();

		multiplayerLabel = new Label("Human Challenger", getSkin(),"title-text");
		getTable().add(multiplayerLabel).spaceBottom(30);
		getTable().row();

		// If there is a bluetooth interface (that is, if we play on Android).
		if (Checkers.BLUETOOTH_INTERFACE_EXISTS) {
			final TextButton hostButton = new TextButton("Host", getSkin());
			getTable().add(hostButton).size(360, 70).uniform().spaceBottom(30);
			hostButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.setScreen(new HostScreen(game));
				}
			});
			getTable().row();

			final TextButton joinButton = new TextButton("Join", getSkin());
			getTable().add(joinButton).size(360, 70).uniform().spaceBottom(30);
			joinButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.setScreen(new JoinScreen(game));
				}
			});
			getTable().row();
		}
		// We are on desktop. No multiplayer.
		else {
			infoLabel = new Label(
					"Can't play multiplayer dude.\nBluetooth not supported on desktop.",
					getSkin());
			infoLabel.setAlignment(Align.center);
			getTable().add(infoLabel).colspan(2).spaceBottom(30);
			getTable().row();
		}

		final TextButton backButton = new TextButton("Back", getSkin());
		getTable().add(backButton).colspan(2).size(360, 70).uniform();
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new MenuScreen(game));
			}
		});
	}

	public void dispose() {
		super.dispose();
		Gdx.app.log(LOG, "Disposing MultiplayerScreen");
	}

}

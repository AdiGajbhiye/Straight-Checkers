package com.pennywise.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher {
    public static void main(String[] arg) {
        /*LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Checkers(), config);*/
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;

        TexturePacker.process(settings, "android\\assets\\background", "android\\assets\\images", "ui-pack");

        System.out.println("Done packing...");

    }
}

package com.pennywise.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.pennywise.Checkers;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Checkers";
        config.useGL30 = true;
        config.width = 480;
        config.height = 800;
		new LwjglApplication(new Checkers(), config);

        /*TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;

        TexturePacker.process(settings, "android\\assets\\background", "android\\assets", "ui-pack");

        System.out.println("Done packing...");
*/
    }
}

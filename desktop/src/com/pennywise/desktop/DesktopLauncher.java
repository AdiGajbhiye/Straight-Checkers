package com.pennywise.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.pennywise.Checkers;

public class DesktopLauncher {
    public static void main(String[] arg) {
      /*  LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Checkers";
        config.useGL30 = true;
        config.width = 480;
        config.height = 800;
		new LwjglApplication(new Checkers(), config);
<<<<<<< HEAD
/*
       TexturePacker.Settings settings = new TexturePacker.Settings();
=======
*/
        TexturePacker.Settings settings = new TexturePacker.Settings();
>>>>>>> c9878da0ff904731ef3622fde457b96404c1b543
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;

        TexturePacker.process(settings, "android\\assets\\background", "android\\assets", "ui-pack");

<<<<<<< HEAD
        System.out.println("Done packing...");*/
=======
        System.out.println("Done packing...");
>>>>>>> c9878da0ff904731ef3622fde457b96404c1b543

    }
}

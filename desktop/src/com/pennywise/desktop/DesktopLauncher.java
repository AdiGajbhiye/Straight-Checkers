package com.pennywise.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.pennywise.Checkers;

public class DesktopLauncher {
    public static void main(String[] arg) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        TexturePacker.process(settings, "android\\assets\\background", "android\\assets", "ui-pack");
        System.out.println("Done packing...");
    }
}

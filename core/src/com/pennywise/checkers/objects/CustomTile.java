package com.pennywise.checkers.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by CHOXXY on 6/20/2015.
 */
public class CustomTile extends Actor implements Disposable {

    private final BitmapFont smallFont;
    private final BitmapFont textFont;
    private Vector2 position;
    private String superScript;
    private boolean hintTile;
    private String text;
    private SpriteDrawable background;
    private boolean showvValue;

    public CustomTile(String tileValue, String superScript, SpriteDrawable background,
                      BitmapFont font, boolean showvValue) {

        position = new Vector2(0, 0);
        this.superScript = superScript;
        this.text = tileValue;
        this.background = background;
        this.textFont = font;
        this.showvValue = showvValue;
        this.smallFont = new BitmapFont(Gdx.files.internal("fonts/superscript_white.fnt"));
        this.smallFont.getData().setScale(0.60f, -0.60f);

    }

    public String getTileValue() {
        return text;
    }

    public void setBackground(SpriteDrawable background) {
        this.background = background;
    }

    public String getSuperScript() {
        return superScript;
    }

    public boolean isBlank() {
        return StringUtils.isBlank(superScript);
    }

    public void setTileValue(String tileValue) {
        this.text = tileValue;
    }

    public boolean isHintTile() {
        return hintTile;
    }

    public void setHintTile(boolean hintTile) {
        this.hintTile = hintTile;
    }

    public void setShowvValue(boolean showvValue) {
        this.showvValue = showvValue;
    }

    public boolean isShowvValue() {
        return showvValue;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // super.draw(batch, parentAlpha);

        background.draw(batch, getX(), getY(), getWidth(), getHeight());

        if (!StringUtils.isBlank(superScript)) {
            position = position.cpy().set(getX() + (getWidth() * 0.70f), (getY() + getHeight() * 0.10f));
            smallFont.draw(batch, String.valueOf(superScript), position.x, position.y);
        }

        if (showvValue) {
            GlyphLayout glyphLayout = new GlyphLayout();
            glyphLayout.setText(textFont, text);
            float textWidth = glyphLayout.width;
            float textHeight = glyphLayout.height;
            float width = getWidth(), height = getHeight();

            float x = (width - textWidth) / 2;
            float y = (height - textHeight) / 8;

            textFont.draw(batch, glyphLayout, getX() + x, getY() + y);
        }
    }

    @Override
    public void dispose() {
        smallFont.dispose();
    }
}

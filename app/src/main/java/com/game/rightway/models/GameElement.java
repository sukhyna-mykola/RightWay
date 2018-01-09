package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.game.rightway.GameSurface;

/**
 * Created by mykola on 02.01.18.
 */

public abstract class GameElement {

    protected GameSurface view;
    protected RectF shape;
    protected Paint paint = new Paint();
    protected int color;


    public GameElement(float x, float y, float width, float height, GameSurface mView, int color) {
        this.view = mView;
        this.color = color;

        shape = new RectF(x, y, x + width, y + height);
        paint.setColor(color);
    }


    public abstract void draw(Canvas c);

    public abstract void update(int interval);

    public void playSound(int soundId) {
        view.playSound(soundId);
    }


}

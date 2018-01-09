package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.game.rightway.GameSurface;


public abstract class GameElement {

    protected GameSurface view;
    protected RectF shape;
    protected int color;

    protected Paint paint = new Paint();


    public GameElement(float x, float y, float width, float height, GameSurface mView, int color) {
        this.view = mView;
        this.color = color;

        shape = new RectF(x, y, x + width, y + height);

        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }


    public abstract void draw(Canvas c);

    public abstract void update(int interval);

    public void playSound(int soundId) {
        view.playSound(soundId);
    }


}

package com.game.rightway.models;

import android.graphics.Canvas;

import com.game.rightway.GameSurface;

public class SnakeCell extends GameElement {


    public SnakeCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color) {
        super(mX, mY, mWidth, mHeight, mView, color);
    }

    public float getRadius() {
        return shape.height() / 2;
    }

    protected void changeColor(int mColor) {
        if (color != mColor) {
            this.color = mColor;
            paint.setColor(color);
        }
    }

    @Override
    public void draw(Canvas c) {
        c.drawCircle(shape.centerX(), shape.centerY(), getRadius(), paint);
    }

    @Override
    public void update(int interval) {
        //
    }

}

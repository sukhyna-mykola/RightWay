package com.game.rightway.models;

import com.game.rightway.GameSurface;

/**
 * Created by mykola on 02.01.18.
 */

public abstract class WayElement extends GameElement {
    protected int count;

    protected static float speed = 0.5f;

    public WayElement(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color);
        count = mCount;
    }

    @Override
    public void update(int interval) {
        shape.offset(0, speed * interval);
    }


    public abstract boolean intersect(Snake mSnake);
}

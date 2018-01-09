package com.game.rightway.models;

import com.game.rightway.GameSurface;


public abstract class WayElement extends GameElement {
    protected int points;

    protected static float speed = 0.5f;

    public WayElement(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int points) {
        super(mX, mY, mWidth, mHeight, mView, color);
        this.points = points;
    }

    @Override
    public void update(int interval) {
        shape.offset(0, speed * interval);
    }


    public abstract boolean intersect(Snake mSnake);
}

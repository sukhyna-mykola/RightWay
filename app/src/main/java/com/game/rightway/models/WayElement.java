package com.game.rightway.models;

import com.game.rightway.GameSurface;

import java.util.List;

import static com.game.rightway.models.WayElements.velocity;


public abstract class WayElement extends GameElement {
    protected int points;

    public WayElement(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int points) {
        super(mX, mY, mWidth, mHeight, mView, color);
        this.points = points;
    }

    @Override
    public void update(int interval) {
        shape.offset(0, velocity * interval);
    }

    public abstract List<Part> generateParts();

    public abstract boolean intersect(Snake mSnake);
}

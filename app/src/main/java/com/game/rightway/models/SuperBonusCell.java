package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

import java.util.ArrayList;
import java.util.List;


public class SuperBonusCell extends WayElement {

    private static final float BONUS_TEXT_SIZE_PERCENT = 0.025f;

    private static final int MIN_PARTS_NUMBER = 12;
    private static final int MIN_PART_SIZE = 5;

    public SuperBonusCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);

        paint.setTextSize(BONUS_TEXT_SIZE_PERCENT * mView.getHeightScreen());
    }

    @Override
    public void draw(Canvas c) {
        //draw bonus circle
        c.rotate(45f, shape.centerX(), shape.centerY());
        c.drawRect(shape, paint);
        c.rotate(-45, shape.centerX(), shape.centerY());
    }


    @Override
    public List<Part> generateParts() {
        List<Part> parts = new ArrayList<>();
        int number = Utils.RANDOM.nextInt(MIN_PARTS_NUMBER) + MIN_PARTS_NUMBER;

        for (int i = 0; i < number; i++) {
            float size = Utils.RANDOM.nextInt(MIN_PART_SIZE) + MIN_PART_SIZE;

            parts.add(new SuperBonusPart(shape.centerX(), shape.centerY(), size, size, view, color));
        }
        return parts;
    }

    private float getRadius() {
        return shape.width() / 2;
    }

    @Override
    public boolean intersect(Snake mSnake) {
        if (RectF.intersects(mSnake.getSnakeHead().shape, shape)) {
            playSound(GameSurface.BONUS_SOUND_ID);
            mSnake.addSuper(points);
            points = 0;
            return true;
        }
        return false;
    }

    private class SuperBonusPart extends Part {

        public SuperBonusPart(float x, float y, float width, float height, GameSurface mView, int color) {
            super(x, y, width, height, mView, color);
        }

        @Override
        public void draw(Canvas c) {
            c.drawCircle(shape.centerX(), shape.centerY(), shape.width() / 2, paint);
        }
    }
}

package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

import java.util.ArrayList;
import java.util.List;


public class BlockCell extends WayElement {

    private static final float BLOCK_TEXT_SIZE_PERCENT = 0.06f;
    private static final float BLOCK_RADIUS_PERCENT = 0.04f;

    private static final int MIN_PARTS_NUMBER = 15;
    private static final int MIN_PART_SIZE = 5;

    public BlockCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);
        paint.setTextSize(BLOCK_TEXT_SIZE_PERCENT * mView.getHeightScreen());
    }

    @Override
    public void draw(Canvas c) {
        paint.setColor(generateColor(points));

        //draw block
        int radius = (int) (view.getWidthScreen() * BLOCK_RADIUS_PERCENT);
        c.drawRoundRect(shape, radius, radius, paint);
        //draw points
        paint.setColor(Color.WHITE);
        c.drawText(String.valueOf(points), shape.centerX(), shape.centerY() - (paint.descent() + paint.ascent()) / 2, paint);
    }

    @Override
    public void update(int interval) {
        super.update(interval);
    }

    @Override
    public List<Part> generateParts() {
        List<Part> parts = new ArrayList<>();
        int number = Utils.RANDOM.nextInt(MIN_PARTS_NUMBER) + MIN_PARTS_NUMBER;

        for (int i = 0; i < number; i++) {
            float size = Utils.RANDOM.nextInt(MIN_PART_SIZE * 2) + MIN_PART_SIZE;

            parts.add(new BlockPart(shape.centerX(), shape.centerY(), size, size, view, generateColor(size - MIN_PART_SIZE)));
        }
        return parts;
    }

    private int generateColor(float mSize) {

        if (mSize < 3) {
            return (Color.CYAN);
        } else {
            if (mSize >= 3 && mSize < 6) {
                return (Color.GREEN);
            } else {
                if (mSize >= 6 && mSize < 10) {
                    return (Color.parseColor("#E91E63"));//pink
                } else {
                    if (mSize >= 10 && mSize < 15) {
                        return (Color.parseColor("#FF9800"));//orange
                    } else {
                        return (Color.BLUE);
                    }
                }
            }
        }
    }

    @Override
    public boolean intersect(Snake mSnake) {
        if (RectF.intersects(mSnake.getSnakeHead().shape, shape)) {
            playSound(GameSurface.BLOCKER_SOUND_ID);
            if (mSnake.isSuper()) {
                mSnake.addPoints(points);
                points = 0;
            } else {
                mSnake.removeCell();
                mSnake.addPoints(1);
                points--;

                if (WayElements.velocity > WayElements.MIN_VELOCITY)
                    WayElements.velocity -= 0.05;
            }

            return true;
        }
        return false;
    }

    private class BlockPart extends Part {

        public BlockPart(float x, float y, float width, float height, GameSurface mView, int color) {
            super(x, y, width, height, mView, color);
        }

        @Override
        public void draw(Canvas c) {
            float angle = Utils.RANDOM.nextInt(90);
            c.rotate(angle, shape.centerX(), shape.centerY());
            super.draw(c);
            c.rotate(-angle, shape.centerX(), shape.centerY());
        }

    }
}
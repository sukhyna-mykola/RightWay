package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.game.rightway.GameSurface;

/**
 * Created by mykola on 02.01.18.
 */

public class BonusCell extends WayElement {
    public static final float TEXT_SIZE_PERCENT = 0.025f;

    public BonusCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(TEXT_SIZE_PERCENT * mView.getHeightScreen());
    }

    @Override
    public void draw(Canvas c) {
        c.drawCircle(shape.centerX(), shape.centerY(), getRadius(), paint);
        c.drawText(String.valueOf(count), shape.centerX(), shape.top - shape.height() / 2, paint);
    }

    @Override
    public void update(int interval) {
        super.update(interval);
    }

    private float getRadius() {
        return shape.width() / 2;
    }

    @Override
    public boolean intersect(Snake mSnake) {

        if (RectF.intersects(mSnake.getSnakeHead().shape, shape)) {
            playSound(GameSurface.BONUS_SOUND_ID);
            mSnake.addCells(count);
            return true;
        }

        return false;
    }

}

package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.game.rightway.GameSurface;


public class BonusCell extends WayElement {

    private static final float BONUS_TEXT_SIZE_PERCENT = 0.025f;

    public BonusCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);

        paint.setTextSize(BONUS_TEXT_SIZE_PERCENT * mView.getHeightScreen());
    }

    @Override
    public void draw(Canvas c) {
        //draw bonus circle
        paint.setColor(Color.YELLOW);
        c.drawCircle(shape.centerX(), shape.centerY(), getRadius(), paint);
        //draw points
        paint.setColor(Color.WHITE);
        c.drawText(String.valueOf(points), shape.centerX(), shape.top - shape.height() / 2, paint);
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
            mSnake.addCells(points);
            return true;
        }
        return false;
    }

}

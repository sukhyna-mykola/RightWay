package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.game.rightway.GameSurface;

/**
 * Created by mykola on 02.01.18.
 */

public class BlockCell extends WayElement {

    public static final float TEXT_SIZE_PERCENT = 0.06f;
    private static final float BLOCK_RADIUS_PERCENT = 0.04f;

    public BlockCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);
        paint.setTextSize(TEXT_SIZE_PERCENT * mView.getHeightScreen());
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas c) {

        if (count < 3) {
            paint.setColor(Color.CYAN);
        } else {
            if (count >= 3 && count < 7) {
                paint.setColor(Color.GREEN);
            } else {
                if (count >= 7 && count < 12) {
                    paint.setColor(Color.RED);
                } else {
                    if (count >= 12 && count < 15) {
                        paint.setColor(Color.BLUE);
                    } else {
                        paint.setColor(Color.GRAY);
                    }
                }
            }
        }


        int radius = (int) (view.getWidthScreen() * BLOCK_RADIUS_PERCENT);
        c.drawRoundRect(shape, radius, radius, paint);

        paint.setColor(Color.WHITE);
        c.drawText(String.valueOf(count), shape.centerX(), shape.centerY() - (paint.descent() + paint.ascent()) / 2, paint);
    }

    @Override
    public void update(int interval) {
        super.update(interval);
    }

    @Override
    public boolean intersect(Snake mSnake) {
        if (RectF.intersects(mSnake.getSnakeHead().shape, shape)) {
            playSound(GameSurface.BLOCKER_SOUND_ID);
            mSnake.removeCell();
            count--;

            if (count <= 0) {
                return true;
            } else {

                return false;
            }
        }
        return false;
    }


}
package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;

import com.game.rightway.GameSurface;


public class BlockCell extends WayElement {

    private static final float BLOCK_TEXT_SIZE_PERCENT = 0.06f;
    private static final float BLOCK_RADIUS_PERCENT = 0.04f;



    public BlockCell(float mX, float mY, float mWidth, float mHeight, GameSurface mView, int color, int mCount) {
        super(mX, mY, mWidth, mHeight, mView, color, mCount);
        paint.setTextSize(BLOCK_TEXT_SIZE_PERCENT * mView.getHeightScreen());

    }

    @Override
    public void draw(Canvas c) {

        if (points < 3) {
            paint.setColor(Color.CYAN);
        } else {
            if (points >= 3 && points < 6) {
                paint.setColor(Color.GREEN);
            } else {
                if (points >= 6 && points <10 ) {
                    paint.setColor(Color.parseColor("#E91E63"));//pink
                } else {
                    if (points >= 10 && points < 15) {
                        paint.setColor(Color.parseColor("#FF9800"));//orange
                    } else {
                        paint.setColor(Color.BLUE);
                    }
                }
            }
        }

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
    public boolean intersect(Snake mSnake) {
        if (RectF.intersects(mSnake.getSnakeHead().shape, shape)) {
            playSound(GameSurface.BLOCKER_SOUND_ID);
            mSnake.removeCell();

            points--;

            if (points <= 0) {
                return true;
            } else {

                return false;
            }
        }
        return false;
    }


}
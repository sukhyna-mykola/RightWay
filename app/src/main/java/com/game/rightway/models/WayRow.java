package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

import static com.game.rightway.models.WayElement.speed;

public class WayRow {

    public static final float ROW_WIDTH_PERCENT = 1f / 5;
    public static final float ROW_HEIGHT_PERCENT = 1f / 5;

    public static final float BLOCK_WIDTH_PERCENT = 0.19f;
    public static final float BLOCK_HEIGHT_PERCENT = 0.19f;

    public static final float BONUS_WIDTH_PERCENT = 1f / 20;
    public static final float BONUS_HEIGHT_PERCENT = 1f / 20;
    private static final double IS_ADD_ELEMENT_PERCENT = 0.83f;
    private static final double LINE_GENERATION_PERCENT = 0.13f;

    private WayElement[] row;
    private float y;
    private float widthRow;
    private float heightRow;


    public WayRow(GameSurface mSurface, int rowLenght, int total) {
        row = new WayElement[rowLenght];

        y = -mSurface.getWidthScreen() * ROW_WIDTH_PERCENT;

        widthRow = mSurface.getWidthScreen() * ROW_WIDTH_PERCENT;
        heightRow = mSurface.getWidthScreen() * ROW_HEIGHT_PERCENT;

        if (Utils.RANDOM.nextDouble() < LINE_GENERATION_PERCENT) {
            generateLine(mSurface, total);
        } else {
            generateRandom(mSurface, total);
        }

    }

    private void generateLine(GameSurface mSurface, int mTotal) {
        for (int i = 0; i < row.length; i++) {
            float width = mSurface.getWidthScreen() * BLOCK_WIDTH_PERCENT;
            float height = mSurface.getWidthScreen() * BLOCK_HEIGHT_PERCENT;
            row[i] = new BlockCell(i * widthRow, -widthRow, width, height, mSurface, Color.RED, generateBlockCoins(mTotal));

        }
    }

    private void generateRandom(GameSurface mSurface, int mTotal) {

        for (int i = 0; i < row.length; i++) {
            if (Utils.RANDOM.nextDouble() > IS_ADD_ELEMENT_PERCENT) {//чи додавати бонус(перешкоду)
                if (Utils.RANDOM.nextBoolean()) {//додати перешкоду
                    float width = mSurface.getWidthScreen() * BLOCK_WIDTH_PERCENT;
                    float height = mSurface.getWidthScreen() * BLOCK_HEIGHT_PERCENT;
                    row[i] = new BlockCell(i * widthRow, -widthRow, width, height, mSurface, Color.RED, generateBlockCoins(mTotal));
                } else {//додати бонус
                    float width = mSurface.getWidthScreen() * BONUS_WIDTH_PERCENT;
                    float height = mSurface.getWidthScreen() * BONUS_HEIGHT_PERCENT;
                    row[i] = new BonusCell(i * widthRow + heightRow / 2, -heightRow + heightRow / 2, width, height, mSurface, Color.YELLOW, generateBonusCoins(mTotal));
                }

            }
        }
    }

    private int generateBonusCoins(int mTotal) {
        int log = (int) Math.log((mTotal + Math.E));
        return Utils.RANDOM.nextInt(log) + log / 2 + 1;
    }

    private int generateBlockCoins(int mTotal) {
        int log = (int) Math.log((mTotal + Math.E) * 2);
        return Utils.RANDOM.nextInt(log * 2) + log / 2 + 1;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas mCanvas) {
        for (WayElement e : row) {
            if (e != null)
                e.draw(mCanvas);
        }
    }

    public void update(int interval) {
        y += speed * interval;

        for (WayElement e : row) {
            if (e != null)
                e.update(interval);
        }
    }

    public WayElement getElement(int position) {
        return row[position];
    }

    public void setElement(int position, WayElement mWayElement) {
        row[position] = mWayElement;
    }

}

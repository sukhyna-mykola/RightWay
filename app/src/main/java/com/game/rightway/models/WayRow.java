package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

import static com.game.rightway.models.WayElements.velocity;

public class WayRow {

    public static final float ROW_WIDTH_PERCENT = 1f / 5;
    public static final float ROW_HEIGHT_PERCENT = 1f / 5;

    public static final float BLOCK_WIDTH_PERCENT = 0.19f;
    public static final float BLOCK_HEIGHT_PERCENT = 0.19f;

    public static final float BONUS_WIDTH_PERCENT = 1f / 20;
    public static final float BONUS_HEIGHT_PERCENT = 1f / 20;

    public static final float SUPER_BONUS_WIDTH_PERCENT = 1f / 25;
    public static final float SUPER_BONUS_HEIGHT_PERCENT = 1f / 25;

    private static final double IS_ADD_ELEMENT_PERCENT = 0.83f;
    private static final double LINE_GENERATION_PERCENT = 0.13f;

    private static final int MIN_SUPER_TIME = 10000;
    private static final double SUPER_TIME_PERCENT = 0.01f;

    private WayElement[] row;

    private float y;
    private float widthRow, heightRow;


    public WayRow(GameSurface mSurface, int rowLenght, int total) {
        row = new WayElement[rowLenght];

        widthRow = mSurface.getWidthScreen() * ROW_WIDTH_PERCENT;
        heightRow = mSurface.getWidthScreen() * ROW_HEIGHT_PERCENT;

        if (Utils.RANDOM.nextDouble() < LINE_GENERATION_PERCENT) {
            generateLine(mSurface, total);
        } else {
            generateRandom(mSurface, total);
        }

        y = -heightRow;
    }

    private void generateLine(GameSurface mSurface, int mTotal) {
        for (int i = 0; i < row.length; i++) {
            float width = mSurface.getWidthScreen() * BLOCK_WIDTH_PERCENT;
            float height = mSurface.getWidthScreen() * BLOCK_HEIGHT_PERCENT;
            row[i] = new BlockCell(i * widthRow + (widthRow - width) / 2, -heightRow + (heightRow - height) / 2, width, height, mSurface, Color.RED, generateBlockPoints(mTotal));

        }
    }

    private void generateRandom(GameSurface mSurface, int mTotal) {

        for (int i = 0; i < row.length; i++) {
            if (Utils.RANDOM.nextDouble() > IS_ADD_ELEMENT_PERCENT) {//чи додавати бонус(перешкоду)
                if (Utils.RANDOM.nextBoolean()) {//додати перешкоду
                    float width = mSurface.getWidthScreen() * BLOCK_WIDTH_PERCENT;
                    float height = mSurface.getWidthScreen() * BLOCK_HEIGHT_PERCENT;
                    row[i] = new BlockCell(i * widthRow + (widthRow - width) / 2, -heightRow + (heightRow - height) / 2, width, height, mSurface, Color.RED, generateBlockPoints(mTotal));
                } else {//додати бонус

                    if (Utils.RANDOM.nextDouble() > SUPER_TIME_PERCENT) {
                        float width = mSurface.getWidthScreen() * BONUS_WIDTH_PERCENT;
                        float height = mSurface.getWidthScreen() * BONUS_HEIGHT_PERCENT;
                        row[i] = new BonusCell(i * widthRow + widthRow / 2 - width / 2, -heightRow + heightRow / 2 - height / 2, width, height, mSurface, Color.YELLOW, generateBonusPoints(mTotal));

                    } else {
                        float width = mSurface.getWidthScreen() * SUPER_BONUS_WIDTH_PERCENT;
                        float height = mSurface.getWidthScreen() * SUPER_BONUS_HEIGHT_PERCENT;
                        row[i] = new SuperBonusCell(i * widthRow + widthRow / 2 - width / 2, -heightRow + heightRow / 2 - height / 2, width, height, mSurface, Color.CYAN, generateSuperTime());
                    }
                }

            }
        }
    }

    private int generateSuperTime() {
        return Utils.RANDOM.nextInt(100) * 100 + MIN_SUPER_TIME;
    }

    private int generateBonusPoints(int mTotal) {
        int log = (int) Math.log((mTotal + Math.E));
        return Utils.RANDOM.nextInt(log) + log / 2 + 1;
    }

    private int generateBlockPoints(int mTotal) {
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
        y += velocity * interval;

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

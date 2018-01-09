package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class WayElements {
    private static final float RADIUS_POINTS_PERCENT = 0.07f;
    private static final int ROW_LENGHT = 5;

    private static final int MIN_PARTS_NUMBER = 12;
    private static final int MIN_PART_SIZE = 3;

    private static final float POINTS_TEXT_SIZE_PERCENT = 0.07f;

    private GameSurface mSurface;

    private LinkedList<WayRow> mWayRows;
    private List<Part> parts;

    private int totalRows;

    private Paint paint;

    public WayElements(GameSurface mSurface) {
        this.mSurface = mSurface;

        mWayRows = new LinkedList<>();
        parts = new ArrayList<>();

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(POINTS_TEXT_SIZE_PERCENT * mSurface.getHeightScreen());

        addNewRow(mSurface);

    }

    private void addNewRow(GameSurface mSurface) {
        WayRow row = new WayRow(mSurface, ROW_LENGHT, totalRows);
        mWayRows.addFirst(row);
        totalRows++;
    }


    public void update(int interval) {

        WayRow first = mWayRows.getFirst();
        if (first.getY() >= 0)
            addNewRow(mSurface);

        WayRow last = mWayRows.getLast();
        if (last.getY() > mSurface.getHeightScreen())
            mWayRows.remove(last);

        for (WayRow row : mWayRows) {
            row.update(interval);
        }

        Iterator<Part> partsIterator = parts.iterator();
        while (partsIterator.hasNext()) {
            GameElement e = partsIterator.next();

            if (e.shape.left < 0 || e.shape.left > e.view.getWidthScreen() &&
                    e.shape.top < 0 || e.shape.top > e.view.getHeightScreen()) {

                partsIterator.remove();
            } else {
                e.update(interval);
            }
        }
    }


    public void draw(Canvas c) {
        for (WayRow row : mWayRows) {
            row.draw(c);
        }

        for (Part p : parts) {
            p.draw(c);
        }

        paint.setColor(Color.WHITE);
        c.drawCircle(mSurface.getWidthScreen() / 2, RADIUS_POINTS_PERCENT * mSurface.getHeightScreen(), RADIUS_POINTS_PERCENT * mSurface.getHeightScreen(), paint);

        paint.setColor(Color.BLACK);
        c.drawText(String.valueOf(totalRows / 2), mSurface.getWidthScreen() / 2, RADIUS_POINTS_PERCENT * mSurface.getHeightScreen() - (paint.descent() + paint.ascent()) / 2, paint);
    }


    public void checkIntersects(Snake s) {
        for (WayRow row : mWayRows) {
            if (row.getY() >= mSurface.getHeightScreen() / 3)
                for (int i = 0; i < ROW_LENGHT; i++) {
                    WayElement e = row.getElement(i);
                    if (e != null && !s.isDead()) {
                        if (e.intersect(s)) {
                            row.setElement(i, null);
                            generateParts(e);
                        }
                    }
                }
        }
    }

    private void generateParts(WayElement mE) {
        int number = Utils.RANDOM.nextInt(MIN_PARTS_NUMBER) + MIN_PARTS_NUMBER;

        for (int i = 0; i < number; i++) {
            float w = Utils.RANDOM.nextInt(MIN_PART_SIZE) + MIN_PART_SIZE;
            float h = Utils.RANDOM.nextInt(MIN_PART_SIZE) + MIN_PART_SIZE;

            parts.add(new Part(mE.shape.centerX(), mE.shape.centerY(), w, h, mSurface, mE.color));
        }
    }


    public int getTotalRows() {
        return totalRows;
    }
}

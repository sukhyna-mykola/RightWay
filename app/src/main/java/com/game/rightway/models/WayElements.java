package com.game.rightway.models;

import android.graphics.Canvas;
import android.util.Log;

import com.game.rightway.GameSurface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class WayElements {

    protected static float velocity;

    protected static final float MIN_VELOCITY = 0.3f;
    protected static final float START_VELOCITY = 0.5f;
    protected static final float MAX_VELOCITY = 0.9f;

    private static final int ROW_LENGHT = 5;

    private GameSurface mSurface;

    private LinkedList<WayRow> mWayRows;
    private List<Part> parts;

    private int totalRows;


    public WayElements(GameSurface mSurface) {
        this.mSurface = mSurface;

        velocity = START_VELOCITY;

        mWayRows = new LinkedList<>();
        parts = new ArrayList<>();

        addNewRow(mSurface);
    }

    private void addNewRow(GameSurface mSurface) {
        WayRow row = new WayRow(mSurface, ROW_LENGHT, totalRows);
        mWayRows.addFirst(row);
        totalRows++;
    }


    public void update(int interval) {

        if (velocity < MAX_VELOCITY) {
            if (velocity < START_VELOCITY)
                velocity += 0.01f;

            velocity += interval / 250_000.0;
        }

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

    }

    public void checkIntersects(Snake s) {
        for (WayRow row : mWayRows) {
            if (row.getY() >= mSurface.getHeightScreen() / 3)
                for (int i = 0; i < ROW_LENGHT; i++) {
                    WayElement e = row.getElement(i);
                    if (e != null && !s.isDead()) {
                        if (e.intersect(s)) {
                            if (e.points == 0) {
                                row.setElement(i, null);
                                parts.addAll(e.generateParts());
                            } else {
                                parts.addAll(s.generateParts());
                            }
                        }
                    }
                }
        }
    }

}

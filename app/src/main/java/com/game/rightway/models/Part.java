package com.game.rightway.models;

import android.graphics.Canvas;

import com.game.rightway.GameSurface;
import com.game.rightway.helpers.Utils;

/**
 * Created by mykola on 09.01.18.
 */

public class Part extends GameElement {
    private float dx, dy;

    public Part(float x, float y, float width, float height, GameSurface mView, int color) {
        super(x, y, width, height, mView, color);

        dx = Utils.RANDOM.nextFloat() + 0.05f;
        if (Utils.RANDOM.nextBoolean())
            dx = -dx;

        dy = Utils.RANDOM.nextFloat() + 0.05f;
        if (Utils.RANDOM.nextBoolean())
            dy = -dy;


    }


    @Override
    public void draw(Canvas c) {
        c.drawRect(shape, paint);
    }

    @Override
    public void update(int interval) {
        shape.offset(dx * interval, dy * interval);
    }


}

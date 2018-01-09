package com.game.rightway.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.game.rightway.GameSurface;

import java.util.LinkedList;


public class Snake {
    private static final float SNAKE_ELEMENT_WIDTH_PERCENT = 1f / 20;
    private static final float SNAKE_ELEMENT_HEIGHT_PERCENT = 1f / 20;

    private static final float SNAKE_BOTTOM_VELOCITY_PERCENT = 0.5f;
    private static final float SNAKE_TOP_VELOCITY_PERCENT = -1f;

    private static final float SNAKE_TEXT_SIZE_PERCENT = 0.025f;

    private LinkedList<SnakeCell> snake;
    private Paint paint;

    public Snake(GameSurface mSurface, int startSize) {
        snake = new LinkedList<>();

        float widthCell = mSurface.getWidthScreen() * SNAKE_ELEMENT_WIDTH_PERCENT;
        float heightCell = mSurface.getWidthScreen() * SNAKE_ELEMENT_HEIGHT_PERCENT;

        for (int i = 0; i < startSize; i++) {
            snake.add(new SnakeCell(mSurface.getWidthScreen() / 2, mSurface.getHeightScreen() / 2 + i * heightCell,
                    widthCell, heightCell, mSurface, Color.YELLOW));

        }

        paint = new Paint();
        configurePaint(mSurface.getHeightScreen());
    }

    private void configurePaint(float height) {
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTextSize(SNAKE_TEXT_SIZE_PERCENT * height);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }


    public void addCells(int number) {
        SnakeCell last = snake.getLast();

        for (int i = 0; i < number; i++) {
            SnakeCell newCell = new SnakeCell(last.shape.left, last.shape.top, last.shape.width(), last.shape.height(), last.view, Color.YELLOW);
            snake.addLast(newCell);
        }
    }


    public void removeCell() {
        if (!isDead())
            snake.removeFirst();
    }

    public void moveHead(float x) {
        if (!isDead()) {

            SnakeCell head = getSnakeHead();
            //check right border
            if (x < head.view.getWidthScreen() - head.shape.width()) {
                head.shape.offsetTo(x, head.shape.top);
            } else {
                head.shape.offsetTo(head.view.getWidthScreen() - head.shape.width(), head.shape.top);
            }
        }
    }

    public void update(int interval) {
        SnakeCell head = getSnakeHead();
        //horizontal
        for (int i = 1; i < snake.size(); i++) {
            SnakeCell cell = snake.get(i);
            if (head.shape.left != cell.shape.left) {
                float diff = head.shape.left - cell.shape.left;
                cell.shape.offset(diff / (i + 1), 0);
            }
        }

        //vertical
        for (int i = 0; i < getSnakeSize(); i++) {
            SnakeCell cell = snake.get(i);

            float y = cell.shape.top;
            float targetY = cell.view.getHeightScreen() / 2 + i * cell.shape.height();

            if (targetY < cell.view.getHeightScreen()) {
                if (y != targetY) {
                    if (y - targetY > 0) {
                        cell.shape.offset(0, ((1.5f / cell.view.getHeightScreen()) * y - 0.5f) * SNAKE_TOP_VELOCITY_PERCENT * interval);
                        if (cell.shape.top < targetY)
                            cell.shape.offsetTo(cell.shape.left, targetY);
                    } else {
                        cell.shape.offset(0, SNAKE_BOTTOM_VELOCITY_PERCENT * interval);
                    }
                }
            } else {
                cell.shape.offsetTo(cell.shape.left, cell.view.getHeightScreen());
            }
        }
    }


    public void draw(Canvas c) {
        //draw snake cells
        for (SnakeCell e : snake) {
            e.draw(c);
        }

        //draw snake size text
        SnakeCell head = getSnakeHead();
        c.drawText(String.valueOf(getSnakeSize()), head.shape.centerX(), head.shape.centerY() - head.shape.height(), paint);
    }

    public SnakeCell getSnakeHead() {
        if (!isDead())
            return snake.getFirst();
        else return null;
    }

    public int getSnakeSize() {
        return snake.size();
    }

    public boolean isDead() {
        return snake.isEmpty();
    }
}

package com.game.rightway;

import com.game.rightway.models.Snake;
import com.game.rightway.models.WayElements;

/**
 * Created by mykola on 02.01.18.
 */

public class GameLogic {

    public static final int SNAKE_START_SIZE = 10;

    private Snake snake;
    private WayElements wayElements;

    private GamePresenter mPresenter;

    public GameLogic(GamePresenter mGamePresenter) {
        this.mPresenter = mGamePresenter;
    }


    public void updateData(int interval) {
        if (snake.isDead())
            mPresenter.endGame();

        checkIntersections();
        snake.update(interval);
        wayElements.update(interval);
    }

    private void checkIntersections() {
        wayElements.checkIntersects(snake);
    }

    public WayElements getWayElements() {
        return wayElements;
    }

    public void newGame(GameSurface mSurface) {
        wayElements = new WayElements(mSurface);
        snake = new Snake(mSurface,SNAKE_START_SIZE);
    }

    public Snake getSnake() {
        return snake;
    }

    public void moveSnakeHead(float x) {
        snake.moveHead(x);
    }

    public int getPoints() {
        return wayElements.getTotalRows();
    }

}

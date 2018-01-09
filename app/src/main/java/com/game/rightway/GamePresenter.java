package com.game.rightway;

import android.content.Context;

public class GamePresenter implements UpdateListener {


    private ViewCallbacks viewCallbacks;

    private GameLogic gameLogic;

    private GameThread gameThread;

    private Context context;


    public GamePresenter(Context context, ViewCallbacks viewCallbacks) {
        this.context = context;
        this.viewCallbacks = viewCallbacks;

        gameLogic = new GameLogic(this);
    }

    @Override
    public void update(int interval) {
        gameLogic.updateData(interval);
        viewCallbacks.updateView();
    }

    public void pauseGame() {
        if (gameThread != null)
            gameThread.setPause(true);

        pauseBackgroundMusic();
    }

    private void pauseBackgroundMusic() {
    }

    public void resumeGame() {
        if (gameThread != null)
            gameThread.setPause(false);

        resumeBackgroundMusic();
    }

    private void resumeBackgroundMusic() {
    }

    public void newGame(GameSurface mSurface) {
        gameLogic.newGame(mSurface);
        viewCallbacks.resetView();

        resumeGame();
    }

    public void moveSnakeHead(float x) {
        gameLogic.moveSnakeHead(x);
    }

    public void endGame() {
        gameThread.setPause(true);
        viewCallbacks.showEndGameAllert(gameLogic.getPoints());
    }


    public void startGame(GameSurface mSurface) {
        configureGameThread();
        newGame(mSurface);
    }

    private void configureGameThread() {
        gameThread = new GameThread(this);
        gameThread.setRunning(true);
        gameThread.setPause(true);
        gameThread.start();
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

}

package com.game.rightway;

class GameThread extends Thread {

    private static final long PAUSE_SLEEP_TIME = 10;

    private boolean running;
    private boolean pause;

    private UpdateListener updator;

    @Override
    public void run() {
        long previousFrameTime = System.currentTimeMillis();
        while (running) {

            while (pause) {
                try {
                    Thread.sleep(PAUSE_SLEEP_TIME);
                    previousFrameTime += PAUSE_SLEEP_TIME;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long currentFrameTime = System.currentTimeMillis();

            long elapsedTimeMS = currentFrameTime - previousFrameTime;

            updator.update((int) elapsedTimeMS);

            previousFrameTime = currentFrameTime;
        }
    }

    public GameThread(UpdateListener updator) {
        this.updator = updator;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

}

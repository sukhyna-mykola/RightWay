package com.game.rightway;

class GameThread extends Thread {

    private boolean running;
    private boolean pause;

    private UpdateListener updator;


    @Override
    public void run() {
        long startTime;
        long previousFrameTime = System.currentTimeMillis();
        while (running) {

            while (pause) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long currentFrameTime = System.currentTimeMillis();

            long elapsedTimeMS = currentFrameTime-previousFrameTime;

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

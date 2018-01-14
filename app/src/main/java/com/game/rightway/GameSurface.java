package com.game.rightway;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.rightway.helpers.PreferenceHelper;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameLogic model;

    private float widthScreen, heightScreen;

    private static final float POINTS_TEXT_SIZE_PERCENT = 0.06f;
    private static final float POINTS_MARGIN_PERCENT = 0.02f;

    public static final int BLOCKER_SOUND_ID = 0;
    public static final int BONUS_SOUND_ID = 1;

    private SoundPool soundPool;
    private SparseArray<Integer> soundMap;

    private Paint paint;


    public GameSurface(Context context) {
        super(context);
        configure(context);
    }

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        configure(context);
    }

    private void configure(Context mContext) {

        getHolder().addCallback(this);

        initSoundPool();

        soundMap = new SparseArray(2);
        soundMap.put(BLOCKER_SOUND_ID, soundPool.load(mContext, R.raw.block, 1));
        soundMap.put(BONUS_SOUND_ID, soundPool.load(mContext, R.raw.bonus, 1));

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

    }


    private void initSoundPool() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundPool();
        } else {
            createNewSoundPool();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes.Builder attrsBuilder = new AudioAttributes.Builder();
        attrsBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder poolBuilder = new SoundPool.Builder();
        poolBuilder.setMaxStreams(3);
        poolBuilder.setAudioAttributes(attrsBuilder.build());

        soundPool = poolBuilder.build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }

    public void setModel(GameLogic mModel) {
        model = mModel;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        widthScreen = w;
        heightScreen = h;

        paint.setTextSize(POINTS_TEXT_SIZE_PERCENT * getHeightScreen());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void update() {
        Canvas canvas = null;

        try {
            canvas = this.getHolder().lockCanvas();
            synchronized (getHolder()) {
                draw(canvas);
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                try {
                    getHolder().unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            model.getSnake().draw(canvas);

            model.getWayElements().draw(canvas);

            if (model.getPoints() > 0) {
                String text = String.valueOf(model.getPoints());
                Rect bounds = new Rect();
                paint.getTextBounds(text, 0, text.length(), bounds);
                canvas.drawText(text, widthScreen - bounds.width() - widthScreen * POINTS_MARGIN_PERCENT, widthScreen * POINTS_MARGIN_PERCENT + bounds.height(), paint);
            }
        }
    }

    public float getWidthScreen() {
        return widthScreen;
    }

    public float getHeightScreen() {
        return heightScreen;
    }

    public void playSound(int mSoundId) {
        if (PreferenceHelper.getInstance(getContext()).isPlaySound())
            soundPool.play(soundMap.get(mSoundId), 1, 1, 1, 0, 1f);
    }

    public void releaseResourses() {
        soundPool.release();
    }
}
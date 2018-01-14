package com.game.rightway;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.rightway.helpers.AdHelper;
import com.game.rightway.helpers.PreferenceHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;


public class GameFragment extends Fragment implements ViewCallbacks, View.OnTouchListener {

    private final String TAG = getClass().getSimpleName();

    private GameSurface gameView;
    private View allertView;
    private View resultView;
    private TextView pointsText;
    private TextView maxPointsText;
    private ImageButton volumeButton;
    private Button replayButton;

    private InterstitialAd mInterstitialAd;

    private GamePresenter presenter;

    private float touchX = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GamePresenter(getContext(), this);
        setRetainInstance(true);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            presenter.startGame(gameView);

            //for in/out animations
            resultView.setY(-resultView.getHeight());
            replayButton.setY(allertView.getHeight());

            gameView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);


        gameView = v.findViewById(R.id.game_surface);
        gameView.setModel(presenter.getGameLogic());
        gameView.setOnTouchListener(this);
        gameView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        allertView = v.findViewById(R.id.end_game_allert);
        replayButton = v.findViewById(R.id.replay_btn);
        resultView = v.findViewById(R.id.result_view);
        pointsText = v.findViewById(R.id.points);
        maxPointsText = v.findViewById(R.id.max_points);

        v.findViewById(R.id.replay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.newGame(gameView);
                hideAllert(500);
            }
        });

        v.findViewById(R.id.exit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyView();
                getActivity().finish();
            }
        });

        volumeButton = v.findViewById(R.id.volume_controll_button);
        volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlayNow = PreferenceHelper.getInstance(getContext()).isPlaySound();

                PreferenceHelper.getInstance(getContext()).saveSoundPref(!isPlayNow);

                updateVolumeButtonView();
            }
        });

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-1630263344353342/6852122114");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        updateVolumeButtonView();

        return v;
    }


    @Override
    public void resetView() {

    }

    @Override
    public void updateView() {
        gameView.update();
    }

    @Override
    public void destroyView() {
        gameView.releaseResourses();
    }

    @Override
    public void showEndGameAllert(final int points) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PreferenceHelper.getInstance(getContext()).loadMaxPoints() < points)
                    PreferenceHelper.getInstance(getContext()).saveMaxPoints(points);


                pointsText.setText(String.valueOf(points));
                maxPointsText.setText(String.valueOf(PreferenceHelper.getInstance(getContext()).loadMaxPoints()));

                showAllert(750);

                AdHelper.showIntersitialAd(getContext(), mInterstitialAd);
            }
        });

    }

    private void hideAllert(int duration) {
        volumeButton.setVisibility(View.GONE);

        resultView.animate()
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(0)
                .setDuration(duration/2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resultView.setY(-(allertView.getHeight() / 2 - resultView.getHeight() / 2) - resultView.getHeight());
                        resultView.setAlpha(1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        resultView.setY(-(allertView.getHeight() / 2 - resultView.getHeight() / 2) - resultView.getHeight());
                        resultView.setAlpha(1);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
        replayButton.animate()
                .translationY(allertView.getHeight())
                .setDuration(duration).start();

    }

    private void showAllert(int duration) {
        volumeButton.setVisibility(View.VISIBLE);

        resultView.animate()
                .translationY(0)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
        replayButton.animate()
                .translationY(0)
                .setDuration(duration)
                .start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();

                if (touchX == -1)
                    touchX = x;

                presenter.moveSnakeHead(x - touchX);
                touchX = x;

                return true;

            case MotionEvent.ACTION_UP:
                touchX = -1;
                return true;

        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resumeGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pauseGame();
    }

    private void updateVolumeButtonView() {
        if (PreferenceHelper.getInstance(getContext()).isPlaySound()) {
            volumeButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            volumeButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

}

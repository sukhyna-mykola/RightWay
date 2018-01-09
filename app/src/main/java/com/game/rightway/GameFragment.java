package com.game.rightway;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.rightway.helpers.AdHelper;
import com.game.rightway.helpers.PreferenceHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;


public class GameFragment extends Fragment implements ViewCallbacks, View.OnTouchListener {

    private GameSurface gameView;
    private View allertView;
    private TextView pointsText;
    private TextView maxPointsText;
    private ImageButton volumeButton;

    private InterstitialAd mInterstitialAd;

    private GamePresenter presenter;

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
        pointsText = v.findViewById(R.id.points);
        maxPointsText = v.findViewById(R.id.max_points);

        v.findViewById(R.id.replay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.newGame(gameView);
                allertView.setVisibility(View.GONE);
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

                allertView.setVisibility(View.VISIBLE);
                pointsText.setText(String.valueOf(points));
                maxPointsText.setText(String.valueOf(PreferenceHelper.getInstance(getContext()).loadMaxPoints()));

                AdHelper.showIntersitialAd(getContext(),mInterstitialAd);
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();

                presenter.moveSnakeHead(x);
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

    private void updateVolumeButtonView(){
        if (PreferenceHelper.getInstance(getContext()).isPlaySound()) {
            volumeButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            volumeButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

}

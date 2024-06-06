package com.example.galacticguest;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class game extends settings implements View.OnTouchListener, View.OnClickListener  {
    private ImageSwitcher meteoriteOneSwitch;
    private ImageSwitcher meteoriteTwoSwitch;
    private ImageView meteoriteRight2Left;
    private ImageView meteoriteLeft2Right;
    private ImageView star;
    private ImageView text;
    private ImageView background;
    private ImageView astronaut;
    private TextView rocketCountView;
    private TextView starCountView;
    private ObjectAnimator animMeteoriteRight2Left;
    private ObjectAnimator animMeteoriteLeft2Right;
    private ObjectAnimator animStar;
    private ObjectAnimator astronautAnim;
    private ConstraintLayout endGameScreen;
    private final Random rand = new Random();
    int CountStar = 0;
    int CountRocket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton menuButton;
        ImageButton replayButton;
        final int[] meteoriteFrames = {R.drawable.meteorite, R.drawable.meteorite_1};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        meteoriteRight2Left = findViewById(R.id.meteoriteOne);
        meteoriteLeft2Right = findViewById(R.id.meteoriteTwo);
        star = findViewById(R.id.starGame);
        text = findViewById(R.id.textIntro);
        astronaut = findViewById(R.id.astronaut);
        background = findViewById(R.id.background);
        endGameScreen = findViewById(R.id.endGameScreen);
        replayButton = findViewById(R.id.replayButtonGame);
        menuButton = findViewById(R.id.menuButtonGame);
        rocketCountView = findViewById(R.id.rocketCounter);
        starCountView = findViewById(R.id.starCounter);
        meteoriteOneSwitch = new ImageSwitcher(meteoriteRight2Left, meteoriteFrames);
        meteoriteTwoSwitch = new ImageSwitcher(meteoriteLeft2Right, meteoriteFrames);

        endGameScreen.setVisibility(View.INVISIBLE);

        background.setOnTouchListener(this);
        replayButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
    }

    //Установка анимаций вылетов астероидов и звезды
    public ObjectAnimator animObject(ImageView obj, int duration, boolean direction) {
        ObjectAnimator anim;
        int i = direction ? -1 : 1;
        anim = ObjectAnimator.ofFloat(obj, "translationX", 0f, (obj.getWidth() + background.getWidth()) * i);

        obj.setY(70 + rand.nextInt(background.getHeight() - 70 - astronaut.getHeight() - obj.getHeight()));
        anim.setDuration(duration);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        return anim;
    }

    //Анимация во время игры
    public void animGame() {
        Handler handler = new Handler();
        astronautAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rocketCountView.setText(Integer.toString(++CountRocket));
            }
        });

        animMeteoriteRight2Left.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                animMeteoriteRight2Left.pause();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animMeteoriteRight2Left.resume();
                    }
                }, 2000);
                meteoriteRight2Left.setY(70 + rand.nextInt(background.getHeight() - 70 - astronaut.getHeight() - meteoriteRight2Left.getHeight()));
            }
        });
        animMeteoriteLeft2Right.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                animMeteoriteLeft2Right.pause();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animMeteoriteLeft2Right.resume();
                    }
                }, 2000);
                meteoriteLeft2Right.setY(70 + rand.nextInt(background.getHeight() - 70 - astronaut.getHeight() - meteoriteLeft2Right.getHeight()));
            }
        });
        animStar.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                star.setY(70 + rand.nextInt(background.getHeight() - 70 - astronaut.getHeight() - star.getHeight()));
            }
        });
    }

    //Основная логика игры
    public void gameActivity() {
        animMeteoriteRight2Left = animObject(meteoriteRight2Left, 2000, true);
        meteoriteOneSwitch.startSwitching();

        animMeteoriteLeft2Right = animObject(meteoriteLeft2Right, 2000, false);
        meteoriteTwoSwitch.startSwitching();

        animStar = animObject(star, 8000, true);

        astronautAnim = ObjectAnimator.ofFloat(astronaut, "translationY", 0f, -background.getHeight() + astronaut.getHeight() + 16f, 0f);
        astronautAnim.setDuration(1000);
        astronautAnim.setInterpolator(new LinearInterpolator());
        //Ищем столкновения между астрероидами и астронавтом, и звездой и астронавтом
        astronautAnim.addUpdateListener(animation -> {
            Rect astronautRect = new Rect();
            Rect met1Rect = new Rect();
            Rect met2Rect = new Rect();
            Rect starRect = new Rect();
            astronaut.getHitRect(astronautRect);
            meteoriteRight2Left.getHitRect(met1Rect);
            meteoriteLeft2Right.getHitRect(met2Rect);
            star.getHitRect(starRect);

            if (Rect.intersects(astronautRect, met2Rect) || Rect.intersects(astronautRect, met1Rect) ) {
               meteoriteOneSwitch.stopSwitching();
               meteoriteTwoSwitch.stopSwitching();
               astronautAnim.cancel();
               animMeteoriteRight2Left.cancel();
               animMeteoriteLeft2Right.cancel();
               animStar.cancel();
               endGameScreen.setVisibility(View.VISIBLE);
            }
            if (Rect.intersects(astronautRect, starRect)) {
                animStar.cancel();
                star.setY(70 + rand.nextInt(background.getHeight() - 70 - astronaut.getHeight() - star.getHeight()));
                star.setTranslationX(0f);
                animStar.start();
                starCountView.setText(Integer.toString(++CountStar));
            }
        });

        animGame();
        animMeteoriteRight2Left.start();
        animMeteoriteLeft2Right.start();
        animStar.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.replayButtonGame) {
            endGameScreen.setVisibility(View.INVISIBLE);
            astronaut.setTranslationY(0f);
            CountStar = 0;
            CountRocket = 0;
            rocketCountView.setText("0");
            starCountView.setText("0");
            gameActivity();
        } else if (v.getId() == R.id.menuButtonGame) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (text.getVisibility() == View.VISIBLE) {
                text.setVisibility(View.INVISIBLE);
                gameActivity();
            } else {
                if (astronautAnim.isRunning()) {
                    return false;
                }
                SoundAndVibration();
                astronautAnim.start();
            }
        }
        return false;
    }
}
package com.example.galacticguest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends settings implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Слушатели на кнопки в мейне
        findViewById(R.id.buttonStart).setOnClickListener(this);
        findViewById(R.id.buttonExit).setOnClickListener(this);
        findViewById(R.id.buttonPolicy).setOnClickListener(this);
        findViewById(R.id.buttonSettings).setOnClickListener(this);

        //Анимация ракеты
        Animation animIn = AnimationUtils.loadAnimation(this, R.anim.animation_rocket);
        Animation animOut = AnimationUtils.loadAnimation(this, R.anim.animation_rocket_back);
        ImageView flame = findViewById(R.id.flame_rocket);

        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                return;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.rocket).startAnimation(animOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                return;
            }
        });

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                return;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.rocket).startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                return;
            }
        });
        findViewById(R.id.rocket).startAnimation(animIn);

        //Анимация пламени
        ImageSwitcher animFlame;
        int[] flameFrames = {R.drawable.rocket_flame, R.drawable.rocket_flame_1};
        animFlame = new ImageSwitcher(flame, flameFrames);
        animFlame.startSwitching();
    }

    //Обработка нажатий
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.buttonStart) {
            Intent intent = new Intent(this, game.class);
            startActivity(intent);
        } else if (view.getId() == R.id.buttonExit) {
            finishAffinity();
        } else if (view.getId() == R.id.buttonPolicy) {
            Intent intent = new Intent(this, policy.class);
            startActivity(intent);
        } else if (view.getId() == R.id.buttonSettings) {
            Intent intent = new Intent(this, settings.class);
            startActivity(intent);
        }
    }
}
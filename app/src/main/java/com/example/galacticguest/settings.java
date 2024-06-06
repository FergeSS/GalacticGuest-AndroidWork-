package com.example.galacticguest;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity implements View.OnClickListener {
    Switch soundSwitch;
    Switch vibroSwitch;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Выключение системного звука
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        soundSwitch = findViewById(R.id.switchSound);
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);
        soundSwitch.setChecked(isSoundEnabled);

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SoundAndVibration();
                editor.putBoolean("sound_enabled", isChecked);
                editor.apply();
            }
        });

        vibroSwitch = findViewById(R.id.switchVibro);
        boolean isVibroEnabled = sharedPreferences.getBoolean("vibro_enabled", true);
        vibroSwitch.setChecked(isVibroEnabled);

        vibroSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SoundAndVibration();
                editor.putBoolean("vibro_enabled", isChecked);
                editor.apply();
            }
        });
    }

    public void SoundAndVibration() {
        if (sharedPreferences.getBoolean("vibro_enabled", false)) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        }

        if (sharedPreferences.getBoolean("sound_enabled", false)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(settings.this, R.raw.click);
            mediaPlayer.setVolume(0.2f, 0.2f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        SoundAndVibration();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundAndVibration();
    }
}
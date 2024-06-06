package com.example.galacticguest;

import android.os.Handler;
import android.widget.ImageView;

public class ImageSwitcher {
    private final ImageView imageView;
    private final int[] images;
    private int currentIndex = 0;
    private final Handler handler = new Handler();
    private Runnable runnable;

    public ImageSwitcher(ImageView imageView, int[] images) {
        this.images = images.clone();
        this.imageView = imageView;
    }

    public void startSwitching() {
        runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(images[currentIndex]);
                currentIndex = (currentIndex + 1) % images.length;
                handler.postDelayed(this, 50);
            }
        };
         handler.post(runnable);
    }
    public void stopSwitching() {
        handler.removeCallbacks(runnable);
    }
}

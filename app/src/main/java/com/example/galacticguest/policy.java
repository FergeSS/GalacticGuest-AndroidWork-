package com.example.galacticguest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class policy extends settings implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        findViewById(R.id.buttonBack).setOnClickListener(this);
        WebView policy = findViewById(R.id.policyText);
        policy.loadUrl("https://telegra.ph/Galactic-Guest-06-03");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
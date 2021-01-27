package com.example.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Startpage extends Activity {
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        imageView = findViewById(R.id.imageView);

        Intent intent = new Intent(Startpage.this, com.example.videoplayer.MainActivity.class);
        finish();
        startActivity(intent);

    }
}

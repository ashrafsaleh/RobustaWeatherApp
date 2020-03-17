package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {
    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        view = (ImageView)findViewById(R.id.viewimage);
        Intent i =getIntent();
        String getImagePath=i.getExtras().getString("sendimages");
        Bitmap bitmap= BitmapFactory.decodeFile(getImagePath);
        view.setImageBitmap(bitmap);
    }
}

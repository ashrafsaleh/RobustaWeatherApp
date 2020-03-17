package com.example.weatherapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Activity activity;
    ArrayList<String>filePath=new ArrayList<>();
    ArrayList<String>fileName=new ArrayList<>();

    public Adapter(Activity activity, ArrayList<String> filePath, ArrayList<String> fileName) {
        this.activity = activity;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Override
    public int getCount() {
        return fileName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
      View v=  inflater.inflate(R.layout.raw_item,null);

        ImageView imageView=v.findViewById(R.id.listimageitem);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath.get(position));
        imageView.setImageBitmap(bitmap);
        return v;
    }
}

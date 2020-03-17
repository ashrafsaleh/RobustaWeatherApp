package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ImagesList extends AppCompatActivity {

    ListView photosList;
    Adapter adapter;
    ArrayList<String> arrayList;
    ArrayList<String> pathList;

    private static final int PERMISSION_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);

        photosList=findViewById(R.id.photosls);
        arrayList = new ArrayList<>();
        pathList = new ArrayList<>();
        adapter = new Adapter(ImagesList.this, pathList,arrayList );
        if(checkPermission())
        {
            File dir = new File(Environment.getExternalStorageDirectory() + "/MyImages");
            if (dir.exists()) {
                Log.d("path", dir.toString());
                File list[] = dir.listFiles();
                for (int i = 0; i < list.length; i++) {
                    arrayList.add(list[i].getName());
                    pathList.add(list[i].getPath());
                    photosList.setAdapter(adapter);
                }

            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RobustaTaskImages");
            if (dir.exists()) {
                Log.d("path", dir.toString());
                File list[] = dir.listFiles();
                for (int i = 0; i < list.length; i++) {
                    arrayList.add(list[i].getName());
                    pathList.add(list[i].getPath());
                    photosList.setAdapter(adapter);
                }
            }
        }
        photosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ViewActivity.class);
                intent.putExtra("sendimages",pathList.get(position));
                startActivity(intent);
            }
        });


    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ImagesList.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(ImagesList.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ImagesList.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ImagesList.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}

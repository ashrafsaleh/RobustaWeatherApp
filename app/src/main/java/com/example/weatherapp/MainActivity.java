package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {
TextView data;
Button cap,sahre,viewImages,clear;
ImageView imageView;
    String dateformat,descob,city;
    BitmapDrawable drawable;
    Bitmap bitmap,bitmap2;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (TextView)findViewById(R.id.data);
        imageView = (ImageView)findViewById(R.id.image1);
        sahre = (Button)findViewById(R.id.share);
        cap = (Button)findViewById(R.id.capture);
        clear = (Button)findViewById(R.id.clear);
        viewImages=findViewById(R.id.openList);
        weather();
        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImagesList.class));
            }
        });
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,1);
            }
        });
        sahre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap2 == null){
                    Toast.makeText(MainActivity.this, "please take a photo first", Toast.LENGTH_SHORT).show();
                }
                else{
                saveImage();}
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode ==1){
                 bitmap2 =(Bitmap)data.getExtras().get("data") ;
                imageView.setImageBitmap(bitmap2);
            }
        }
    }

    public void weather(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=cairo,egypt&appid=b4c9d2aa9b4773e4a135a4ac765f1791&units=imperial";
        JsonObjectRequest jso = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String tempob = String.valueOf(main_object.getDouble("temp"));
                    descob = object.getString("description");
                    city = response.getString("name");
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("EEEE/MM/dd");
                     dateformat = format.format(calendar.getTime());
                    double int_temp = Double.parseDouble(tempob);
                    double centi = (int_temp-32)/1.8000;
                    centi = Math.round(centi);
                    i = (int) centi;


                    data.setText("The weather is : "+ i +"\n"+city+"\n"+descob);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jso);
    }
    public void saveImage()
    {
        drawable=(BitmapDrawable)imageView.getDrawable();
        bitmap=drawable.getBitmap();

        FileOutputStream outputStream =null;

        File sdCArd=Environment.getExternalStorageDirectory();

        File directory=new File(sdCArd.getAbsolutePath()+"/MyImages");
        directory.mkdir();

        String fileName=String.format("%d.jpg",System.currentTimeMillis());
        File outFile=new File(directory,fileName);
        Log.e("CHECKPATH",outFile.toString());

        try {
            outputStream =new FileOutputStream(outFile);
            Bitmap textBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(textBitmap);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE); // Text Color
            paint.setTextSize(9); // Text Size
            paint.setAntiAlias(true);
            paint.setARGB(255, 255, 255, 255);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

            canvas.drawBitmap(textBitmap, 0, 0, paint);
            String getData=data.getText().toString();
            canvas.drawText(getData, 25, 25, paint);

            textBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            imageView.setImageBitmap(textBitmap);
            Uri photo_uri= FileProvider.getUriForFile(this,getApplicationContext().getPackageName()+".provider",outFile);



            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            this.sendBroadcast(intent);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, photo_uri);
            startActivity(Intent.createChooser(share, "Share via"));

        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.mrravipande.lailas;

import static android.net.Uri.fromFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {
    private ImageView male, female;
    private TextView score;
    private Intent intent;
    private Button btnShare, btnDownload;
    LinearLayout layoutLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        male = findViewById(R.id.boy);
        female = findViewById(R.id.girl);
        score = findViewById(R.id.loveTV);
        btnShare = findViewById(R.id.btnShare);
        btnDownload = findViewById(R.id.btnDownload);
        layoutLL = findViewById(R.id.layoutLL);

        intent = getIntent();

        Uri uriMale = intent.getParcelableExtra("imgMale");
        male.setImageURI(uriMale);

        Uri uriFemale = intent.getParcelableExtra("imgFemale");
        female.setImageURI(uriFemale);

        String loveScore = getIntent().getStringExtra("value");
        score.setText(loveScore);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData();
            }
        });
    }

    //to save an image on local Download file
    private void saveImage() {

        layoutLL.setDrawingCacheEnabled(true);
        layoutLL.buildDrawingCache();
        layoutLL.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Bitmap bitmap = layoutLL.getDrawingCache();
        save(bitmap);

    }

    private void save(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(root + "/Download");
        String filename = "my_love_photos.png";
        File myfile = new File(file, filename);

        if (myfile.exists()) {
            myfile.delete();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
            layoutLL.setDrawingCacheEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error : "+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetWorldReadable")
    private void shareData() {

        Bitmap bitmap = getBitmapFromView(layoutLL);

        try {
            File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"appbackground.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(android.R.color.white);
        }

        view.draw(canvas);

        return returnedBitmap;
    }
}
package com.mrravipande.lailas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher <Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();



                        }
                    }
            );

    private ImageView male, female;
    private Bitmap bitmap1, bitmap2;
    private TextView loveTV;
    private Button loveStatusBtn, move;
    private String result;
    private Uri uri1, uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        male = findViewById(R.id.boy);
        female = findViewById(R.id.girl);
        loveTV = findViewById(R.id.loveTV);
        loveStatusBtn = findViewById(R.id.btnStatus);
        move = findViewById(R.id.btnMove);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryMale();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryFemale();
            }
        });

        loveStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();
                result = (random.nextInt(100)+1)+" %";

                if (bitmap1 == null || bitmap2 == null) {
                    Toast.makeText(MainActivity.this, "Please Select Image.", Toast.LENGTH_SHORT).show();
                } else {
                    loveTV.setText(result);
                }
            }
        });

        //move data to ShareActivity
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                intent.putExtra("value", result);
                intent.putExtra("imgMale", uri1);
                intent.putExtra("imgFemale", uri2);

                startActivity(intent);
            }
        });
    }

    //select image from gallery.
    private void openGalleryMale() {
        Intent intent1 = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent1, 1);
    }

    private void openGalleryFemale() {
        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent2, 2);
    }

    //Show images in circle view.
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch(requestCode){
                case 1:
                    uri1 = data.getData();
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                        male.setImageBitmap(bitmap1);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    uri2 = data.getData();
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
                        female.setImageBitmap(bitmap2);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }

        if (resultCode == 1 && data != null) {

            uri1 = data.getData();
            uri2 = data.getData();

        }

    }

}
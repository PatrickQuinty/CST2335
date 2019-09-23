package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "ProfileActivity";


            //(ACTIVITY_NAME, “In function:” + "onCreate");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e(ACTIVITY_NAME, "In function:" + "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lab3_second_relative);


        EditText mail2nd = findViewById(R.id.mailFTwo);


        Intent dataFromLastPage = getIntent();
        String emailInput = dataFromLastPage.getStringExtra("emailInput");

        mail2nd.setText(emailInput);

        ImageButton picPrompt = findViewById(R.id.imgBtn);

        if(picPrompt != null)
        {
            picPrompt.setOnClickListener(
                    v -> {Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }}
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function:" + "onActivityResult");
        ImageButton mImageButton = findViewById(R.id.imgBtn);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

}

package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_main_relative);

        EditText typedMail = findViewById(R.id.mailFill);


        SharedPreferences shared = getSharedPreferences("sharedSave", Context.MODE_PRIVATE);
        String savedMail = shared.getString("email", "");

        typedMail.setText(savedMail);


        //Find login button and add a listener
        Button logBtn = findViewById(R.id.loginBtn);
            //Make sure button is found
        if(logBtn != null)
        {
            logBtn.setOnClickListener(v ->{
                Intent pageTwo = new Intent(MainActivity.this, ProfileActivity.class);
                pageTwo.putExtra("emailInput", typedMail.getText().toString());
                startActivity(pageTwo);
            });
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        EditText typedMail = findViewById(R.id.mailFill);

        SharedPreferences shared = getSharedPreferences("sharedSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEdit = shared.edit();
        sharedEdit.putString("email", typedMail.getText().toString());
        sharedEdit.commit();
    }
}

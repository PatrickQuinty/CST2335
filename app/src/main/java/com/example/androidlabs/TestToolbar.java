package com.example.androidlabs;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String toastText = "This is the original message.";
    Snackbar sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        sb = Snackbar.make(tool, "Return to profile page?", Snackbar.LENGTH_LONG)
        .setAction("Go Back", e -> finish());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_first_test, menu);


    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.overflowItem:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();

                break;
            case R.id.toastItem:
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();

                break;
            case R.id.typeItem:
                customMessage();
                break;
            case R.id.goBack:

                sb.show();
                break;
        }
        return true;
    }

    private void customMessage() {
        View middle = getLayoutInflater().inflate(R.layout.toolbar_popup_msg, null);
        //Button btn = (Button)middle.findViewById(R.id.view_button);
        //btn.setOnClickListener( clk -> et.setText("You clicked my button!"));

        EditText et = middle.findViewById(R.id.msgPrompt);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Accept", (dialog, id) -> { //using a lambda
            toastText = et.getText().toString();
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //without lambda
            public void onClick(DialogInterface dialog, int id) {

            }
        }).setView(middle);

        builder.create().show();
    }
}

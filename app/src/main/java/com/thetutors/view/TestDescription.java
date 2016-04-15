package com.thetutors.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;

public class TestDescription extends ActionBarActivity {

    Button startTest;
    VariabelConfig publicVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_description);

        //initialization singleton object to reset tutorial page (when back button pressed)
        publicVar = new VariabelConfig();

        startTest = (Button) findViewById(R.id.startTest);
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the test when the button pressed
                Intent i = new Intent(TestDescription.this, TestScreen.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Return to Tutorial")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes, i'm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            publicVar.setCount(0);
                            Intent i = new Intent(TestDescription.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

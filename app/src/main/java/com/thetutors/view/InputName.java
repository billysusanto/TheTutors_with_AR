package com.thetutors.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;


public class InputName extends ActionBarActivity {

    Button startTutorial;
    EditText userName;
    VariabelConfig publicVar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);
        context = this;

        //initialization singleton object to put username into global variabel
        publicVar = new VariabelConfig();

        userName = (EditText) findViewById(R.id.userNameInput);

        startTutorial = (Button) findViewById(R.id.startTutorial);
        startTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user name, and put it into global variabel in singelton object
                if(!userName.getText().toString().equalsIgnoreCase("")) {
                    publicVar.setUsername(userName.getText().toString());

                    //start welcome message page with username
                    Intent i = new Intent(InputName.this, WelcomeMessage.class);
                    startActivity(i);
                    finish();
                }
                else{
                    //show notification whenever the user put empty name
                    Toast.makeText(context, "Input your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method for trigger command when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit Tutorial")
                    .setMessage("Are you sure want to quit this tutorial?")
                    .setPositiveButton("Yes, i'm done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InputName.this.finish();
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

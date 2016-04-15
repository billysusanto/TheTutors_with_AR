package com.thetutors.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;


public class WelcomeMessage extends Activity {
    private static int SPLASH_TIME_OUT = 1000; //change the value for customize the splash screen duration

    TextView welcomeMsg;
    VariabelConfig vg;
    Intent i = new Intent();
    String username;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_message);

        //initialization singleton object to load username to welcome message screen
        vg = VariabelConfig.getInstance();

        sharedPref = getSharedPreferences("Auth", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");

        //initialization text view (username & message)
        welcomeMsg = (TextView) findViewById(R.id.welcomeText);
        welcomeMsg.setText(username + " Get ready for Tutorials!!!");
        Log.e("USERNAME at Welcome", vg.getUserName() + " TEst");
        Log.e("USERNAME at Welcome", vg.getUserName() + " TEst 2");


        //Thread to show splash screen with time_out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(WelcomeMessage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

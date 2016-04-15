package com.thetutors.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.thetutors.R;
import com.thetutors.controller.DatabaseHelper;


public class TutorialDescription extends ActionBarActivity {

    TextView marque;
    Button inputName;
    ScrollView sv;
    int x=0;
    boolean finish = false;
    DatabaseHelper dh;

    final int DELAY = 5000; //millisecond (5 second delay)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TableLayout tl = new TableLayout(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_description);

        dh = new DatabaseHelper(this);

        //initialization scrollview
        sv = (ScrollView) findViewById(R.id.svTutorialDescription);

        //initialization marque text view
        marque = (TextView) findViewById(R.id.marque);
        //marque.setText(getResources().getString(R.string.marque));
        marque.setText(dh.getMarqueText());
        //marque.setText("bmn");

        inputName = (Button) findViewById(R.id.inputName);
        inputName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //force to start animation when the button pressed
                finish = true;
                Intent i = new Intent(TutorialDescription.this, Animation.class);
                startActivity(i);
                TutorialDescription.this.finish();
            }
        });

        //Timer to start the next page with delay
        new CountDownTimer(DELAY, 250) {
            public void onTick(long millisUntilFinished) {
                    //auto scroll per 1 pixel
                    sv.smoothScrollBy(0, x);
                    x += 1;
            }

            public void onFinish() {
                if(finish != true) {
                    //Start animation after the countdown finish
                    Intent i = new Intent(TutorialDescription.this, Animation.class);
                    startActivity(i);
                    TutorialDescription.this.finish();
                }
            }

        }.start();
    }

    //Method for trigger command when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit Tutorial")
                    .setMessage("Are you sure want to quit this tutorial?")
                    .setPositiveButton("Yes, i'm done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TutorialDescription.this.finish();
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

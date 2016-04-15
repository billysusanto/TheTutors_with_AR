package com.thetutors.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;


public class Result extends ActionBarActivity {

    Button mainScreen, testAgain;
    TextView resultView, resultView2;
    VariabelConfig publicVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //initialization singleton object to put test result into global variable
        publicVar = new VariabelConfig();

        //initialization text view (result and message)
        resultView = (TextView) findViewById(R.id.result);
        resultView2 = (TextView) findViewById(R.id.result2);

        mainScreen = (Button) findViewById(R.id.tomain);
        mainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the home page (tutorial)
                publicVar.setCount(0);
                Intent i = new Intent(Result.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button testAgain = (Button) findViewById(R.id.testAgain);
        testAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the test page
                Intent i = new Intent(Result.this, TestScreen.class);
                startActivity(i);
                finish();
            }
        });

        //write the test result, check if the score greater than 75 or lower.
        resultView.setText(publicVar.getUserName() + ", you scored " + publicVar.getResult() + "%");
        if(publicVar.getResult() > 75){
            //message when the user get score more than 75
            testAgain.setVisibility(View.INVISIBLE);
            resultView2.setText("CONGRATULATION!!!!");
        }
        else{
            //message when the user get score less than 75
            resultView2.setText("You can improve more");
        }

        publicVar.resetResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Method for trigger command when menu button pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mainMenu:
                publicVar.setCount(0);
                Intent i = new Intent(Result.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.aboutMenu:
                Intent j = new Intent(Result.this, About.class);
                startActivity(j);
                finish();
                break;
            case R.id.creditMenu:
                Intent k = new Intent(Result.this, Credits.class);
                startActivity(k);
                finish();
                break;
        }
        return true;
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

                            //Stop the activity
                            Result.this.finish();
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

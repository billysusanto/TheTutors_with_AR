package com.thetutors.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;


public class Credits extends ActionBarActivity {

    VariabelConfig publicVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        publicVar = new VariabelConfig();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mainMenu:
                publicVar.setCount(0);
                Intent i = new Intent(Credits.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.aboutMenu:
                publicVar.setCount(0);
                Intent j = new Intent(Credits.this, About.class);
                startActivity(j);
                finish();
                break;
            case R.id.creditMenu:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button - BACK TO MAIN SCREEN
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Credits.this, MainActivity.class);
            startActivity(i);
            finish();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

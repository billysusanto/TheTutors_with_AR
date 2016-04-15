package com.thetutors.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.thetutors.R;
import com.thetutors.controller.DatabaseHelper;
import com.thetutors.model.Marque;
import com.thetutors.model.QuestionTest;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 1000; //change the value for customize the splash screen duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        DatabaseHelper dh = new DatabaseHelper(this);

        try {
            //if (dh.getMarqueText() == null) {
                QuestionTest q = new QuestionTest();
                q.setQuestion("(3 - 1) x (9 - 7)");
                q.setAnswer("4");
                q.setMultipleChoice("4#5#6");

                //dh.createQuestion(q);

                Marque mq = new Marque();
                mq.setMarque("Test\\n\n" +
                        "\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\\\n\\n\" +\n" +
                        "                        \"        Tutorial Description - Marque\\n");

                dh.createMarque(mq);
                Log.e("marque", dh.getMarqueText());
           // }
        }
        catch(Exception ex){
            Log.d("Error Create DB", ex+"");
        }

        //Show the splass screen with time_out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, TutorialDescription.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

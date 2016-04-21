package com.thetutors.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.thetutors.R;
import com.thetutors.controller.VariabelConfig;
import com.thetutors.model.QuestionTest;
import com.thetutors.webservice.JavaServlet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.view.ViewGroup.MarginLayoutParams;

public class TestScreen extends AppCompatActivity {

    LinearLayout mainLayout;
    int max = 10;

    //List<String> list_question = new LinkedList<String>();
    //List<String> list_answer = new LinkedList<String>();
    //List<String> list_multi_choice = new LinkedList<String>();
    List<Integer> sequence = new LinkedList<Integer>();

    boolean checker [];

    Random random = new Random();
    //boolean getQuestion = false;
    RadioGroup rg [];
    VariabelConfig publicVar;
    Context context;

    QuestionTest questionTest [];
    JavaServlet servlet = new JavaServlet();
    String msg;
    String [] split_multi_choice;

    int temp [] = new int [max];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_screen);

        context = this;

        //instance the variabel
        publicVar = new VariabelConfig();
        LinearLayout ll = new LinearLayout(this);
        LinearLayout lla [] = new LinearLayout[max];
        LinearLayout llq [] = new LinearLayout[max];
        TextView number []= new TextView [max];
        TextView question []= new TextView [max];
        rg = new RadioGroup[max];
        RadioButton a [] = new RadioButton[max];
        RadioButton b [] = new RadioButton[max];
        RadioButton c [] = new RadioButton[max];
        Button finish = new Button(this);
        checker = new boolean [Arrays.asList(getResources().getStringArray(R.array.question)).size()];

        try {
            msg = servlet.execute(3, 1).get();
            questionTest = xmlParser(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //get content from string.xml (question, answer, multiplechoice)

//        list_question = Arrays.asList(getResources().getStringArray(R.array.question));
//        list_answer = Arrays.asList(getResources().getStringArray(R.array.answer));
//        list_multi_choice = Arrays.asList(getResources().getStringArray(R.array.multi_choice));

        mainLayout = (LinearLayout) findViewById(R.id.questionLL);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        ll.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(ll);

        for(int i=0; i<max; i++){
            checker[i] = false;
        }

        for(int i=0; i<max; i++) {
            //instance the layout
            llq[i] = new LinearLayout(this);
            llq[i].setOrientation(LinearLayout.HORIZONTAL);
            lla[i] = new LinearLayout(this);
            lla[i].setOrientation(LinearLayout.HORIZONTAL);

            //instance the text layout
            number [i]= new TextView(this);
            question [i] = new TextView(this);

            //instance the radiobutton layout
            rg[i] = new RadioGroup(this);
            a[i] = new RadioButton(this);
            b[i] = new RadioButton(this);
            c[i] = new RadioButton(this);

            //add question group and answer group to the main layout
            ll.addView(llq[i]);
            ll.addView(lla[i]);

            MarginLayoutParams params = (MarginLayoutParams) lla[i].getLayoutParams();
            params.bottomMargin = 50; params.topMargin = 5;

            //Check is the question is already show on the screen yet.
            while(true) {
                //temp[i] = random.nextInt(Arrays.asList(getResources().getStringArray(R.array.question)).size()-1);
                temp[i] = random.nextInt(questionTest.length-1);
                if(checker[temp[i]] == false){
                    checker[temp[i]] = true;
                    break;
                }
            }

            //add number and unchoosen question to the layout
            llq[i].addView(number[i]);
            llq[i].addView(question[i]);

            //add radio group to the layout (multiple answer)
            rg[i].addView(a[i]);
            rg[i].addView(b[i]);
            rg[i].addView(c[i]);
            rg[i].setOrientation(LinearLayout.HORIZONTAL);
            lla[i].addView(rg[i]);

            //set the value of question and answer
            number[i].setText(questionTest[i].getId());
            //String split_multi_choice [] = list_multi_choice.get(temp[i]).split("#");
            question[i].setText(questionTest[i].getQuestion());
            split_multi_choice = questionTest[i].getMultipleChoice();

            for(int j=0; j<split_multi_choice.length; j++) {
                a[i].setText("a. " + split_multi_choice[0]);
                b[i].setText("b. " + split_multi_choice[1]);
                c[i].setText("c. " + split_multi_choice[2]);
            }
            sequence.add(Integer.parseInt(questionTest[i].getAnswer()));
        }

        //add finish button to layout
        mainLayout.addView(finish);
        finish.setText("Get Result");

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user answer, and send it to the result page
                for(int i=0; i<max; i++){
                    int radioButtonID = rg[i].getCheckedRadioButtonId();
                    View radioButton = rg[i].findViewById(radioButtonID);
                    int idx = rg[i].indexOfChild(radioButton);

                    //algorithm to check the result
                    if(idx != -1) {
                        String user_answer_check[] = split_multi_choice;
                        if (Integer.parseInt(user_answer_check[idx]) == sequence.get(i)) {
                            publicVar.incResult();
                        }
                    }
                }
                //start the result page
                Intent i = new Intent(TestScreen.this, Result.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Method for trigger command when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit the Test")
                    .setMessage("Are you sure want to quit this test?")
                    .setPositiveButton("Yes, i'm quit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            publicVar.setCount(0);
                            Intent i = new Intent(TestScreen.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            String value="";

            Log.d("ActivityThread", value);

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public QuestionTest[] xmlParser(String xml){
        QuestionTest questionTest [] = new QuestionTest[0];

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("no");

            questionTest = new QuestionTest[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element e = (Element) nNode;
                questionTest[i] = new QuestionTest();

                questionTest[i].setId(Integer.parseInt(nNode.getAttributes().getNamedItem("id").getNodeValue()));

                NodeList question = e.getElementsByTagName("question");
                NodeList multipleChoice = e.getElementsByTagName("multiple-choice");
                NodeList answer = e.getElementsByTagName("answer");

                for(int j=0; j<question.getLength(); j++){
                    questionTest[i].setQuestion(question.item(j).getTextContent());
                }

                String multipleChoiceTemp [] = new String [multipleChoice.getLength()];
                for(int j=0; j<multipleChoice.getLength(); j++){
                    multipleChoiceTemp[i] = multipleChoice.item(j).getTextContent();
                }
                questionTest[i].setMultipleChoice(multipleChoiceTemp);

                for(int j=0; j<answer.getLength(); j++){
                    questionTest[i].setAnswer(answer.item(j).getTextContent());
                }
            }
        }
        catch(ParserConfigurationException e){
            Log.e("ParseConfigExc", e.toString());
        }
        catch(IOException e){
            Log.e("IOException", e.toString());
        }
        catch(SAXException e){
            Log.e("SAXException", e.toString());
        }

        return questionTest;
    }
}

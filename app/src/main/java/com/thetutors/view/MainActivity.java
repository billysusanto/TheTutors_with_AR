package com.thetutors.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.thetutors.R;
import com.thetutors.ar.AugmentedRealityActivity;
import com.thetutors.controller.VariabelConfig;
import com.thetutors.model.Init;
import com.thetutors.webservice.JavaServlet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity {

    TextView tutorialTitle;
    int pageCount = 0;
    double finalTime=0, timeElapsed=0;
    int forwardTime = 2000, backwardTime = 2000;
    String username="";
    ImageButton prev, next, rew, ff;
    Button startDescription;
    VariabelConfig publicVar;
    Context context = this;
    WebView web_view;
    String videoPath;
    String audioPath;

    MediaPlayer audio_player;
    SeekBar seekBar;
    ImageButton play, pause;
    Handler durationHandler = new Handler();

    //List <String> tutorialContent1 = new ArrayList <>();
    //List <String> tutorialTitleList = new ArrayList<>();

    FrameLayout fl;
    LayoutInflater li;
    VideoView video_view;
    //MediaController mediaController;

    //Execute Servlet
    JavaServlet servlet = new JavaServlet();
    String msg;
    Init init [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        publicVar = VariabelConfig.getInstance();

        xmlParser(getInitXML());
        //Initialization view - start

        fl = (FrameLayout) findViewById(R.id.frameLayout);

        li = this.getLayoutInflater();
        View v = li.inflate(R.layout.video_view, null);
        video_view = (VideoView) v.findViewById(R.id.video_tutorial);
        //mediaController = new MediaController(getApplicationContext());
        //video_view.setMediaController(mediaController);

        //get content string from string.xml
        //tutorialContent1 = Arrays.asList(getResources().getStringArray(R.array.tutorial_content_1));
        //tutorialTitleList = Arrays.asList(getResources().getStringArray(R.array.tutorial_title));

        //web view (to decode HTML format)
        web_view = (WebView) findViewById(R.id.webView);
        web_view.setBackgroundColor(Color.parseColor("#a2a2a2"));

        //content text view
        tutorialTitle = (TextView) findViewById(R.id.tutorialTitle);
        //tutorialTitle.setText(tutorialTitleList.get(publicVar.getCount()).toString());
        tutorialTitle.setText(init[publicVar.getCount()].getTitle());

        //media player & seekbar
        audio_player = MediaPlayer.create(this, publicVar.getResId()); // get harus ganti ke <resource>
        finalTime = audio_player.getDuration();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax((int) finalTime);

        //button
        play = (ImageButton) findViewById(R.id.playButton);
        pause = (ImageButton) findViewById(R.id.pauseButton);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        rew = (ImageButton) findViewById(R.id.rewindButton);
        ff = (ImageButton) findViewById(R.id.forwardButton);

        startDescription = (Button) findViewById(R.id.description);
        //Initialization view - end

        //getInitValue();
        publicVar.setTutorLength(init.length);

        //load the default tutorial page (page 1)
        //web_view.loadData(tutorialContent1.get(publicVar.getCount()).toString(), "text/html", "ISO-8859-1");
        if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("text")) {
            msg = getTextContent(publicVar.getCount());
            web_view.loadData(msg, "text/html", "ISO-8859-1");
        }
        else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("video")){
            switchTextToVideo();
            tutorialTitle.setText(init[publicVar.getCount()].getTitle());

            videoPath = "android.resource://" + getPackageName() + "/" +
                    getResources().getIdentifier("animation_"+init[publicVar.getCount()].getResource(), "raw", getPackageName());

            //videoPath = "http://192.168.0.12/appengine-helloworld/animation_0.mp4";

            video_view.setVideoURI(Uri.parse(videoPath));

            //reset seebar max duration depends on video length
            seekBar.setMax(video_view.getDuration());
        }
        else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("ar")){
            Intent i = new Intent(MainActivity.this, AugmentedRealityActivity.class);
            startActivity(i);
        }

        startDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sequence process when the start a test pressed
                mediaStop();
                publicVar.setCount(0);
                Intent i = new Intent(MainActivity.this, TestDescription.class);
                startActivity(i);
                finish();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlay();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_player.pause();
            }
        });

        rew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewind(v);
            }
        });

        ff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                forward(v);
            }
        });

        prev.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(publicVar.getCount() > 0){
                    mediaStop();
                    publicVar.setCount(publicVar.getCount() - 1);
                    if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("text")) {
                        //Set tutorial title
                        //tutorialTitle.setText(tutorialTitleList.get(publicVar.getCount()).toString());
                        tutorialTitle.setText(init[publicVar.getCount()].getTitle());

                        //switch content between video_view to web_view
                        switchVideoToText();
                        //Load tutorial content in HTML format
                        web_view.loadData(getTextContent(init[publicVar.getCount()].getResource()), "text/html", "utf-8");
                        //reset scroll to top position after load prev page
                        web_view.scrollTo(0, 0);

                        Log.e("publicVar Count", publicVar.getCount() + "");

                        //Load prev audio
                        audio_player.reset();
                        try {
                            //set audio content
                            audioPath = "android.resource://" + getPackageName() + "/" +
                                   getResources().getIdentifier("audio_"+init[publicVar.getCount()].getResource(), "raw", getPackageName());

                            //audioPath = "http://192.168.0.12/appengine-helloworld/audio_0.mp3";

                            audio_player.setDataSource(context, Uri.parse(audioPath));
                            audio_player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Reset Maximum seekBar duration
                        seekBar.setMax(audio_player.getDuration());
                    }
                    else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("video")){
                        //try to remove webview, and replace it with videoview
                        switchTextToVideo();
                        //set tutorial title
                        tutorialTitle.setText(init[publicVar.getCount()].getTitle());

                        videoPath = "android.resource://" + getPackageName() + "/" +
                                getResources().getIdentifier("animation_"+init[publicVar.getCount()].getResource(), "raw", getPackageName());

                        //videoPath = "http://192.168.0.12/appengine-helloworld/animation_0.mp4";

                        video_view.setVideoURI(Uri.parse(videoPath));

                        //reset seebar max duration depends on video length
                        seekBar.setMax(video_view.getDuration());
                    }
                    else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("ar")){
                        Intent i = new Intent(MainActivity.this, AugmentedRealityActivity.class);
                        startActivity(i);
                    }
                }
                else{
                    //message when we get back to first page
                    Toast.makeText(context, "This is the first page", Toast.LENGTH_LONG).show();
                }
            }
        });

        next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(publicVar.getCount() < publicVar.getTutorLength()-1){
                    mediaStop();
                    publicVar.setCount(publicVar.getCount() + 1);
                    if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("text")) {

                        //set content from video_view to web_view
                        switchVideoToText();
                        //Set tutorial title
                        //tutorialTitle.setText(tutorialTitleList.get(publicVar.getCount()).toString());
                        tutorialTitle.setText(init[publicVar.getCount()].getTitle());

                        //Load tutorial content in HTML format
                        web_view.loadData(getTextContent(init[publicVar.getCount()].getResource()), "text/html", "utf-8");
                        //reset scroll to top position after load next page
                        web_view.scrollTo(0, 0);

                        //Load next audio
                        audio_player.reset();
                        try {
                            //set audio content

                            audio_player.setDataSource(context,
                                    Uri.parse("android.resource://" + getPackageName() + "/" +
                                            getResources().getIdentifier("audio_"+init[publicVar.getCount()].getResource(), "raw", getPackageName())));
                            audio_player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Reset Maximum seekBar duration
                        seekBar.setMax(audio_player.getDuration());
                    }
                    else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("video")){
                        //switch content from web_view to video_view
                        switchTextToVideo();
                        //set tutorial content
                        tutorialTitle.setText(init[publicVar.getCount()].getTitle());
                        String videoPath = "android.resource://" + getPackageName() + "/" +
                                getResources().getIdentifier("animation_"+init[publicVar.getCount()].getResource(), "raw", getPackageName());
                        video_view.setVideoURI(Uri.parse(videoPath));

                        //reset seekbar depends on video length
                        seekBar.setMax( video_view.getDuration());
                    }
                    else if(init[publicVar.getCount()].getContentType().equalsIgnoreCase("ar")){
                        Intent i = new Intent(MainActivity.this, AugmentedRealityActivity.class);
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(context, "You have reach the last page", Toast.LENGTH_LONG).show();
                    //show up test button
                    startDescription.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Method for update seekBar when the audio played
    Runnable updateSeekBarTime = new Runnable(){
        public void run(){
            if(isDisplayedText()) {
                timeElapsed = audio_player.getCurrentPosition();
            }
            else{
                timeElapsed = video_view.getCurrentPosition();
            }
            seekBar.setProgress((int) timeElapsed);
            durationHandler.postDelayed(this, 100);
        }
    };

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
                            publicVar.setCount(0);
                            mediaStop();
                            MainActivity.this.finish();
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
                break;
            case R.id.aboutMenu:
                publicVar.setCount(0);
                Intent i = new Intent(MainActivity.this, About.class);
                startActivity(i);
                finish();
                break;
            case R.id.creditMenu:
                publicVar.setCount(0);
                Intent j = new Intent(MainActivity.this, Credits.class);
                startActivity(j);
                finish();
                break;
            case R.id.logoutMenu:
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();

                SharedPreferences sharedPreferences = getSharedPreferences("Auth", Context.MODE_PRIVATE);
                Log.e("USERNAME at Main", sharedPreferences.getString("username", "") + " TEst");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                publicVar.setCount(0);
                Intent k = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(k);
                finish();
                break;
        }
        return true;
    }

    void mediaPlay(){
        //sequence process to play the audio
        if(isDisplayedText()) {
            //audio_player.create(getApplicationContext(),
            //        getResources().getIdentifier("audio_"+init[publicVar.getCount()].getResource(), "raw", getPackageName()));
            audio_player.start();
            timeElapsed = audio_player.getCurrentPosition();
            seekBar.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
        else{
            video_view.start();
            seekBar.setMax(video_view.getDuration());
            timeElapsed = video_view.getCurrentPosition();
            seekBar.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
    }

    void mediaStop(){
        audio_player.stop();
        video_view.stopPlayback();

        seekBar.setProgress(0);
    }

    public void forward(View view) {
        //sequence process to forward the audio
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;

            audio_player.seekTo((int) timeElapsed);
        }
    }

    public void rewind(View view) {
        //sequence process to rewind the audio
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;

            audio_player.seekTo((int) timeElapsed);
        }
    }

    public void switchTextToVideo(){
        fl.removeAllViews();
        fl.addView(video_view);
    }

    public void switchVideoToText(){
        fl.removeAllViews();
        fl.addView(web_view);
    }

    public boolean isDisplayedText(){
        return init[publicVar.getCount()].getContentType().equalsIgnoreCase("text");
    }

    public void xmlParser(String xml){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("information");

            init = new Init [nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    init[i] = new Init(
                            Integer.parseInt(eElement.getAttribute("page")),
                            eElement.getElementsByTagName("content-type").item(0).getTextContent(),
                            eElement.getElementsByTagName("title").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("resource").item(0).getTextContent())
                    );
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
            Log.e("SAXEXception", e.toString());
        }
    }

    public String getInitXML(){
        try {
            servlet = new JavaServlet();
            msg = servlet.execute(1, 0).get();
        }
        catch(InterruptedException e){
            Log.e("InterruptedEx : ", e.toString());
        }
        catch(ExecutionException e){
            Log.e("ExecutionEx : ", e.toString());
        }

        return msg;
    }

    public String getTextContent(int page){
        String content="";

        try {
            servlet = new JavaServlet();
            content = servlet.execute(2, page).get();
        }
        catch(InterruptedException e){
            Log.e("InterruptedEx : ", e.toString());
        }
        catch(ExecutionException e){
            Log.e("ExecutionEx : ", e.toString());
        }

        return content;
    }

}

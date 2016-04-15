package com.thetutors.webservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class JavaServlet extends AsyncTask<Integer, Void, String> {

    URL url;
    URLConnection urlConn;
    HttpURLConnection httpConn;
    //String IP = "10.0.2.2";
    //String IP = "192.168.0.14";
    String IP = "10.2.44.23";
    //String IP = "192.168.43.68";
    //String IP = "192.168.56.1";

    String SERVLET_URL = "http://"+ IP +":8080/appengine-helloworld/";
    String SERVLET_URL_INIT = SERVLET_URL + "?init=1";
    String SERVLET_URL_HTML = SERVLET_URL + "?html=";
    int response;

    @Override
    protected String doInBackground(Integer... params) {
        String msg = "";

        Log.e("PARAMS", params[0] + " --- " + params[1]);

        if(params[0] == 1 && params[1] == 0) {
            try {
                url = new URL(SERVLET_URL_INIT);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                            Log.e("", value + "1");
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        if(params[0] == 2){
            try {
                url = new URL(SERVLET_URL_HTML+params[1]);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        return msg;
    }



    protected void onPreExecute(){

    }
}
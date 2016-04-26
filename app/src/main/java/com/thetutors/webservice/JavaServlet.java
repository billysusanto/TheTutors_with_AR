package com.thetutors.webservice;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class JavaServlet extends AsyncTask<Integer, Void, String> {

    URL url;
    URLConnection urlConn;
    HttpURLConnection httpConn;
    //String IP = "10.0.2.2";
    //String IP = "192.168.0.17";
    String IP = "10.2.44.23";
    //String IP = "192.168.43.68";
    //String IP = "192.168.56.1";

    String SERVLET_URL = "http://"+ IP +":8080/appengine-helloworld/";
    String SERVLET_URL_INIT = SERVLET_URL + "?init=1";
    String SERVLET_URL_HTML = SERVLET_URL + "?html=";
    String SERVLET_URL_TEST = SERVLET_URL + "?test=";
    int response;

    @Override
    protected String doInBackground(Integer... params) {
        String msg = "";

        /*b
            Param [0] used for directory / function
            Param [1] used for spesific content on directory / function
         */

        if(params[0] == 1 && params[1] == 0) {
            try {
                url = new URL(SERVLET_URL_INIT);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                httpConn.setConnectTimeout(3000);

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
                Log.e("MalformedURLEx - JS1", e.toString());
                return "error";
            } catch (IOException e) {
                Log.e("IOException - JS1", e.toString());
                return "error";
            }
        }

        if(params[0] == 2){
            try {
                url = new URL(SERVLET_URL_HTML+params[1]);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                httpConn.setConnectTimeout(3000);

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
                Log.e("MalformedURLEx - JS2: ", e.toString());
                return "error";
            } catch (IOException e) {
                Log.e("IOException - JS2: ", e.toString());
                return "error";
            }
        }

        if(params[0] == 3){
            try {
                url = new URL(SERVLET_URL_TEST+params[1]);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                httpConn.setConnectTimeout(3000);

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
            } catch (SocketTimeoutException e){
                Log.e("SocketTimeOut - JS3", e.toString());
                return "error";
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx  - JS3", e.toString());
                return "error";
            } catch (IOException e) {
                Log.e("IOException  - JS3", "Gagal Konek");
                return "error";
            }
        }

        return msg;
    }



    protected void onPreExecute(){

    }
}
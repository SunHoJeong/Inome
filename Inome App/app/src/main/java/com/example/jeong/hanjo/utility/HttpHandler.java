package com.example.jeong.hanjo.utility;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Jeong on 2016-11-07.
 */
public class HttpHandler extends AsyncTask<String, Void, String> {
    private static final String TAG = HttpHandler.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) { //params[0] = url, params[1] = get,post params[2] = json
        URL url;
        String response = null;
        String urlString = params[0];
        String httpFlag = params[1];
        StringBuffer sb = new StringBuffer();
        Log.d(TAG, urlString);

        if (httpFlag.equals("GET")) {

            try {
                url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-type", "html/text");
                conn.setRequestMethod("GET");

                int HttpResult = conn.getResponseCode();
                Log.d(TAG, "resultCode:"+ HttpResult);
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    response = sb.toString();
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        } else if (httpFlag.equals("POST")) {

            try {
                url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestMethod("POST");

                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(params[2]);
                os.flush();

                int HttpResult = conn.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    response = sb.toString();
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "--받은메세지--:"+s);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sb = sb.deleteCharAt(0);
        return sb.toString();
    }

}

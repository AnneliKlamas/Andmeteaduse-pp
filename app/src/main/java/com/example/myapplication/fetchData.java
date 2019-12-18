package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<String, Void, String> {
    String data = "";
    private WeakReference<Informer> mCallBack;

    public fetchData(Informer callback){
        this.mCallBack = new WeakReference<>(callback);
    }

    @Override
    protected String doInBackground(String... voids) {
        try {
            URL url = new URL("https://toetused.kul.ee/public/applications/fetch?sort=submission_date%7Cdesc&page=1&per_page=20000000&applicant=&organization=&applicationround=&submissionDate=%7B%22start%22:null,%22end%22:null%7D");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);

        final Informer callBack = mCallBack.get();
        if(callBack != null){
            callBack.onTaskDone(json);
        }
    }
}

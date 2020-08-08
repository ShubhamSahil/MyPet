package com.pet.webservice;

import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class NetworkRequest extends AsyncTask<String, Integer, String> {

    private Context context;
    private String datas = "";
    private ResponceQueues responceQueues;

    public NetworkRequest(ResponceQueues responceQueues, Context context) {
        this.context = context;
        this.responceQueues = responceQueues;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... string) {
        StringBuffer buffer = new StringBuffer();
        HttpsURLConnection urlConnection;
        String strRespo = "";
        String str2 = "";
        try {
            datas = string[1];
            URL url = new URL(string[0]);
            urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setSSLSocketFactory(CrtVerify.buildSSLFactory(context));
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
                    return verifier.verify(s, sslSession);
                }
            });
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(datas.getBytes().length);
//            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter writer = new PrintWriter(urlConnection.getOutputStream());
            writer.print(datas);
            writer.close();
            InputStream stream = urlConnection.getInputStream();
            strRespo = Utility.readResponce(stream, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strRespo;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onCancelled() {

    }

    @Override
    protected void onPostExecute(String aVoid) {
        responceQueues.responceQue(aVoid,"","");
//        ResponceCreator.writeToFile(aVoid, context);

    }
}
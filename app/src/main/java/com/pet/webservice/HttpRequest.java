package com.pet.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import preference.PrefrenceUtils;

/**
 * Created by asd on 6/4/2018.
 */

public class HttpRequest extends AsyncTask<Httpcall, String, String> {

    private static final String UTF_8 = "UTF-8";
    String token="";
    String url="";
    String methodtype="";
    JSONObject jsonObject;
    Context ctx;
    public HttpRequest(String token, Context context, JSONObject jsonObject, String url, String methodtype) {
        ctx=context;
        this.token=token;
        this.jsonObject=jsonObject;
        this.url=url;
        this.methodtype=methodtype;
    }
    @Override
    protected String doInBackground(Httpcall... params) {
        Response response = null;
        try{
            OkHttpClient client = new OkHttpClient();
//            client.setConnectTimeout(40, TimeUnit.SECONDS); // connect timeout
//            client.setReadTimeout(40, TimeUnit.SECONDS);
//            client.setWriteTimeout(40, TimeUnit.SECONDS);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            if (methodtype.equalsIgnoreCase("GET")){
                body = null;
            }
            Request request = new Request.Builder()
                    .url(url)
                    .method(methodtype, body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer "+token)
                    .build();
            try {
                 response = client.newCall(request).execute();
                Log.e("response-url",response+"");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("response-url",e+"");

            }
        }
         catch (Exception e) {
            e.printStackTrace();
             Log.e("response-url",e+"");

         }finally {

        }
        String responsestr = "";
        try {
             responsestr = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responsestr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }



    private String getDataString(HashMap<String, String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
                if(methodType == Httpcall.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }
        return result.toString();
    }
}

package com.pet.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.google.gson.JsonObject;
import com.pet.preference.PrefrenceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ApiService extends AsyncTask<Void, Void, String> {
    private String url;
    Context context;
    private String posttype;
    private String xid;

    ProgressDialog progressDialog;
    ResponceQueues responceQueues;
    Httpcall httpCallPost;
    JSONObject jsonObject;

    public ApiService(Context context, ResponceQueues responceQueues, String url, String posttype, JSONObject jsonObject){
        this.url = url;
        this.context = context;
        this.responceQueues = responceQueues;
        this.jsonObject = jsonObject;
        this.posttype = posttype;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }catch (Exception e){

        }
    }

    @Override
    protected String doInBackground(Void... params) {
//        httpCallPost = new Httpcall();
//        httpCallPost.setMethodtype(posttype);
//
//        httpCallPost.setUrl(url);
////        HashMap<String,String> paramsPost = new HashMap<>();
//        httpCallPost.setParams(hashMap);
        return "";
    }

    @Override
    protected void onPostExecute(String aVoid) {
        new HttpRequest(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_DEVIC_TOKEN,""),context,jsonObject,url,posttype) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
//                    Log.e("REPONSE", response);
                    progressDialog.dismiss();
                }
                catch (Exception e){

                }


                responceQueues.responceQue(response,url,posttype+"");
            }
    }.execute();


    }
}

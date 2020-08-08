package com.pet.mypet.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pet.mypet.R;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.Httpcall;
import com.pet.webservice.ResponceQueues;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity implements ResponceQueues {

    Context context;
    EditText ed_npswd;
    EditText ed_cpswd;

    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reset);

        context = ResetPasswordActivity.this;
        ed_npswd = findViewById(R.id.ed_npswd);
        ed_cpswd = findViewById(R.id.ed_cpswd);

    }

    private void makeHttpCall(String Url, JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, Url, "POST",paramObject);
            apiService.execute();
        } catch (Exception e) {

        }

    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {

        try {
            if (url.contains(Cons.RESET_URL)) {
JSONObject jsonObject=new JSONObject(responce);
if (!jsonObject.has("message")) {

    makeHttpCall(Cons.VERIFIED_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), jsonObject);
}
else {
    Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
}

            } else if (url.contains(Cons.VERIFIED_URL)) {
                Toast.makeText(context,"Reset password successfully",Toast.LENGTH_LONG).show();
                finish();
            }

        }
        catch (Exception e){

        }

        Log.e("RESPONSE", responce);
    }


    public void textreset(View view) {
        if ((ed_npswd.getText().toString().length()<5)){
            Toast.makeText(context,"Password must be of 5 character.",Toast.LENGTH_LONG).show();

//            Toast.makeText(context,"Password accept only alphanumeric and must contain at least 8 characters with 1 capital letters and 1 Special character.",Toast.LENGTH_LONG).show();
            return;
        }


        maketokencall();
    }

    private void maketokencall() {
//        PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_DEVIC_TOKEN,"55kqEYXiVtjxEYz_xNxm2uCN_SQAUDufhJQw2aliHk2tZ4srl4yuGFBulw1APw4z_MHW_uLUMHW9eu_MTzuYWP9iuCRWjHbId8h-aJ4QpG0Zo0D8UfaLtY_yuwN3TRdRnid5vNlFR1e0nwxira-9AtO8gspxQq5Sif2c3TbjVeaktEdvQtbbypGgyVlZKQzvaYT_pUfRHWGmjjiNFRmGFw");
        try {
            hashMap.clear();
//            JSONObject paramObject = new JSONObject();
            hashMap.put("grant_type", "password");
            hashMap.put("client_id", "");
            hashMap.put("client_secret", "");
            hashMap.put("username", "A8700661448");
            hashMap.put("password", "India@9198_Eliet@9040");
//            hashMap.put("username", "admin");
//            hashMap.put("password", "adminpass");
            hashMap.put("scope", "");
            new HttpRequest().execute();
//            apiService.execute();
        } catch (Exception e) {

        }
//        hashMap.clear();
//        hashMap.put("Username", ed_mobilesignin.getText().toString());
//        hashMap.put("pass", ed_passwordsignin.getText().toString());

    }


    class HttpRequest extends AsyncTask<Httpcall, String, String> {

        private static final String UTF_8 = "UTF-8";
        ProgressDialog progressDialog;
        String token = "";
        String url = "";
        String methodtype = "";
        JSONObject jsonObject;
        Context ctx;

        public HttpRequest() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Httpcall... params) {
            Response response = null;
            try{
                String hashmapurl  =getDataString(hashMap,Httpcall.POST);

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, hashmapurl.toString());

                Request request = new Request.Builder()
                        .url(Cons.TOKEN_URL)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization", "Bearer "+token)
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
            Log.e("TAGRESS",s);
            super.onPostExecute(s);
            progressDialog.dismiss();
            try{
                JSONObject jsonObject = new JSONObject(s);
                String access_token  =jsonObject.getString("access_token");
                PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_DEVIC_TOKEN,access_token);
                callApi();
            }
            catch (Exception e){

            }

        }
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
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }


    private void callApi() {

        if (ed_npswd.getText().toString().equalsIgnoreCase(ed_cpswd.getText().toString())) {
            try {
                JSONObject paramObject = new JSONObject();
                paramObject.put("Username", PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""));
                paramObject.put("pass", ed_npswd.getText().toString());
                makeHttpCall(Cons.RESET_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), paramObject);

            } catch (Exception e) {

            }
        }
        else{
            Toast.makeText(context,"Please check confirm password entry",Toast.LENGTH_LONG).show();

        }


    }

    public  boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;


//        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z]).{6,20})";
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})";
//        final String PASSWORD_PATTERN = "^[a-zA-Z0-9]+$";
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])$";
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!_-])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }



}

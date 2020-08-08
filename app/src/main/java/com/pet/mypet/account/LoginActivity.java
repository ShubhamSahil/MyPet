package com.pet.mypet.account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pet.mypet.MainActivity;
import com.pet.mypet.R;
import com.pet.navigation.MainNaviagtionActivity;
import com.pet.payment.CheckoutUIActivity;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.Httpcall;
import com.pet.webservice.IRetrofit;
import com.pet.webservice.ResponceQueues;
import com.pet.webservice.ServiceGenerator;

import org.json.JSONArray;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements ResponceQueues,Otppage {

    Context context;
    EditText ed_mobilesignin;
    EditText ed_passwordsignin;
    HashMap<String, String> hashMap = new HashMap<>();
    private static final int GET_STORAGE_REQUEST_CODE = 1;
    public int value=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        context = LoginActivity.this;
//        startActivity(new Intent(context, MainNaviagtionActivity.class));

//        startActivity(new Intent(context, CheckoutUIActivity.class));

//        sendmsg();
        if (PrefrenceUtils.readString(context,PrefrenceUtils.PREF_DEVIC_TOKEN,"").equalsIgnoreCase("")){
//            startActivity(new Intent(context, MainNaviagtionActivity.class));

        }
        else{
//            startActivity(new Intent(context, CheckoutUIActivity.class));
//            finish();
        }

        ed_mobilesignin = findViewById(R.id.ed_mobilesignin);
        ed_passwordsignin = findViewById(R.id.ed_passwordsignin);


//        startActivity(new Intent(context,LeadGenerateActivity.class));
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {

        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, GET_STORAGE_REQUEST_CODE, LoginActivity.this);
        }

    }

    public void textsigninaccount(View view) {
        if(!isFormValid()){
            Toast.makeText(context,"Please select all fields",Toast.LENGTH_LONG).show();
            return;
        }

        if (ed_passwordsignin.getText().toString().length()<5){

            Toast.makeText(context,"Password must be of 5 character.",Toast.LENGTH_LONG).show();
            return;
        }


        PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_ID,ed_mobilesignin.getText().toString().replaceFirst("^0+(?!$)",""));
//        makeHttpCall();
//
        maketokencall();
//            value = 1;
//        Otp_Activity cdd = new Otp_Activity(LoginActivity.this,this,ed_mobilesignin.getText().toString());
//        cdd.show();

    }


    private void makeHttpCall() {
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("Username", ed_mobilesignin.getText().toString().replaceFirst("^0+(?!$)",""));
            paramObject.put("pass", ed_passwordsignin.getText().toString());
            paramObject.put("type", "Android");
            ApiService apiService = new ApiService(context, this, Cons.LOGIN_URL + ed_mobilesignin.getText().toString().replaceFirst("^0+(?!$)",""), "POST",paramObject);
            apiService.execute();
        } catch (Exception e) {

        }

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


    private boolean isFormValid(){
        if((ed_mobilesignin.getText().toString().equalsIgnoreCase(""))){
            ed_mobilesignin.setError("Enter mobile number");
            return false;
        } if((ed_passwordsignin.getText().toString().equalsIgnoreCase(""))){
            ed_passwordsignin.setError("Enter password here");
            return false;
        }



        return true;
    }

    private void sendmsg(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+918285973129", "8766266811", "Appointment taken by this" +PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("RESPONSETOKEN",responce);

        if (url.contains(Cons.TOKEN_URL)){
//            makeHttpCall();

        }

//         if (url.contains(Cons.LOGIN_URL)){
//             try{
//                 JSONObject jsonObject = new JSONObject(responce);
//                 if (jsonObject.has("message")){
//                     if (jsonObject.getString("message").contains("Please Verify Mobile")){
//                         ApiService apiService = new ApiService(context, this, Cons.VERIFIED_URL+ PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST",jsonObject);
//                         apiService.execute();
//                     }
//
//                 }
//             }
//             catch (Exception e){
//
//             }
//        }
        if (url.contains(Cons.LOGIN_URL)){
            try{
                JSONObject jsonObject = new JSONObject(responce);
                if (jsonObject.has("message")){
                    Log.e("RESPONSE", jsonObject.getString("message"));

                    if (jsonObject.getString("message").contains("Please Verify Mobile")){
                        ApiService apiService = new ApiService(context, this, Cons.VERIFIED_URL+ PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST",jsonObject);
                        apiService.execute();
                    }
                    else  if (jsonObject.getString("message").contains("\"Invalid User Name!please Sig-up first\"")){
                        Toast.makeText(context, "Invalid User Name!please Sign-up first", Toast.LENGTH_LONG).show();

                    }

                    else{
                        Toast.makeText(context, "Please check password", Toast.LENGTH_LONG).show();

                    }

                }
                else if (jsonObject.has("access_token")){
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_ID,ed_mobilesignin.getText().toString().replaceFirst("^0+(?!$)",""));
                    startActivity(new Intent(context, MainNaviagtionActivity.class));
                    finish();
                }

            }
            catch (Exception e){
                Log.e("RESPONSETOKEN",e+"");

            }
        }
//        try {
//            JSONObject jsonObject = new JSONObject(responce);
//            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
//                PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_EMAIL, ed_mobilesignin.getText().toString());
//                PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_ID, jsonObject.getJSONObject("user").getString("id"));
//                PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_DEVIC_TOKEN, jsonObject.getJSONObject("user").getString("jwtToken"));
//                PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_LOGINTYPE, jsonObject.getJSONObject("user").getString("role"));
//
////                    if (jsonObject.getJSONObject("user").getString("role").equalsIgnoreCase("user")){
////                        startActivity(new Intent(context, LeadsActivitylists.class));
////
////                    }
////                    else{
////                        startActivity(new Intent(context, LeadGenerateActivity.class));
////
////                    }
//            }
//        } catch (Exception e) {
//
//        }
    }


    private void requestPermission(String getAccounts, String getmore, String getLocation, int getEmailRequestCode, LoginActivity setting) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(setting, getAccounts)) {
//            Toast.makeText(Mobile.this, "Calling is Active", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(setting, new String[]{getAccounts, getmore, getLocation}, getEmailRequestCode);
        }
    }

    private boolean checkPermission(String getAccounts, Context applicationContext) {
        int result = ContextCompat.checkSelfPermission(applicationContext, getAccounts);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void texttrouble(View view) {

        if (ed_mobilesignin.getText().toString().equalsIgnoreCase("")){
          Toast.makeText(context,"Please enter mobile number first to reset password",Toast.LENGTH_LONG).show();
        }
        else {
//            PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_ID,ed_mobilesignin.getText().toString());
            value=0;
//            Toast.makeText(context,PrefrenceUtils.readString(context,"number1"+PrefrenceUtils.PREF_ID,"")+"next"+ed_mobilesignin.getText().toString(),Toast.LENGTH_LONG).show();

            Otp_Activity cdd = new Otp_Activity(LoginActivity.this, this, ed_mobilesignin.getText().toString().replaceFirst("^0+(?!$)", ""),"") {
                @Override
                public void responceQue(String responce, String url, String extra_text) {
                    try{
                        Log.e("OTP_ERRORres",responce);
        JSONArray jsonElements=  new JSONArray(responce);

        String otp = jsonElements.getJSONObject(0).getString("otp");
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_OTP, otp);
                    }
                    catch (Exception e){
                        Log.e("OTP_ERRORres",e+"");

                    }

                }
            };
            cdd.show();
        }

//        startActivity(new Intent(context,ResetPasswordActivity.class));
    }

    public void texttsignup(View view) {
        startActivity(new Intent(context,SignupActivity.class));
    }

    @Override
    public void otp(String otp) {
        if(value ==0) {
            startActivity(new Intent(context, ResetPasswordActivity.class));
        }
        else if (value==1){

        }
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
//                client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
//                client.setReadTimeout(15, TimeUnit.SECONDS);
//                client.setWriteTimeout(10, TimeUnit.SECONDS);

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, hashmapurl);

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
                makeHttpCall();
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

package com.pet.mypet.account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pet.mypet.MainActivity;
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


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements ResponceQueues,Otppage {

    Context context;
    EditText ed_titlesignup;
    EditText ed_fnamesignup;
    EditText ed_lnamesignup;
    EditText ed_mobsignup;
    EditText ed_emailsignup;
    EditText ed_passsignup;
    EditText ed_passconfirmsignup;
    HashMap<String, String> hashMap = new HashMap<>();
    private static final int GET_STORAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_account);

        context = SignupActivity.this;

        ed_titlesignup = findViewById(R.id.ed_titlesignup);
        ed_fnamesignup = findViewById(R.id.ed_fnamesignup);
        ed_lnamesignup = findViewById(R.id.ed_lnamesignup);
        ed_mobsignup = findViewById(R.id.ed_mobsignup);
        ed_emailsignup = findViewById(R.id.ed_emailsignup);
        ed_passsignup = findViewById(R.id.ed_passsignup);
        ed_passconfirmsignup = findViewById(R.id.ed_passconfirmsignup);


//        startActivity(new Intent(context,LeadGenerateActivity.class));
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {

        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, GET_STORAGE_REQUEST_CODE, SignupActivity.this);
        }

    }

    public void textsigninaccount(View view) {

        if (!isValidEmail(ed_emailsignup.getText().toString())){
            ed_emailsignup.setError("Enter correct email");
            Toast.makeText(context,"Enter correct email",Toast.LENGTH_LONG).show();

            return ;
        }

        if ((ed_passsignup.getText().toString().length()<5)){
            Toast.makeText(context,"Password must be of 5 character.",Toast.LENGTH_LONG).show();

//            Toast.makeText(context,"Password accept only alphanumeric and must contain at least 8 characters with 1 capital letters and 1 Special character.",Toast.LENGTH_LONG).show();
            return;
        }


        if (!isValidPassword()){
            Toast.makeText(context,"Confirm Password must be match",Toast.LENGTH_LONG).show();

            return;

        }

        if(!isFormValid()){
            Toast.makeText(context,"Please select all fields",Toast.LENGTH_LONG).show();
            return;
        }

        Otp_Activity cdd = new Otp_Activity(SignupActivity.this, this, ed_mobsignup.getText().toString().replaceFirst("^0+(?!$)", ""),ed_emailsignup.getText().toString()) {
            @Override
            public void responceQue(String responce, String url, String extra_text) {

                Log.e("responseotp",responce);

    try{
        JSONArray jsonElements=  new JSONArray(responce);

        String otp = jsonElements.getJSONObject(0).getString("otp");
        PrefrenceUtils.writeString(c, PrefrenceUtils.PREF_OTP, otp);
    }
    catch (Exception e){

    }

      }
        };
        cdd.show();
    }


    private void makeHttpCall() {
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("title", ed_titlesignup.getText().toString());
            paramObject.put("firstName", ed_fnamesignup.getText().toString());
            paramObject.put("surname", ed_lnamesignup.getText().toString());
            paramObject.put("mobile", ed_mobsignup.getText().toString().replaceFirst("^0+(?!$)",""));
            paramObject.put("emailId", ed_emailsignup.getText().toString());
            paramObject.put("from", "ANDROID");
            ApiService apiService = new ApiService(context, this, Cons.SIGNUP_URL+ed_passsignup.getText().toString(), "POST",paramObject);
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
        if((ed_titlesignup.getText().toString().equalsIgnoreCase(""))){
            ed_titlesignup.setError("Enter title");
            return false;
        } if((ed_fnamesignup.getText().toString().equalsIgnoreCase(""))){
            ed_fnamesignup.setError("Enter first name");
            return false;
        }if((ed_lnamesignup.getText().toString().equalsIgnoreCase(""))){
            ed_lnamesignup.setError("Enter last name");
            return false;
        }if((ed_mobsignup.getText().toString().equalsIgnoreCase(""))){
            ed_mobsignup.setError("Enter mobile number");
            return false;
        }if((ed_emailsignup.getText().toString().equalsIgnoreCase(""))){
            ed_emailsignup.setError("Enter email detail");
            return false;
        }if((ed_passsignup.getText().toString().equalsIgnoreCase(""))){
            ed_passsignup.setError("Enter password here");
            return false;
        }


        return true;
    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("RESPONSETOKEN",responce);

        if (url.contains(Cons.TOKEN_URL)){
            makeHttpCall();

        }

         if (url.contains(Cons.VERIFIED_URL)){
            startActivity(new Intent(context, LoginActivity.class));
            finish();
            Toast.makeText(context, "User Registered successfully", Toast.LENGTH_LONG).show();
        }
        if (url.contains(Cons.SIGNUP_URL)){
            try{
                JSONObject jsonObject =new JSONObject(responce);
                if (jsonObject.has("client_Number")) {
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_ID, ed_mobsignup.getText().toString().replaceFirst("^0+(?!$)",""));

                    ApiService apiService = new ApiService(context, this, Cons.VERIFIED_URL+ PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST",jsonObject);
                    apiService.execute();
//                    makeHttpCall(Cons.VERIFIED_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), jsonObject);



                }
                else if (jsonObject.has("message")){
                    Toast.makeText(context, "Mobile Number Already Registered", Toast.LENGTH_LONG).show();

                }
            }
            catch (Exception e){

            }

        }

        Log.e("RESPONSE", responce);
    }


    private void requestPermission(String getAccounts, String getmore, String getLocation, int getEmailRequestCode, SignupActivity setting) {
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

    @Override
    public void otp(String otp) {
        maketokencall();

//        startActivity(new Intent(context,LoginActivity.class));
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

private boolean isValidPassword() {
    if ((!ed_passsignup.getText().toString().equalsIgnoreCase(ed_passconfirmsignup.getText().toString()))) {
        ed_passconfirmsignup.setError("Confirm password must be match");
        return false;
    }
    return true;
}

    public  boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);

        return matcher.matches();

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

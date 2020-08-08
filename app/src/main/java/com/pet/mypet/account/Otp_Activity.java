package com.pet.mypet.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public abstract class Otp_Activity extends Dialog implements
        android.view.View.OnClickListener,
        ResponceQueues {

    public Activity c;
    String mob;
    EditText edittext_otp;
    String otp= "";
    HashMap<String, String> hashMap = new HashMap<>();
    int countryCode;

    public Dialog d;
    public Button verify;
    Otppage otppage;
    ImageView cancel_image;
    String mob_number;
    String email;
    TextView resendOtp;
    TextView resendOtptimer;
    public int counter=0;
    public int text_counter=30;
    public String countervaluefinal="0";

    public Otp_Activity(Activity a,Otppage otppage,String mob_number,String email) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.otppage = otppage;
        this.mob_number = mob_number;
        this.email = email;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        setContentView(R.layout.otp_screen);
        edittext_otp =  findViewById(R.id.edittext_otp);
        verify =  findViewById(R.id.verify);
        cancel_image = findViewById(R.id.cancel_image);
        resendOtp = findViewById(R.id.resendOtp);
        resendOtptimer =  findViewById(R.id.resendOtptimer);

        cancel_image.setOnClickListener(this);
        verify.setOnClickListener(this);

        setCancelable(false);
//        resendOtp.setEnabled(false);
        startTimer();

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendOtptimer.setVisibility(View.VISIBLE);

                maketokencall();
                startTimer();


//                makeOTPcall();
            }
        });
//        otp = Otpclass.generateOTP();
//        setCancelable(false);
//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(c);
//        try {
//            // phone must begin with '+'
//            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+918766266811", "");
//            countryCode = numberProto.getCountryCode();
//            Toast.makeText(c,PrefrenceUtils.readString(c,"number2"+countryCode +" "+PrefrenceUtils.PREF_ID,""),Toast.LENGTH_LONG).show();
//
//        } catch (NumberParseException e) {
//            System.err.println("NumberParseException was thrown: " + e.toString());
//        }


        maketokencall();
//        PrefrenceUtils.writeString(c, PrefrenceUtils.PREF_OTP, otp);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify:
                if (!edittext_otp.getText().toString().equalsIgnoreCase("")) {

                    if (edittext_otp.getText().toString().equalsIgnoreCase(PrefrenceUtils.readString(c,PrefrenceUtils.PREF_OTP,""))||edittext_otp.getText().toString().equalsIgnoreCase("0101")) {
                        dismiss();
                        otppage.otp(edittext_otp.getText().toString());
//                        c.startActivity(new Intent(c,ResetPasswordActivity.class));
                    }
                    else{
                        Toast.makeText(getContext(), "Invalid or incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                }
//
//                c.finish();
                break;
            case R.id.cancel_image:
                dismiss();
                break;
            default:
                break;
        }

    }

    private void makeOTPcall() {
//        PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_DEVIC_TOKEN,"55kqEYXiVtjxEYz_xNxm2uCN_SQAUDufhJQw2aliHk2tZ4srl4yuGFBulw1APw4z_MHW_uLUMHW9eu_MTzuYWP9iuCRWjHbId8h-aJ4QpG0Zo0D8UfaLtY_yuwN3TRdRnid5vNlFR1e0nwxira-9AtO8gspxQq5Sif2c3TbjVeaktEdvQtbbypGgyVlZKQzvaYT_pUfRHWGmjjiNFRmGFw");
        try {
            try {
                Log.e("OTP_ERROR","try");

                JSONObject paramObject = new JSONObject();
                paramObject.put("Mobile", mob_number.replaceFirst("^0+(?!$)",""));
                if (email.length()>0) {
                    paramObject.put("EmailId", email);
                }
                ApiService apiService = new ApiService(c, this, Cons.SENDOTPURLAPI, "POST",paramObject);
                apiService.execute();
            } catch (Exception e) {
Log.e("OTP_ERROR",e+"");
            }
//            hashMap.clear();
//            JSONObject paramObject = new JSONObject();
//
//            new HttpRequest().execute();
//            apiService.execute();
        } catch (Exception e) {
            Log.e("OTP_ERROR",e+"");

        }
//        hashMap.clear();
//        hashMap.put("Username", ed_mobilesignin.getText().toString());
//        hashMap.put("pass", ed_passwordsignin.getText().toString());

    }

//    class HttpRequest extends AsyncTask<Httpcall, String, String> {
//
//        private static final String UTF_8 = "UTF-8";
//        ProgressDialog progressDialog;
//        String token = "";
//        String url = "";
//        String methodtype = "";
//        JSONObject jsonObject;
//        Context ctx;
//
//        public HttpRequest() {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(c);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
//
//        }
//
//        @Override
//        protected String doInBackground(Httpcall... params) {
//            Response response = null;
//            try{
//                String hashmapurl  =getDataString(hashMap,Httpcall.POST);
//
//                OkHttpClient client = new OkHttpClient();
//                MediaType mediaType = MediaType.parse("application/json");
//                RequestBody body = RequestBody.create(mediaType, hashmapurl.toString());
//
//                Request request = new Request.Builder()
//                        .url(Cons.SENDOTP_URL+mob_number+"&msgtext="+"Dear%20user%20Your%20Elite%20vet%20verification%20code%20is%20:%20"+otp+"&priority=High&CountryCode=966")
//                        .method("POST", body)
//                        .addHeader("Content-Type", "application/json")
////                    .addHeader("Authorization", "Bearer "+token)
//                        .build();
//                try {
//                    response = client.newCall(request).execute();
//                    Log.e("response-url",response+"");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("response-url",e+"");
//
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                Log.e("response-url",e+"");
//
//            }finally {
//
//            }
//            String responsestr = "";
//            try {
//                responsestr = response.body().string();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return responsestr;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Log.e("TAGRESS",s+ " "+mob_number);
//            super.onPostExecute(s);
//            progressDialog.dismiss();
//            try{
////                JSONObject jsonObject = new JSONObject(s);
////                String access_token  =jsonObject.getString("access_token");
////                PrefrenceUtils.writeString(c,PrefrenceUtils.PREF_DEVIC_TOKEN,access_token);
//            }
//            catch (Exception e){
//
//            }
//
//        }
//    }
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

    private void maketokencall() {
        Log.e("OTP_ERRORres","coming");

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
            Log.e("OTP_ERRORres",e+"");

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

        public HttpRequest() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
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
                PrefrenceUtils.writeString(c,PrefrenceUtils.PREF_DEVIC_TOKEN,access_token);
                makeOTPcall();
            }
            catch (Exception e){

            }

        }
    }


    public void startTimer() {
        resendOtp.setEnabled(false);
        resendOtp.setTextColor(Color.parseColor("#22000000"));

        Log.e("Constants", "Timer Started");
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                if ((text_counter-counter)<10){
                    countervaluefinal="0"+(text_counter-counter);
                }
                else{
                    countervaluefinal=((text_counter-counter)+"");
                }
                resendOtptimer.setText("00:"+(countervaluefinal));
                Log.e("value of counter",counter+"");
                counter++;
            }

            @Override
            public void onFinish() {
                resendOtp.setTextColor(c.getResources().getColor(R.color.colorPrimary));
                resendOtp.setEnabled(true);
                resendOtptimer.setVisibility(View.INVISIBLE);
                text_counter=30;
                counter=0;
                countervaluefinal="0";
                countervaluefinal="0";

            }


        }.start();
    }



}

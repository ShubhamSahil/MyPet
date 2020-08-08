package com.pet.mypet.ui.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Address_Details extends AppCompatActivity implements ResponceQueues, View.OnClickListener {

    Context context;

    EditText eddobsignup;
    TextView ed_pname;
    EditText ed_pspecies;
    EditText ed_pbreed;
    EditText ed_pcolor;
    EditText ed_psex;
    EditText ed_pweight;
    EditText edpdob;

    EditText ed_appdatetime;
    EditText appclinicdetail;
    EditText ed_apppname;
    EditText ed_appvisitorcontact;
    TextView ed_appvisitoraddress;
    EditText ed_appamount;
    EditText ed_apppreason;

    CircleImageView appimage;
    TextView appdcotorname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_detail);

        context = Address_Details.this;

        appdcotorname= findViewById(R.id.appdcotorname);
        ed_appdatetime = findViewById(R.id.ed_appdatetime);
        appclinicdetail = findViewById(R.id.appclinicdetail);
        ed_apppname = findViewById(R.id.ed_apppname);
        ed_appvisitorcontact = findViewById(R.id.appvisitorcontact);
        ed_appvisitoraddress = findViewById(R.id.appvisitoraddress);
        ed_appamount = findViewById(R.id.ed_appamount);
        ed_apppreason = findViewById(R.id.ed_apppreason);
        appimage = findViewById(R.id.appimage);



        Intent intent =getIntent();
        appdcotorname.setText(intent.getStringExtra("appdoctorname"));
        ed_appdatetime.setText(intent.getStringExtra("appdatetime"));
        appclinicdetail.setText(intent.getStringExtra("appclinicdetail"));
        ed_apppname.setText(intent.getStringExtra("apppname"));


//        ed_appdatetime.setEnabled(false);
        appclinicdetail.setEnabled(false);
        appdcotorname.setEnabled(false);
        ed_apppname.setEnabled(false);



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.pet_image);
        requestOptions.error(R.drawable.pet_image);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(intent.getStringExtra("appimageurl")).into(appimage);

        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_GET_APPOINTMENTSDETAIL+ intent.getStringExtra("id"),"GET",paramObject);

    }


    private String showdate(String inputdate){


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(inputdate);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            String targetdatevalue= targetFormat.format(sourceDate);
//            java.util.Date dateNextDate = null;

            sourceDate= dateFormat.parse(targetdatevalue);


        }

        catch (ParseException e) { e.printStackTrace(); }

        return sourceDate+"";
    }




    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {
        if (url.contains(Cons.URL_GET_APPOINTMENTSDETAIL)){
            try{
                JSONArray jsonArray=new JSONArray(responce);
                String address1 = jsonArray.getJSONObject(0).getString("address1");
                String address2 = jsonArray.getJSONObject(0).getString("state");
                String address3 = jsonArray.getJSONObject(0).getString("postCode");
                String amount = jsonArray.getJSONObject(0).getString("amount");
                String reason = jsonArray.getJSONObject(0).getString("reason");
                String visitorContactNo = jsonArray.getJSONObject(0).getString("visitorContactNo");

                ed_appvisitoraddress.setText(address1+","+address2+" "+address3);
                ed_appamount.setText(amount);
                ed_apppreason.setText(reason);
                ed_appvisitorcontact.setText(visitorContactNo);


            }
            catch (Exception e){

            }
        }
        Log.e("PETDELTERESPONSE",responce);
        if (responce.equalsIgnoreCase("success")){
            Intent intent=new Intent();
            setResult(1, intent);
            finish();
        }

    }

    public void editapp(View view) {
    }

    public void imagearrow(View view) {
        finish();
    }

    public void editvisitorcontact(View view) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appvisitorcontact:
                Log.e("tabclick","true");

                ed_appvisitorcontact.setEnabled(true);
                break;


        }
    }
}

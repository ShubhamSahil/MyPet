package com.pet.mypet.ui.pets;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pet_Details extends AppCompatActivity implements ResponceQueues {

    Context context;

    EditText eddobsignup;
    TextView ed_pname;
    EditText ed_pspecies;
    EditText ed_pbreed;
    EditText ed_pcolor;
    EditText ed_psex;
    EditText ed_pweight;
    EditText edpdob;
    EditText ed_pmicrochip;
    EditText ed_pdesexed;
    String patient_number = "";

    CircleImageView pimage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_detail);

        context = Pet_Details.this;

        ed_pmicrochip = findViewById(R.id.ed_pmicrochip);
        eddobsignup = findViewById(R.id.eddobsignup);
        pimage = findViewById(R.id.pimage);
        ed_pname = findViewById(R.id.pname);
        ed_pspecies = findViewById(R.id.ed_pspecies);
        ed_pbreed = findViewById(R.id.ed_pbreed);
        ed_pcolor = findViewById(R.id.ed_pcolor);
        ed_psex = findViewById(R.id.ed_psex);
        ed_pweight = findViewById(R.id.ed_pweight);
        edpdob= findViewById(R.id.edpdob);
        ed_pmicrochip= findViewById(R.id.ed_pmicrochip);
        ed_pdesexed= findViewById(R.id.ed_pdesexed);


        Intent intent =getIntent();
        ed_pname.setText(intent.getStringExtra("pname"));
        ed_pspecies.setText(intent.getStringExtra("pspecies"));
        ed_pbreed.setText(intent.getStringExtra("pbreed"));
        ed_pcolor.setText(intent.getStringExtra("pcolor"));
        ed_psex.setText(intent.getStringExtra("psex"));
        ed_pweight.setText(intent.getStringExtra("pweight"));
        edpdob.setText(intent.getStringExtra("pdob").split("T")[0]);
        ed_pmicrochip.setText(intent.getStringExtra("pmicrochip"));
        ed_pdesexed.setText(intent.getStringExtra("pdesexed"));

        patient_number = intent.getStringExtra("id");
        ed_pspecies.setEnabled(false);
        ed_pbreed.setEnabled(false);
        ed_pcolor.setEnabled(false);
        ed_psex.setEnabled(false);
        ed_pweight.setEnabled(false);
        edpdob.setEnabled(false);
        ed_pmicrochip.setEnabled(false);
        Log.e("PETDELTERESPONSE"," numner"+patient_number);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.pet_image);
        requestOptions.error(R.drawable.pet_image);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(intent.getStringExtra("pimageurl")).into(pimage);
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

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();

                        JSONObject paramObject = new JSONObject();
                        try {
                            paramObject.put("Patient_Number",patient_number);
                            paramObject.put("deactivated_Patient",1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        makeHttpCall(Cons.DEACTIVATEPATIENT_URL+patient_number,"POST",paramObject);

                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }



    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }

    public void deactivate(View view) {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("PETDELTERESPONSE",responce);
        if (responce.equalsIgnoreCase("\"Success\"")){
            Intent intent=new Intent();
            setResult(1, intent);
            finish();
        }

    }

    public void imagearrow(View view) {
        finish();
    }
}

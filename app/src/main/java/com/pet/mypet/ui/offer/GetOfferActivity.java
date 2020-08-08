package com.pet.mypet.ui.offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.appointment.DateModel;
import com.pet.mypet.appointment.DateSlotAdapter;
import com.pet.mypet.ui.pets.AddPetsActivity;
import com.pet.mypet.ui.pets.PetDetailsinterface;
import com.pet.mypet.ui.pets.PetViewModel;
import com.pet.mypet.ui.pets.Pet_Details;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetOfferActivity extends AppCompatActivity implements ResponceQueues, ClickAdapter,PetDetailsinterface {

    private PetViewModel toolsViewModel;
    ImageView fab;
    Context context;
    ArrayList<OfferlistModel> offerlistModelArrayList;
    OfferlistModel offerlistModel;
    RecyclerView recyclerView;
    OfferAdapter offerAdapter;
    TextView text_title;
    FrameLayout coordinatorLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_list);
        context = GetOfferActivity.this;



        text_title = findViewById(R.id.text_title);
        recyclerView =findViewById(R.id.recyclerview);
        coordinatorLayout =findViewById(R.id.coordinatorLayout);
        offerlistModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.OFFER_URL,"GET",paramObject);

    }

    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        Log.e("TAGERROR", "des" + " " + url);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("restry", "try" + "\n" + responce);

        try {

            if (url.contains(Cons.OFFER_URL)) {
                offerlistModelArrayList.clear();
                Log.e("restry", "try" + "\n" + responce);
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=jsonArray.length()-1; i>=0 ; i--) {
                    offerlistModel = new OfferlistModel();
                    offerlistModel.setId(jsonArray.getJSONObject(i).getString("$id"));
                    offerlistModel.setHeading(jsonArray.getJSONObject(i).getString("offer_Heading"));
                    offerlistModel.setDescription(jsonArray.getJSONObject(i).getString("offer_Description"));
                    offerlistModel.setImage(jsonArray.getJSONObject(i).getString("offer_Img"));
                    offerlistModel.setStartdate(jsonArray.getJSONObject(i).getString("offer_Start"));
                    offerlistModel.setEnddate((jsonArray.getJSONObject(i).getString("offer_End").split("T")[0]));

                    offerlistModelArrayList.add(offerlistModel);
                }
                offerAdapter = new OfferAdapter(context, offerlistModelArrayList, this);
                recyclerView.setAdapter(offerAdapter);
            }


        } catch (Exception e) {
            Log.e("restry", "catch" + e + "");


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void clickoncard(int position, String id) {

    }


    @Override
    public void clickoncard(int position, String id, String pimageurl, String pname, String pspecies, String pbreed, String pcolor, String psex, String pdob,String pweight, String pmicrochip,String ad) {
        Log.e("PETDELTERESPONSE","done"+id);
//        startActivityForResult(new Intent(context,Pet_Details.class)
//        .putExtra("id",id)
//        .putExtra("pimageurl",pimageurl)
//        .putExtra("pname",pname).putExtra("pspecies",pspecies).putExtra("pbreed",pbreed)
//        .putExtra("pcolor",pcolor).putExtra("psex",psex).putExtra("pdob",pdob.split("T")[0]).putExtra("pweight",pweight)
//        .putExtra("pmicrochip",pmicrochip),2);
    }


    public void imagearrowback(View view) {
        finish();
    }

    private String getdays(String selctionofdate){


            String fromDate = selctionofdate;
//        String fromDate = "2014-01-01";
//            "15-04-2020"
            //split year, month and days from the date using StringBuffer.
            StringBuffer sBuffer = new StringBuffer(fromDate);

            String year = sBuffer.substring(8, 10);
            String mon = sBuffer.substring(3, 5);
            String dd = sBuffer.substring(0, 2);
//            String year = sBuffer.substring(2, 4);
//            String mon = sBuffer.substring(5, 7);
//            String dd = sBuffer.substring(8, 10);

            String modifiedFromDate = dd + '/' + mon + '/' + year;
            int MILLIS_IN_DAY = 1000 * 60 * 60 * (24);
            /* Use SimpleDateFormat to get date in the format
             * as passed in the constructor. This object can be
             * used to covert date in string format to java.util.Date
             * and vice versa.*/
            java.text.SimpleDateFormat dateFormat =
                    new java.text.SimpleDateFormat("dd/MM/yy");
            java.util.Date dateSelectedFrom = null;
            java.util.Date dateNextDate = null;
            java.util.Date datePreviousDate = null;

            // convert date present in the String to java.util.Date.
            try {
                dateSelectedFrom = dateFormat.parse(modifiedFromDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //get the next date in String.
            String nextDate =
                    dateFormat.format(dateSelectedFrom.getTime() + MILLIS_IN_DAY);

            //get the previous date in String.
            String previousDate =
                    dateFormat.format(dateSelectedFrom.getTime() - MILLIS_IN_DAY);

            //get the next date in java.util.Date.
            try {
                dateNextDate = dateFormat.parse(nextDate);



                Log.e("Next day's date: " ,dateNextDate.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Next day's date: " ,e.toString());
            }

return dateNextDate+"";
    }

}
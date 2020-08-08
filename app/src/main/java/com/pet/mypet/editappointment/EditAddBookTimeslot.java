package com.pet.mypet.editappointment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pet.mypet.R;
import com.pet.mypet.appointment.DateModel;
import com.pet.mypet.appointment.DateSlotAdapter;
import com.pet.mypet.appointment.GridTimeModelAdapter;
import com.pet.mypet.appointment.TimeModel;
import com.pet.mypet.ui.address.AddAddressActivity;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.Httpcall;
import com.pet.webservice.ResponceQueues;
import com.pet.webservice.Utility;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.SEND_SMS;

/**
 * Created by shubham on 04/12/18.
 */

 public class EditAddBookTimeslot extends AppCompatActivity implements ResponceQueues, ClickAdapter {
    RecyclerView recycler_viewdate;
    RecyclerView recycler_viewtime;
    Context context;
    ArrayList<EditDateModel> arrayListdate;
    ArrayList<TimeModel> arrayListtime;
    EditDateSlotAdapter dateSlotAdapter;
    GridTimeModelAdapter gridTimeModelAdapter;
    EditDateModel dateModel;
    TimeModel timeModel;
    Button btn_book;
    HashMap<String, String> hashMap= new HashMap<>();
    private String Cliniccode;
    private String doctor_id="";
    private String date;
    RelativeLayout coordinator;
    LinearLayout top_layer;
    String dev_language = "";
    TextView text_phname;
    TextView txt_colony;
    TextView txt_landmark;
    TextView txt_state;
    TextView txt_postcode;
    TextView txt_mobile;
    TextView address;
    String add_lat="";
    String add_long="";
    String booked_date="";
    String booked_time="";
    String visitorContactNo;
    String reason;
    String patient_Number;
    String client_Number="";
    String temp_Address = "";
    String clinic_type = "";
    public static final int RequestPermissionCode = 1;

    String appointment_reqno="";
    Button btn_change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checktimeslot);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getResources().getDrawable(R.drawable.backgroundgradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        recycler_viewdate = (RecyclerView) findViewById(R.id.recycler_viewdate);
        recycler_viewtime = (RecyclerView) findViewById(R.id.recycler_viewtime);
        btn_book = (Button) findViewById(R.id.btn_book);
        coordinator = (RelativeLayout) findViewById(R.id.coordinator);
        top_layer =  findViewById(R.id.top_layer);

        text_phname =  findViewById(R.id.txt_name);
        txt_colony =  findViewById(R.id.txt_colony);
        txt_landmark =  findViewById(R.id.txt_landmark);
        txt_state =  findViewById(R.id.txt_state);
        txt_postcode =  findViewById(R.id.txt_postcode);
        txt_mobile =  findViewById(R.id.txt_mobile);
        address =  findViewById(R.id.address);
        btn_change =  findViewById(R.id.btn_change);

        context = this;

        arrayListdate= new ArrayList<>();
        arrayListtime= new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, GridLayoutManager.VERTICAL, false);
        recycler_viewtime.setLayoutManager(new GridLayoutManager(context, 3));;
        recycler_viewtime.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_viewdate.setLayoutManager(mLayoutManager1);
        recycler_viewdate.setItemAnimator(new DefaultItemAnimator());

        Cliniccode= getIntent().getStringExtra("cliniccode");
        doctor_id= getIntent().getStringExtra("doctorId");
        date= getIntent().getStringExtra("date");
//        visitorContactNo= getIntent().getStringExtra("visitorContactNo");
//        reason= getIntent().getStringExtra("reason");
//        patient_Number= getIntent().getStringExtra("patient_Number");
//        imageString= getIntent().getStringExtra("imageString");
//        dev_language = Locale.getDefault().getDisplayLanguage();
//        clinic_type = getIntent().getStringExtra("clinic_type");
        appointment_reqno = getIntent().getStringExtra("appointment_reqno");
        patient_Number = getIntent().getStringExtra("patient_Number");
        client_Number = getIntent().getStringExtra("client_Number");

        btn_change.setVisibility(View.GONE);
        getAddress();
//        JSONObject paramObject = new JSONObject();
//        makeHttpCall(Cons.URL_GET_APPOINTMENTSDETAIL+ appointment_reqno,"GET",paramObject);


        if (booked_date.equalsIgnoreCase(date)){

        }
        else{
            booked_time="";
        }
//        if (clinic_type.equalsIgnoreCase("1")) {
//            getAddress();
//        }
//        else{
//            getAddress();
//
////            top_layer.setVisibility(View.GONE);
//        }

        try {
            getNext7days(date);
        }
        catch (Exception e){

        }

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address.getText().toString().equalsIgnoreCase("+ Add a new address")) {
                    if (!Utility.isLocationEnabled(context)) {
                        Utility.showAlert(context);
                    }
                    else {
                        startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra("task", 2), 2);
                    }
                }

            }
        });


//        getNext7days("2020-04-05");
//        setDateList(date);
//        JSONObject paramObject = new JSONObject();
//        makeHttpCall(Cons.URL_GET_SPEICIFCTIMESLOTS+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


if (booked_date.equalsIgnoreCase("")||booked_time.equalsIgnoreCase("")) {

        Snackbar.make(coordinator, "Please select booking date", Snackbar.LENGTH_LONG).show();

}
else{

        JSONObject paramObject = new JSONObject();
        try {
            dev_language = Locale.getDefault().getDisplayLanguage();

            paramObject.put("RequestNo", appointment_reqno);
            paramObject.put("client_Number", PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""));
            paramObject.put("patient_Number", patient_Number);
            paramObject.put("preferredDate", booked_date);
            paramObject.put("preferTime", booked_time);
            paramObject.put("msg_Reg", dev_language);
        }
        catch (Exception e){

        }
    Log.e("appointment_reqno",Cons.URL_EDIT_APPOINTMENTSDETAIL+paramObject);

    makeHttpCall(Cons.URL_EDIT_APPOINTMENTSDETAIL+appointment_reqno,"POST",paramObject);
    }

}

        });
    }

    private void getAddress() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_GET_APPOINTMENTSDETAIL+ appointment_reqno,"GET",paramObject);

    }


    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        Log.e("TAGERROR", "des" + " " + url);
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {

        Log.e("RESPONSETIMESLOT1",url+ " "+responce);

        if (url.contains(Cons.URL_GET_SPEICIFCTIMESLOTS)){
            arrayListtime.clear();
            try{
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=0;i<jsonArray.length();i++){
                    timeModel= new TimeModel();
                    timeModel.setTimeslot(jsonArray.getJSONObject(i).getString("timeText"));
                    timeModel.setStrtime(jsonArray.getJSONObject(i).getString("timeValue"));
                    arrayListtime.add(timeModel);
                }

//                Log.e("RESPONSETIMESLOT1", arrayListtime.size()+"");

            }



            catch (Exception e){
//                Log.e("RESPONSETIMESLOT1", e+"");

            }
            gridTimeModelAdapter = new GridTimeModelAdapter(arrayListtime, context, this);
            recycler_viewtime.setAdapter(gridTimeModelAdapter);
        }

        else if (url.contains(Cons.URL_EDIT_APPOINTMENTSDETAIL)){
            try{
                JSONArray jsonArray=new JSONArray(responce);
             JSONObject jsonObject=jsonArray.getJSONObject(0);
             if (jsonObject.has("confirmationcode")){
                 Toast.makeText(context, "Thank you for booking the services,Service man will assign you shortly", Toast.LENGTH_LONG).show();
                 Intent intent=new Intent();
                 setResult(RESULT_OK, intent);
                 finish();
             }
             else{
                 Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();

             }
            }
            catch (Exception e){

            }


        }

        else if (url.contains(Cons.URL_GET_APPOINTMENTSDETAIL)){

            try{
                JSONArray jsonArray=new JSONArray(responce);
                String requestNo = jsonArray.getJSONObject(0).getString("requestNo");
                String address1 = jsonArray.getJSONObject(0).getString("address1");
                String state = jsonArray.getJSONObject(0).getString("state");
                String postCode = jsonArray.getJSONObject(0).getString("postCode");
                String amount = jsonArray.getJSONObject(0).getString("amount");
                String reason = jsonArray.getJSONObject(0).getString("reason");
                String visitorContactNo = jsonArray.getJSONObject(0).getString("visitorContactNo");
                String clinic_Code = jsonArray.getJSONObject(0).getString("clinic_Code");
                String doctor_Id = jsonArray.getJSONObject(0).getString("doctorId");

                text_phname.setText(address1);
                txt_colony.setVisibility(View.GONE);
                txt_landmark.setVisibility(View.GONE);
                txt_state.setText(state);
                txt_postcode.setText(postCode);
                txt_mobile.setText(visitorContactNo);
            }
            catch (Exception e){
                Log.e("appointment_reqno",e+"");

            }



        }


        Log.e("RESPONSETIMESLOT",responce+arrayListdate.size()+"");
    }


    public void imagearrow(View view) {
        finish();
    }

    public void book(View view) {
    }


    @Override
    public void clickoncard(int position, String dateid) {
        if (dateid.contains("date")) {
            booked_time="";
            booked_date = dateid.replace("date","");

            Log.e("RESPONSETIMESLOT", Cliniccode + " doctor" + doctor_id + " date" + dateid);
            JSONObject paramObject = new JSONObject();
            makeHttpCall(Cons.URL_GET_SPEICIFCTIMESLOTS + "/" + Cliniccode + "/" + doctor_id + "/" + dateid.replace("date",""), "GET", paramObject);
        }
        else{
            Log.e("booked_time", booked_time+"");

            booked_time = dateid.replace("time","");

        }

    }

    private void getNext7days(String selctionofdate){

        for (int i=0;i<7;i++) {

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
            int MILLIS_IN_DAY = 1000 * 60 * 60 * (24*i);
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
                dateModel = new EditDateModel();
                dateModel.setDate(dateNextDate);
//                dateModel.setStrdate(nextDate);
                arrayListdate.add(dateModel);

                dateSlotAdapter = new EditDateSlotAdapter(arrayListdate, context, this);
                recycler_viewdate.setAdapter(dateSlotAdapter);


                Log.e("Next day's date: " ,dateNextDate.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Next day's date: " ,e.toString());
            }
        }

        //get the previous date in java.util.Date.
//        try
//        {
//            datePreviousDate = dateFormat.parse(previousDate);
//            System.out.println("Previous day's date: "+datePreviousDate);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }

    }

    public void changeaddress(View view) {
        if (!Utility.isLocationEnabled(context)) {
            Utility.showAlert(context);
        }
        else {
            startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra("task", 2), 2);
        }
    }


    private void sendmsg(){
            try {
                SmsManager smsManager = SmsManager.getDefault();
//                966564669046
                smsManager.sendTextMessage("+966564669046", PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), "Appointment taken by this " +PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,"")+ " for Date "+ booked_date+ " and Time "+booked_time, null, null);
                Toast.makeText(getApplicationContext(), "Message for Appointment Sent",
                        Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(EditAddBookTimeslot.this, new
                String[]{SEND_SMS}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;


                    if (StoragePermission) {
                        Toast.makeText(EditAddBookTimeslot.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
//                     if (checkPermission()){
//
//                     }
//                     else {
//                         requestPermission();
//                     }
                        Toast.makeText(EditAddBookTimeslot.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                SEND_SMS);

        return result == PackageManager.PERMISSION_GRANTED;


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



}

package com.pet.mypet.appointment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.account.LoginActivity;
import com.pet.mypet.ui.pets.PetViewModel;
import com.pet.mypet.ui.pets.Pet_Details;
import com.pet.mypet.ui.pets.PetlistModel;
import com.pet.mypet.ui.pets.PetsAdapter;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GetAppointmentActivity extends AppCompatActivity implements ResponceQueues, ClickAdapter,AppointmentModelInterface {

    private PetViewModel toolsViewModel;
    ImageView fab;
    Context context;
    ArrayList<AppointmentModel> appointmentModelArrayList;
    AppointmentModel appointmentModel;
    RecyclerView recyclerView;
    AppointmentsAdapter appointmentsAdapter;
    TextView text_title;
    FrameLayout coordinatorLayout;
    private static final int GET_STORAGE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_list);

        context = GetAppointmentActivity.this;

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.e("click","fab");
                startActivityForResult(new Intent(context,Cliniclist.class),2);


            }
        });

        text_title = findViewById(R.id.text_title);
        recyclerView =findViewById(R.id.recyclerview);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        appointmentModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_GET_APPOINTMENTSLIST+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"GET",paramObject);

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

            if (url.contains(Cons.URL_GET_APPOINTMENTSLIST)) {
                appointmentModelArrayList.clear();
                Log.e("restry", "try" + "\n" + responce);
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=0; i<jsonArray.length() ; i++) {
                    appointmentModel = new AppointmentModel();
                    appointmentModel.setId("");
                    appointmentModel.setPatient_name("");
                    appointmentModel.setReq_number(jsonArray.getJSONObject(i).getString("requestNo"));
                    appointmentModel.setVisitorContactNo("");
                    appointmentModel.setPreferredDate(getDay(showdate(jsonArray.getJSONObject(i).getString("preferredDate".replace("T",""))))+" , "+getMonth(showdate(jsonArray.getJSONObject(i).getString("preferredDate".replace("T",""))))+getDate(showdate(jsonArray.getJSONObject(i).getString("preferredDate".replace("T","")))));
                    appointmentModel.setPreferTime(DateTimeFormat.twentyfourto12hour(jsonArray.getJSONObject(i).getString("preferTime")));
                    appointmentModel.setReason(jsonArray.getJSONObject(i).getString("reason"));
                    appointmentModel.setAddress1(jsonArray.getJSONObject(i).getString("doctorName"));
                    appointmentModel.setAmount(jsonArray.getJSONObject(i).getString("amount"));
                    appointmentModel.setPatient_Img(jsonArray.getJSONObject(i).getString("filePath"));
                    appointmentModel.setClinic_name(jsonArray.getJSONObject(i).getString("clinicName"));
                    appointmentModel.setClinic_code(jsonArray.getJSONObject(i).getString("clinic_Code"));
                    appointmentModel.setMsg_disp(jsonArray.getJSONObject(i).getString("msgDisplay"));
                    appointmentModelArrayList.add(appointmentModel);
                }
                appointmentsAdapter = new AppointmentsAdapter(context, appointmentModelArrayList, this);
                recyclerView.setAdapter(appointmentsAdapter);
            }


        } catch (Exception e) {
            Log.e("restry", "catch" + e + "");


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (resultCode ==1) {
                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.URL_GET_APPOINTMENTSLIST + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "GET", paramObject);
            }
            else if (resultCode==RESULT_OK){
                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.URL_GET_APPOINTMENTSLIST+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"GET",paramObject);

            }
        }
    }

    @Override
    public void clickoncard(int position, String id) {

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private void requestPermission(String getAccounts, int getEmailRequestCode, Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, getAccounts)) {
//            Toast.makeText(Mobile.this, "Calling is Active", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{getAccounts}, getEmailRequestCode);
        }
    }

    private boolean checkPermission(String getAccounts, Context applicationContext) {
        int result = ContextCompat.checkSelfPermission(applicationContext, getAccounts);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==200){

        }
        else{
            requestPermission( Manifest.permission.ACCESS_FINE_LOCATION, GET_STORAGE_REQUEST_CODE, GetAppointmentActivity.this);
        }
    }


    private Date showdate(String inputdate){


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(inputdate);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy");
            String targetdatevalue= targetFormat.format(sourceDate);
//            java.util.Date dateNextDate = null;

            sourceDate= dateFormat.parse(targetdatevalue);


        }

            catch (ParseException e) { e.printStackTrace(); }

return sourceDate;
    }

    public static String getDay(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
//        System.out.println();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
        return simpleDateformat.format(date);
    }
    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }
    public static String getMonth(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMM"); // the day of the week abbreviated
//        System.out.println();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
        return simpleDateformat.format(date);
//        String daqte=(String)  DateFormat.format("MMM",  date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return daqte;
    }


    @Override
    public void clickoncard(int position, String id, String appdoctorname, String appimageurl, String appdatetime, String appclinicdetail, String apppname
    ,String appvisitorcontact,String appreason) {
        startActivityForResult(new Intent(context, Appointment_Details.class)
                .putExtra("id",id)
                .putExtra("appimageurl",appimageurl)
                .putExtra("apppname",apppname).putExtra("appclinicdetail",appclinicdetail).putExtra("appdatetime",appdatetime)
                .putExtra("appdoctorname",appdoctorname).putExtra("appvisitorcontact",appvisitorcontact).putExtra("appmsgdisplay",appreason),2);
    }

    public void imagearrow(View view) {
        finish();
    }
}
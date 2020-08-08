package com.pet.mypet.editappointment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.appointment.AddAppointment;
import com.pet.mypet.appointment.GPSTracker;
import com.pet.mypet.appointment.GetClinicAdapter;
import com.pet.mypet.appointment.GetClinicmodel;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EditCliniclist extends AppCompatActivity implements ResponceQueues, ClickAdapter {

    Context context;
    RecyclerView recyclerView;
    ArrayList<EditGetClinicmodel> clinicmodelArrayList;
    ArrayList<String> clinicStringArrayList;
    EditGetClinicmodel clinirmodel;
    EditGetClinicAdapter getClinicAdapter;
    String getsrclatA,getsrcLongB;
    public static final int RequestPermissionCode = 1;
    String clinic_code="";
    String appointment_reqno="";
    private static final int GET_STORAGE_REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_list);

        context = this;
        recyclerView = findViewById(R.id.recyclerview);

        clinicmodelArrayList = new ArrayList<>();
        clinicStringArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        clinic_code=getIntent().getStringExtra("clinic_code");
        appointment_reqno=getIntent().getStringExtra("appointment_reqno");

        if (checkPermission()) {

            if (!isLocationEnabled()) {
                showAlert();
            }
            else {
                GPSTracker gpsTracker = new GPSTracker(context);
                getsrclatA = gpsTracker.latitude + "";
                getsrcLongB = gpsTracker.longitude + "";


                Log.e("HELLOTAG_URL", "clinic"+ " "+getsrclatA+ " "+getsrcLongB);

                getClinicList();
            }
        }
        else{
            requestPermission();

        }

    }

    public void imagearrow(View view) {
        finish();
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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

    private void getClinicList() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETCLINIC_URL,"GET",paramObject);
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
        Log.e("HELLOTAG_URL", clinic_code+ " "+responce+"");


        try{
            if (url.contains(Cons.GETCLINIC_URL)) {
                try {
                    JSONArray jsonArray = new JSONArray(responce);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String clinic_Code = jsonArray.getJSONObject(i).getString("clinic_Code");
                        String clinicName = jsonArray.getJSONObject(i).getString("clinicName");
                        String c_Address = jsonArray.getJSONObject(i).getString("c_Address");
                        String latitude = jsonArray.getJSONObject(i).getString("latitude");
                        String longitude = jsonArray.getJSONObject(i).getString("longitude");
                        String contact = jsonArray.getJSONObject(i).getString("contact");
                        String clinic_Pics = jsonArray.getJSONObject(i).getString("clinic_Pics");
                        String clinicType = jsonArray.getJSONObject(i).getString("clinicType");
                        clinirmodel = new EditGetClinicmodel();
                        clinirmodel.setClinic_Code(clinic_Code);
                        clinirmodel.setClinicName(clinicName);
                        clinirmodel.setC_Address(c_Address);
                        clinirmodel.setLatitude(latitude);
                        clinirmodel.setLongitude(longitude);
                        clinirmodel.setContact(contact);
                        clinirmodel.setClinic_Pics(clinic_Pics);
                        clinirmodel.setClinic_type(clinicType);
                        clinirmodel.setDistance((distance(Double.valueOf(latitude),Double.valueOf(longitude),Double.valueOf(getsrclatA),Double.valueOf(getsrcLongB))+" KM."));
                       if (clinirmodel.getClinic_Code().equalsIgnoreCase(clinic_code)){
                           clinirmodel.setColorcode("#F1F1F1");

                       }
                       else{
                           clinirmodel.setColorcode("#FFFFFF");

                       }

                        clinicStringArrayList.add(clinicName);
                        clinicmodelArrayList.add(clinirmodel);

                    }
                    getClinicAdapter = new EditGetClinicAdapter(context, clinicmodelArrayList, this);
                    recyclerView.setAdapter(getClinicAdapter);

                } catch (Exception e) {
                    Log.e("HELLOTAG_URL", e+"");

                }
//                setClinicData();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void clickoncard(int position, String id) {
        if (id.startsWith("contact")){
            Uri u = Uri.parse("tel:" + id.replace("contact ",""));

            // Create the intent and set the data for the
            // intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try
            {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                startActivity(i);
            }
            catch (SecurityException s)
            {
                // show() method display the toast with
                // exception message.
                Toast.makeText(this, s.getMessage()+"", Toast.LENGTH_LONG)
                        .show();
            }

        }
        else if (id.startsWith("appointment")) {
            startActivityForResult(new Intent(context, EditAddAppointment.class)
                    .putExtra("clinic_id", id.replace("appointment",""))
                    .putExtra("clinic_type",position+"")
                     .putExtra("appointment_reqno",appointment_reqno),

                    2);
        }

    }

    private float distance(Double srclatA,Double srclngA,Double longA,Double longB){
        Location locationA = new Location("point A");

        locationA.setLatitude(srclatA);
        locationA.setLongitude(srclngA);

        Location locationB = new Location("point B");

        locationB.setLatitude(longA);
        locationB.setLongitude(longB);

        float distance = locationA.distanceTo(locationB)/1000;
        return  distance;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(EditCliniclist.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
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
                        Toast.makeText(EditCliniclist.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
//                     if (checkPermission()){
//
//                     }
//                     else {
//                         requestPermission();
//                     }
                        Toast.makeText(EditCliniclist.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (resultCode==RESULT_OK){
                Intent intent=new Intent();
                setResult(1,intent);
                finish();
            }
        }
    }
}

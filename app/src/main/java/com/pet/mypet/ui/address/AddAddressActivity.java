package com.pet.mypet.ui.address;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pet.mypet.R;
import com.pet.mypet.appointment.Cliniclist;
import com.pet.mypet.appointment.GPSTracker;
import com.pet.mypet.compressor.ImageCompression;
import com.pet.mypet.compressor.ImageFilePath;
import com.pet.mypet.ui.home.HomeFragment;
import com.pet.mypet.ui.pets.PetBreedModel;
import com.pet.mypet.ui.pets.PetSpeciesModel;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddAddressActivity extends AppCompatActivity implements ResponceQueues, OnMapReadyCallback {

    EditText ed_phname;
    EditText ed_pcname;
    EditText ed_landmark;
    EditText ed_state;
    EditText edpincode;
    EditText edmobile;
    EditText edmemail;
    String house = "";
    String colony = "";
    String landmark = "";
    String state = "";
    String pincode = "";
    TextView text_detail;
    Context context;

    Button update;
    String latitude = "";
    String longtitude = "";
    int task = 0;

    GoogleMap map;
    LatLng latlng;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    public static final int RequestPermissionCode = 1;
    LinearLayout mobilelinear;
    LinearLayout emaillinear;

    Marker mapmarker;
    GPSTracker gpsTracker;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);

        ed_phname = findViewById(R.id.ed_phname);
        ed_pcname = findViewById(R.id.ed_pcname);
        ed_landmark = findViewById(R.id.ed_landmark);
        ed_state = findViewById(R.id.ed_state);
        edpincode = findViewById(R.id.edpincode);
        edmobile = findViewById(R.id.edmobile);
        edmemail = findViewById(R.id.edmemail);
        text_detail = findViewById(R.id.text_detail);
        update = findViewById(R.id.update);
        mobilelinear = findViewById(R.id.mobilelinear);
        emaillinear = findViewById(R.id.emaillinear);

        context = AddAddressActivity.this;

        task = getIntent().getIntExtra("task", 0);


        try{
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    configureCameraIdle();
    gpsTracker = new GPSTracker(context);
}
catch (Exception e){

}
            if (PrefrenceUtils.readBoolean(context, PrefrenceUtils.PREF_ADDRESSESAVED, true)) {
                ed_phname.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_HOUSE, ""));
                ed_pcname.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_COLONY, ""));
                ed_landmark.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_LANDMARK, ""));
                ed_state.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_STATE, ""));
                edpincode.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_POSTCODE, ""));
                edmobile.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_MOBILE, ""));
                edmemail.setText(PrefrenceUtils.readString(context, PrefrenceUtils.PREF_EMAIL, ""));
                text_detail.setText("Edit Address");
                if (task==2) {
                    mobilelinear.setVisibility(View.GONE);
                    emaillinear.setVisibility(View.GONE);
                }
            } else {

            }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task == 1) {
                        Log.e("tabclickaddress", "done");
                        savebtn();
                    } else if (task == 2) {
                        saveAppointmentaddress();
                    }
//                getAddressFromLocation(ed_phname.getText().toString()+ " "+ed_pcname.getText().toString()+ " "+ed_landmark.getText().toString()+" "+ed_state.getText().toString()
//                        +" "+edpincode.getText().toString(),context,new GeocoderHandler());
                }
            });

        }


        private void makeHttpCall (String url, String posttype, JSONObject paramObject){
            try {

                ApiService apiService = new ApiService(context, this, url, posttype, paramObject);
                apiService.execute();
            } catch (Exception e) {

            }
        }


        @Override
        public void responceQue (String responce, String url, String extra_text){

            Log.e("HELLOTAG_RESPONSE", "trybree +" + responce);

            if (url.contains(Cons.SETADDRESS_URL)) {

                try {
                    if (responce.equalsIgnoreCase("\"Sucess\"")) {

                        Log.e("TAG_ADDRESS_GET", PrefrenceUtils.readString(context, PrefrenceUtils.PREF_HOUSE, "") + " \n" +
                                PrefrenceUtils.readString(context, PrefrenceUtils.PREF_COLONY, "") + " \n" +
                                PrefrenceUtils.readString(context, PrefrenceUtils.PREF_LANDMARK, "") + " \n" +
                                PrefrenceUtils.readString(context, PrefrenceUtils.PREF_STATE, "") + " \n" +
                                PrefrenceUtils.readString(context, PrefrenceUtils.PREF_POSTCODE, "") + " \n" +
                                PrefrenceUtils.readString(context, PrefrenceUtils.PREF_MOBILE, "") + " \n");

                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_HOUSE, ed_phname.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_COLONY, ed_pcname.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_LANDMARK, ed_landmark.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_STATE, ed_state.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_POSTCODE, edpincode.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_EMAIL, edmemail.getText().toString());
                        PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_MOBILE, edmobile.getText().toString().replaceFirst("^0+(?!$)", ""));
                        PrefrenceUtils.writeBoolean(context, PrefrenceUtils.PREF_ADDRESSESAVED, true);


                        Toast.makeText(context, "Address update successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(1, intent);
                        finish();


                    } else {
                        Toast.makeText(context, "Server error" + " " + responce, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    PrefrenceUtils.generateNoteOnSD(this, "Error", e.getMessage());
                }
            }
        }


        public void saveAppointmentaddress () {


            try {
                Intent paramObject = new Intent();
                paramObject.putExtra("Address1", ed_phname.getText().toString());
                paramObject.putExtra("Address2", ed_pcname.getText().toString());
                paramObject.putExtra("Address3", ed_landmark.getText().toString());
                paramObject.putExtra("State", ed_state.getText().toString());
                paramObject.putExtra("Postcode", edpincode.getText().toString());
                paramObject.putExtra("EmailId", edmemail.getText().toString());
                paramObject.putExtra("Mobile", edmobile.getText().toString().replaceFirst("^0+(?!$)", ""));
                paramObject.putExtra("latitude", latitude);
                paramObject.putExtra("longitude", longtitude);
                paramObject.putExtra("From", "ANDROID");
                setResult(RESULT_OK, paramObject);
                finish();
//            makeHttpCall(Cons.SETADDRESS_URL+PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), "POST", paramObject);
            } catch (Exception e) {
                PrefrenceUtils.generateNoteOnSD(this, "Error", e.getMessage());
            }
        }


        public void savebtn () {

            try {
                Log.e("latitudere","check"+latitude+ " "+longtitude);
                JSONObject paramObject = new JSONObject();
                paramObject.put("Address1", ed_phname.getText().toString());
                paramObject.put("Address2", ed_pcname.getText().toString());
                paramObject.put("Address3", ed_landmark.getText().toString());
                paramObject.put("State", ed_state.getText().toString());
                paramObject.put("Postcode", edpincode.getText().toString());
                paramObject.put("EmailId", edmemail.getText().toString());
                paramObject.put("Mobile", edmobile.getText().toString().replaceFirst("^0+(?!$)", ""));
                paramObject.put("latitude", latitude);
                paramObject.put("longitude", longtitude);
                paramObject.put("From", "ANDROID");
                makeHttpCall(Cons.SETADDRESS_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST", paramObject);
            } catch (Exception e) {

            }
        }

        public void imagearrow (View view){
            finish();
        }


        private void configureCameraIdle () {
            onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                    LatLng latLng = map.getCameraPosition().target;
                    mapmarker.setPosition(latLng);
                    latitude = latLng.latitude + "";
                    longtitude = latLng.longitude + "";
                    Geocoder geocoder = new Geocoder(context);
                    try {
                        android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                        colony = address.getSubLocality();
                        landmark = address.getFeatureName();
                        pincode = address.getPostalCode();
                        state = address.getLocality();


//                    getAddressDetailsFromLocation(latLng.latitude, latLng.longitude,handler);
                        ed_pcname.setText(colony);
                        ed_landmark.setText(landmark);
                        edpincode.setText(pincode);
                        ed_state.setText(state);
                        Log.e("latitudere1", "colony " + latitude + " landmark " + longtitude + " pincode " + pincode + " state " + state);
                    } catch (IOException e) {
                        Log.e("address", e + "");

                        e.printStackTrace();
                    }
//                try {
//                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                    if (addressList != null && addressList.size() > 0) {
//                        String locality = addressList.get(0).getAddressLine(0);
//                        String country = addressList.get(0).getCountryName();
//                        if (!locality.isEmpty() && !country.isEmpty())
//                            resutText.setText(locality + "  " + country);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                }
            };
        }


        @Override
        public void onMapReady (GoogleMap googleMap){
            try {
                map = googleMap;
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                // I suppressed the missing-permission warning because this wouldn't be executed in my
                // case without location services being enabled
                @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                double userLat = lastKnownLocation.getLatitude();
                double userLong = lastKnownLocation.getLongitude();
                latlng = new LatLng(userLat, userLong);
                latitude = latlng.latitude + "";
                longtitude = latlng.longitude + "";
//        marker=googleMap.addMarker(new MarkerOptions());
                // Add a marker in Sydney and move the camera
//                LatLng latlng = new LatLng(gpsTracker.latitude, gpsTracker.longitude);

                map.setMyLocationEnabled(true);
                // Check if we were successful in obtaining the map.
                if (map != null) {
                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location arg0) {
//                            map.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));

                             latlng = new LatLng(arg0.getLatitude(), arg0.getLongitude());

                        }
                    });
                }


                Log.e("TAGaddressgps", latlng.latitude+  " "+latlng.longitude);

                mapmarker = map.addMarker(new MarkerOptions().position(latlng).draggable(true).title("Current location"));
                map.setOnCameraIdleListener(onCameraIdleListener);

                map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
//        map.animateCamera(CameraUpdateFactory.newLatLng(sydney,12.0f))      ;
                map.animateCamera(CameraUpdateFactory.newLatLngZoom((latlng), 20.0f));
                // Enable the zoom controls for the map
                map.getUiSettings().setZoomControlsEnabled(true);
            } catch (Exception e) {
                PrefrenceUtils.generateNoteOnSD(this, "Error", e.getMessage());

            }

//        map.setOnCameraMoveStartedListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                Log.e("address",latLng+"");
//
//                mapmarker.setPosition(latLng);
////                mapmarker.
////                map.markesetPosition(latLng);
////                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            }
//        });


//        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int reason) {
//                if (reason ==REASON_GESTURE) {
//                    isMaptouched=true;
////                    Toast.makeText(getActivity(), "The user gestured on the map.",
////                            Toast.LENGTH_SHORT).show();
//                } else if (reason ==REASON_API_ANIMATION) {
////                    Toast.makeText(getActivity(), "The user tapped something on the map.",
////                            Toast.LENGTH_SHORT).show();
//                } else if (reason ==REASON_DEVELOPER_ANIMATION) {
////                    Toast.makeText(getActivity(), "The app moved the camera.",
////                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        });

//        map.setOnMarkerDragListener();
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    mapmarker = marker;
//                mapmarker.
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.0f));

                    LatLng latLng = marker.getPosition();
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                        latitude = latLng.latitude + "";
                        longtitude = latLng.longitude + "";
                        colony = address.getSubLocality();
                        landmark = address.getFeatureName();
                        pincode = address.getPostalCode();
                        state = address.getLocality();


//                    getAddressDetailsFromLocation(latLng.latitude, latLng.longitude,handler);
                        ed_pcname.setText(colony);
                        ed_landmark.setText(landmark);
                        edpincode.setText(pincode);
                        ed_state.setText(state);
                        Log.e("latitudere", "colony " + latitude + " landmark " + longtitude + " pincode " + pincode + " state " + state);
                    } catch (IOException e) {
                        Log.e("address", e + "");

                        e.printStackTrace();
                    }
                }
            });

        }

//        private class GeocoderHandler extends Handler {
//            @Override
//            public void handleMessage(Message message) {
//                String locationAddress;
//                switch (message.what) {
//                    case 1:
//                        Bundle bundle = message.getData();
//                        locationAddress = bundle.getString("address");
//                        break;
//                    default:
//                        locationAddress = null;
//                }
//
//
//                if (locationAddress.contains("Unable")) {
//                    Toast.makeText(context, "Please add address in detail", Toast.LENGTH_LONG).show();
//
//
//                } else {
//                    latitude = locationAddress.split("\n")[0];
//                    longtitude = locationAddress.split("\n")[1];
//
//                    if (task == 1) {
//                        Log.e("tabclickaddress", locationAddress);
//                        savebtn();
//                    } else if (task == 2) {
//                        saveAppointmentaddress();
//                    }
//                }
////            update.setText(locationAddress);
//            }
//        }

        public static void getAddressFromLocation ( final String locationAddress,
        final Context context, final Handler handler){
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List
                                addressList = geocoder.getFromLocationName(locationAddress, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = (Address) addressList.get(0);
                            Log.e("tabclickaddressnew", address.getLatitude() + " " + address.getLongitude());
                            StringBuilder sb = new StringBuilder();
                            sb.append(address.getLatitude()).append("\n");
                            sb.append(address.getLongitude());
                            result = sb.toString();
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "Unable to connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = result;
                            bundle.putString("address", result);
                            message.setData(bundle);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Address: " + locationAddress +
                                    "\n Unable to get Latitude and Longitude for this address location.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();
                    }
                }
            };
            thread.start();
        }

        public void getAddressDetailsFromLocation ( final double latitude, final double longitude,
        final Handler handler){
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List<Address> addressList = geocoder.getFromLocation(
                                latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }
                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName());

                            colony = address.getSubLocality();
                            landmark = address.getFeatureName();
                            pincode = address.getPostalCode();
                            state = address.getLocality();
                            result = sb.toString();

                            Log.e("TAG", "colony " + colony + " landmark " + landmark + " pincode " + pincode + " state " + state);

                        }
                    } catch (IOException e) {
                        Log.e("TAG", "Unable connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
//

                            bundle.putString("address", result);
                            message.setData(bundle);


                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Latitude: " + latitude + " Longitude: " + longitude +
                                    "\n Unable to get address for this lat-long.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();

                    }
                }

            };
            thread.start();
        }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void showAlert() {
        final android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                    finish();
                    }
                });
        dialog.show();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(AddAddressActivity.this, new
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
                        Toast.makeText(context, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                        update.setVisibility(View.VISIBLE);

                        if (!isLocationEnabled()) {
                            showAlert();
//                update.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show();
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


}

package com.pet.webservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pet.mypet.appointment.Cliniclist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Utility {
    public static final int RequestPermissionCode = 1;

    public static String getCurrent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    public static String readResponce(InputStream stream, String stren) {
        StringBuffer builder = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, stren));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static boolean isLocationEnabled(final Context context) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


    }

    public static void showAlert(final Context context) {
        final android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) { ;
                    }
                });
        dialog.show();
    }

    public static boolean checkPermission(final Context context) {
        int result = ContextCompat.checkSelfPermission(context,
                ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;


    }

//    private void requestPermission(final Context context) {
//        ActivityCompat.requestPermissions(context, new
//                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
//    }


}

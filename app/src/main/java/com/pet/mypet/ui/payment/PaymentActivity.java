package com.pet.mypet.ui.payment;

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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.appointment.AddBookTimeslot;
import com.pet.mypet.appointment.AppointmentModel;
import com.pet.mypet.appointment.AppointmentModelInterface;
import com.pet.mypet.appointment.Appointment_Details;
import com.pet.mypet.appointment.AppointmentsAdapter;
import com.pet.mypet.appointment.Cliniclist;
import com.pet.mypet.appointment.DateTimeFormat;
import com.pet.mypet.ui.pets.PetViewModel;
import com.pet.payment.CheckoutUIActivity;
import com.pet.payment.CheckoutUIActivityPAyment;
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

public class PaymentActivity extends AppCompatActivity implements ResponceQueues, ClickAdapter,AppointmentModelInterface {

    private PetViewModel toolsViewModel;
    ImageView fab;
    Context context;
    ArrayList<PaymentModel> paymentModelArrayList;
    PaymentModel paymentModel;
    RecyclerView recyclerView;
    PaymentsAdapter paymentsAdapter;
    TextView text_title;
    FrameLayout coordinatorLayout;
    private String card_mode= "";

    private static final int GET_STORAGE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_list);

        context = PaymentActivity.this;

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
        paymentModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_PAYMENTLIST+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"GET",paramObject);

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

            if (url.contains(Cons.URL_PAYMENTLIST)) {
                paymentModelArrayList.clear();
                Log.e("restry", "try" + "\n" + responce);
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=0; i<jsonArray.length() ; i++) {
                    paymentModel = new PaymentModel();
                    paymentModel.setId(jsonArray.getJSONObject(i).getString("id"));
                    paymentModel.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
                    paymentModel.setAmount_due(jsonArray.getJSONObject(i).getString("amount_due"));
                    paymentModel.setReceiveTime(getDay(showdate(jsonArray.getJSONObject(i).getString("receiveTime").split("T")[0]))+" , "+getMonth(showdate(jsonArray.getJSONObject(i).getString("receiveTime").split("T")[0]))+getDate(showdate(jsonArray.getJSONObject(i).getString("receiveTime").split("T")[0])));
                    paymentModel.setConsultdate(getDay(showdate(jsonArray.getJSONObject(i).getString("consultdate").split("T")[0]))+" , "+getMonth(showdate(jsonArray.getJSONObject(i).getString("consultdate").split("T")[0]))+getDate(showdate(jsonArray.getJSONObject(i).getString("consultdate").split("T")[0])));
                    paymentModelArrayList.add(paymentModel);
                }
                paymentsAdapter = new PaymentsAdapter(context, paymentModelArrayList, this);
                recyclerView.setAdapter(paymentsAdapter);
            }

            else if (url.contains(Cons.URL_PAYMENTLISTCHECKOUTID)){
                try {
                    JSONObject jsonObject=new JSONObject(responce);
                    String id = jsonObject.getString("id");
                    String confirmationCode = jsonObject.getString("confirmationCode");
                    startActivityForResult(new Intent(context, CheckoutUIActivity.class).putExtra("amount","1.00")
                            .putExtra("confirmationCode",confirmationCode).putExtra("REQ_id",id)
                            .putExtra("cardtype", card_mode)
                            .putExtra("url",Cons.URL_PAYMENTSATTYS),10);
                }
                catch (Exception e){

                }

            }

        } catch (Exception e) {
            Log.e("restry", "catch" + e + "");


        }
    }

    private void showAlert(final String amount,final String id) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert")
                .setMessage("Billing amount "+amount+" SAR.Please pay to confirm your booking.")
                .setPositiveButton("Pay now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        selectcardmode(amount,id);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (resultCode ==RESULT_OK) {
                Toast.makeText(context,"Payment done successfully",Toast.LENGTH_LONG).show();
                finish();
            }
            if (resultCode ==RESULT_CANCELED) {
                Toast.makeText(context,"Payment failed!!",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void clickoncard(int position, String id) {

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
    ,String appvisitorcontact,String amount) {

        showAlert(amount,id);


    }

    public void imagearrow(View view) {
        finish();
    }

    private void selectcardmode(final String amount,final String id) {
        final CharSequence[] options = { "Debit card", "Credit card","Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Choose mode of payment!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Debit card"))
                {
                    card_mode = "DEBIT";
                    JSONObject paramObject = new JSONObject();

                    makeHttpCall(Cons.URL_PAYMENTLISTCHECKOUTID+id+"/"+card_mode,"GET",paramObject);
                }
                else if (options[item].equals("Credit card"))
                {
                    card_mode = "CREDIT";
                    JSONObject paramObject = new JSONObject();

                    makeHttpCall(Cons.URL_PAYMENTLISTCHECKOUTID+id+"/"+card_mode,"GET",paramObject);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

}
package com.pet.mypet.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.mypet.editappointment.EditAddAppointment;
import com.pet.mypet.editappointment.EditAddBookTimeslot;
import com.pet.mypet.editappointment.EditCliniclist;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Appointment_Details extends AppCompatActivity implements ResponceQueues, View.OnClickListener {

    Context context;
    String u_id="";
    EditText ed_appdatetime;
    EditText appclinicdetail;
    EditText ed_apppname;
    EditText ed_appvisitorcontact;
    TextView ed_appvisitoraddress;
    EditText ed_appamount;
    EditText ed_apppreason;
    TextView editreschedule;

    CircleImageView appimage;
    TextView appdcotorname;
    String appointment_reqno="";
    TextView edit;
    String cliniccode="";
    String doctorId="";
    String PatientNumber="";
    String ClientNumber="";

    int day,month,year;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail);

        context = Appointment_Details.this;

        appdcotorname= findViewById(R.id.appdcotorname);
        ed_appdatetime = findViewById(R.id.ed_appdatetime);
        appclinicdetail = findViewById(R.id.appclinicdetail);
        ed_apppname = findViewById(R.id.ed_apppname);
        ed_appvisitorcontact = findViewById(R.id.appvisitorcontact);
        ed_appvisitoraddress = findViewById(R.id.appvisitoraddress);
        ed_appamount = findViewById(R.id.ed_appamount);
        ed_apppreason = findViewById(R.id.ed_apppreason);
        appimage = findViewById(R.id.appimage);
        edit = findViewById(R.id.edit);
        editreschedule = findViewById(R.id.editreschedule);



        Intent intent =getIntent();
        appdcotorname.setText(intent.getStringExtra("appdoctorname"));
        ed_appdatetime.setText(intent.getStringExtra("appdatetime"));
        appclinicdetail.setText(intent.getStringExtra("appclinicdetail"));
        ed_apppname.setText(intent.getStringExtra("apppname"));
        editreschedule.setText(intent.getStringExtra("appmsgdisplay"));


//        ed_apppname.setText("Dog");
//        ed_appvisitorcontact.setText("8766266839");


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


        u_id= intent.getStringExtra("id");
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_GET_APPOINTMENTSDETAIL+ u_id,"GET",paramObject);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject paramObject = new JSONObject();
                try {
                    paramObject.put("RequestNo",appointment_reqno);
                    paramObject.put("patient_Number",PatientNumber);
                    paramObject.put("client_Number", PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""));
                    paramObject.put("visitorContactNo", ed_appvisitorcontact.getText().toString());
                }
                catch (Exception e){

                }
                makeHttpCall(Cons.URL_EDIT_APPOINTMENTSDETAIL+appointment_reqno,"POST",paramObject);



            }
        });
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

        if (extra_text.equals("GET")) {
            if (url.contains(Cons.URL_GET_APPOINTMENTSDETAIL)) {

                Log.e("appointment_reqno", responce);
                try {
                    JSONArray jsonArray = new JSONArray(responce);
                    String requestNo = jsonArray.getJSONObject(0).getString("requestNo");
                    String address1 = jsonArray.getJSONObject(0).getString("address1");
                    String address2 = jsonArray.getJSONObject(0).getString("state");
                    String address3 = jsonArray.getJSONObject(0).getString("postCode");
                    String amount = jsonArray.getJSONObject(0).getString("amount");
                    String reason = jsonArray.getJSONObject(0).getString("reason");
                    String visitorContactNo = jsonArray.getJSONObject(0).getString("visitorContactNo");
                    String clinic_Code = jsonArray.getJSONObject(0).getString("clinic_Code");
                    String doctor_Id = jsonArray.getJSONObject(0).getString("doctorId");
                    String Client_Number = jsonArray.getJSONObject(0).getString("client_Number");
                    String Patient_Number = jsonArray.getJSONObject(0).getString("patient_Number");

                    ed_appvisitoraddress.setText(address1 + "," + address2 + " " + address3);
                    ed_appamount.setText(amount);
                    ed_apppreason.setText(reason);
                    ed_appvisitorcontact.setText(visitorContactNo);

                    appointment_reqno = requestNo;
                    cliniccode = clinic_Code;
                    doctorId = doctor_Id;
                    ClientNumber = Client_Number;
                    PatientNumber = Patient_Number;
                } catch (Exception e) {

                }


            }
        }
        else {

         if (url.contains(Cons.URL_EDIT_APPOINTMENTSDETAIL)) {
                try {
//                    Toast.makeText(context, "Updated successfully", Toast.LENGTH_LONG).show();

                    JSONArray jsonArray=new JSONArray(responce);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if (jsonObject.has("confirmationcode")) {
                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }

            }
        }

        Log.e("PETDELTERESPONSE",responce);
        if (responce.equalsIgnoreCase("success")){
            Intent intent=new Intent();
            setResult(1, intent);
            finish();
        }

    }

    public void imagearrow(View view) {
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appvisitorcontact:
                Log.e("tabclick","true");

                ed_appvisitorcontact.setEnabled(true);
                ed_appvisitorcontact.setCursorVisible(true);
                break;

            case R.id.edit:

                Log.e("tabclick","true");

//                ed_appvisitorcontact.setEnabled(true);
                break;

//                case R.id.editreschedule:
//
//
//                Log.e("tabclick","true");
//
//                ed_appvisitorcontact.setEnabled(true);
//                break;
                case R.id.editaddress:
                Log.e("tabclick","true");

                ed_appvisitorcontact.setEnabled(true);
                break;
            case R.id.editcontact:
                Log.e("tabclick","true");

                ed_appvisitorcontact.setEnabled(true);
                break;
        }
    }


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month= c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,  datePickerListener, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return dialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(calendar.getTime());
            strDate= DateTimeFormat.formatToddmmyyyyDate(strDate);

            startActivityForResult(new Intent(context, EditAddBookTimeslot.class)
                            .putExtra("date",strDate)
                            .putExtra("appointment_reqno",appointment_reqno)
                    .putExtra("cliniccode",cliniccode)
                    .putExtra("doctorId",doctorId)
                    .putExtra("patient_Number",PatientNumber)
                    ,10);

//            if(month < 10){
//
//                month = "0" + month;
//            }
//            if(day < 10){
//
//                day  = "0" + day ;
///            }


//            edappointmentdate.setText(strDate);
        }
    };


    public void editdate(View view) {
//        showDialog(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (resultCode==RESULT_OK){
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
//                JSONObject paramObject = new JSONObject();
//                makeHttpCall(Cons.URL_GET_APPOINTMENTSDETAIL+u_id,"GET",paramObject);
            }
        }
    }

    public void changecontact(View view) {


        edit.setVisibility(View.VISIBLE);
        ed_appvisitorcontact.setEnabled(true);
        ed_appvisitorcontact.setCursorVisible(true);
        ed_appvisitorcontact.setEnabled(true);
        ed_appvisitorcontact.requestFocus();

    }
}

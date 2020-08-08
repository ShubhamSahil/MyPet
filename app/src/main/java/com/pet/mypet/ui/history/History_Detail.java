package com.pet.mypet.ui.history;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.mypet.appointment.DateTimeFormat;
import com.pet.mypet.editappointment.EditAddBookTimeslot;
import com.pet.preference.PrefrenceUtils;
import com.pet.web.WebViewActivity;
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

public class History_Detail extends AppCompatActivity implements ResponceQueues, View.OnClickListener {

    Context context;
    String u_id="";

    String PatientNumber="";
    String ConsultNumber="";

    int day,month,year;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        context = History_Detail.this;




        Intent intent =getIntent();
        PatientNumber = (intent.getStringExtra("PatientId"));
        ConsultNumber= (intent.getStringExtra("ConsultNumber"));

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("UserName", PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""));
            paramObject.put("PatientId", PatientNumber);
            paramObject.put("ConsultNumber", ConsultNumber);
            Log.e("TAGERROR", "startstrDate" + " " + paramObject.getString("UserName")+ " "+paramObject.getString("PatientId")
                    + paramObject.getString("ConsultNumber"));

        }
        catch (Exception e){

        }



        makeHttpCall(Cons.URL_HISTORYLISTDETAIL_ID+ PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""),"POST",paramObject);


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

            if (url.contains(Cons.URL_HISTORYLISTDETAIL_ID)) {

                Log.e("appointment_reqno", responce);
                try {
                    startActivity(new Intent(context, WebViewActivity.class).putExtra("url",responce));
                } catch (Exception e) {

                }


        }
        else {
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

//                ed_appvisitorcontact.setEnabled(true);
//                ed_appvisitorcontact.setCursorVisible(true);
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

//                ed_appvisitorcontact.setEnabled(true);
                break;
            case R.id.editcontact:
                Log.e("tabclick","true");

//                ed_appvisitorcontact.setEnabled(true);
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

//            startActivityForResult(new Intent(context, EditAddBookTimeslot.class)
//                            .putExtra("date",strDate)
//                            .putExtra("appointment_reqno",appointment_reqno)
//                    .putExtra("cliniccode",cliniccode)
//                    .putExtra("doctorId",doctorId)
//                    .putExtra("patient_Number",PatientNumber)
//                    ,10);

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
        showDialog(0);
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

//    public void changecontact(View view) {
//
//
//        edit.setVisibility(View.VISIBLE);
//        ed_appvisitorcontact.setEnabled(true);
//        ed_appvisitorcontact.setCursorVisible(true);
//        ed_appvisitorcontact.setEnabled(true);
//        ed_appvisitorcontact.requestFocus();
//
//    }
}

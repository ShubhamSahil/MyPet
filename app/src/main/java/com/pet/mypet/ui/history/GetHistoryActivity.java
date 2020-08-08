package com.pet.mypet.ui.history;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.appointment.AppointmentModelInterface;
import com.pet.mypet.appointment.DateModel;
import com.pet.mypet.appointment.DateSlotAdapter;
import com.pet.mypet.appointment.DateTimeFormat;
import com.pet.mypet.ui.pets.AddPetsActivity;
import com.pet.mypet.ui.pets.PetDetailsinterface;
import com.pet.mypet.ui.pets.PetViewModel;
import com.pet.mypet.ui.pets.Pet_Details;
import com.pet.mypet.ui.pets.PetsAdapter;
import com.pet.preference.PrefrenceUtils;
import com.pet.web.WebViewActivity;
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
import java.util.Locale;
import java.util.TimeZone;

public class GetHistoryActivity extends AppCompatActivity implements ResponceQueues, ClickAdapter, AppointmentModelInterface {
    int day,month,year;

    private PetViewModel toolsViewModel;
    ImageView fab;
    Context context;
    ArrayList<HistoryModel> historyModelArrayList;
    HistoryModel historyModel;
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    TextView text_title;
    LinearLayout coordinatorLayout;
    String startstrDate="";
    String endstrDate="";

    ImageView imgfromdate;
    ImageView imgenddate;
    TextView textfromdate;
    TextView textenddate;
    Boolean checktext=false;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);
        context = GetHistoryActivity.this;
//        getActivity().setTitle("Your title");

        fab = findViewById(R.id.fab);
        textfromdate = findViewById(R.id.textfromdate);
        imgfromdate = findViewById(R.id.imgfromdate);
        imgenddate = findViewById(R.id.imgenddate);
        textenddate = findViewById(R.id.textenddate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,AddPetsActivity.class),2);
            }
        });

        text_title = findViewById(R.id.text_title);
        recyclerView =findViewById(R.id.recyclerview);
        coordinatorLayout =findViewById(R.id.coordinatorLayout);
        historyModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        imgfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checktext = true;
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imgenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checktext = false;
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        System.out.println("current date: " + getDate(cal));
        startstrDate = DateTimeFormat.formatToyyyymmddDate(getDate(cal));
        textfromdate.setText( startstrDate);

        //adding days into Date in Java
        cal.add(Calendar.DATE, -90);
        System.out.println("date befor 60 days : " + getDate(cal));


        endstrDate = DateTimeFormat.formatToyyyymmddDate(getDate(cal));
        textenddate.setText(endstrDate);
//        getPrevious20days(startstrDate);




        callapi();

    }

    private void callapi() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("UserName", PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""));
            paramObject.put("StartDate", startstrDate);
            paramObject.put("EndDate", endstrDate);
            Log.e("TAGERROR", "startstrDate" + " " + paramObject.getString("UserName")+ " "+paramObject.getString("StartDate")
                    + paramObject.getString("EndDate"));

        }
        catch (Exception e){

        }

        makeHttpCall(Cons.URL_HISTORYLIST+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);
    }

    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        Log.e("TAGERROR", "des" + " " + url);
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("TAGERROR", "try "+url + "\n" + responce);

        try {

            if (url.contains(Cons.URL_HISTORYLIST)) {
                historyModelArrayList.clear();
                Log.e("TAGERROR", "try" + "\n" + responce);
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=jsonArray.length()-1; i>=0 ; i--) {
                    historyModel = new HistoryModel();
                    historyModel.setpName(jsonArray.getJSONObject(i).getString("pName"));
                    historyModel.setDebitamount(jsonArray.getJSONObject(i).getString("debit"));
//                    historyModel.setTrdate(jsonArray.getJSONObject(i).getString("trdate"));
                    historyModel.setTrdate(getDay(showdate(jsonArray.getJSONObject(i).getString("trdate".replace("T",""))))+" , "+getMonth(showdate(jsonArray.getJSONObject(i).getString("trdate".replace("T",""))))+getDate(showdate(jsonArray.getJSONObject(i).getString("trdate".replace("T","")))));
                    historyModel.setpNumber(jsonArray.getJSONObject(i).getString("pNumber"));
                    historyModel.setCnumber(jsonArray.getJSONObject(i).getString("cnumber"));

                    historyModelArrayList.add(historyModel);
                }
                historyAdapter = new HistoryAdapter(context, historyModelArrayList, this);
                recyclerView.setAdapter(historyAdapter);
            }


        } catch (Exception e) {
            Log.e("restry", "catch" + e + "");


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (resultCode==RESULT_OK){
                startActivityForResult(new Intent(context,AddPetsActivity.class),2);

            }
            else if (resultCode==RESULT_CANCELED) {

                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.GETPATIENT_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST", paramObject);
            }
        }
        else if (requestCode==3){
            if (resultCode == 1) {
                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.GETPATIENT_URL+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);
            }

        }
    }

    @Override
    public void clickoncard(int position, String id) {

    }


    @Override
    public void clickoncard(int position, String id, String appdoctorname, String appimageurl, String appdatetime, String appclinicdetail, String apppname
            ,String appvisitorcontact,String amount) {
        startActivity(new Intent(context, WebViewActivity.class).putExtra("PatientId",id).putExtra("ConsultNumber",amount));

//        showAlert(amount,id);


    }

    public void imagearrow(View view) {
        finish();
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

    public static String getDate(Calendar cal){
        return "" + cal.get(Calendar.YEAR) +"-" +
                (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
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


    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month= c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        switch(id){
            case 0:
                DatePickerDialog dialog = new DatePickerDialog(this,  datePickerListener, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
break;
            case 1:
                DatePickerDialog dialog1 = new DatePickerDialog(this,  datePickerListener1, year, month, day);
                dialog1.getDatePicker().setMinDate(System.currentTimeMillis());
break;
        }


        return null;
    }

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update();
        }
    };

    private void update() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (checktext) {
            textfromdate.setText(sdf.format(myCalendar.getTime()));
            startstrDate = sdf.format(myCalendar.getTime());
        }
        else{
            textenddate.setText(sdf.format(myCalendar.getTime()));
            startstrDate = sdf.format(myCalendar.getTime());

        }

        if (!textfromdate.getText().equals("")&&!textenddate.getText().equals("")){
            callapi();
        }
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

//            Calendar cal = Calendar.getInstance();
//            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//            System.out.println("current date: " + getDate(cal));
//            startstrDate = DateTimeFormat.formatToyyyymmddDate(getDate(cal));
//
//            //adding days into Date in Java
//            cal.add(Calendar.DATE, -90);
//            System.out.println("date befor 90 days : " + getDate(cal));


//            if(month < 10){
//
//                month = "0" + month;
//            }
//            if(day < 10){
//
//                day  = "0" + day ;
///            }


            textfromdate.setText(strDate);
            startstrDate  =strDate;


//            textenddate.setText(strDate);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
//            cal.add(Calendar.DATE, -90);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(calendar.getTime());
            strDate= DateTimeFormat.formatToddmmyyyyDate(strDate);

//            Calendar cal = Calendar.getInstance();
//            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//            System.out.println("current date: " + getDate(cal));
//            startstrDate = DateTimeFormat.formatToyyyymmddDate(getDate(cal));
//
//            //adding days into Date in Java
//            cal.add(Calendar.DATE, -90);
//            System.out.println("date befor 90 days : " + getDate(cal));


//            if(month < 10){
//
//                month = "0" + month;
//            }
//            if(day < 10){
//
//                day  = "0" + day ;
///            }


            textenddate.setText(strDate);
            endstrDate  =strDate;

        }
    };

}
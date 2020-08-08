package com.pet.mypet.appointment;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shubham on 30/06/18.
 */

public class DateTimeFormat {

    public static String formatstartDate(String date1){
        String outputDateStr="";
        Date date = null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputDateStr=date1;
        try {
            date = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date);
        }
        catch (Exception e){

        }


        return outputDateStr;
    }

    public static String formatstartDate1(String date1){
        String outputDateStr="";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM");
        String inputDateStr=date1;
        try {
            Date date = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date);
        }
        catch (Exception e){

        }


        return outputDateStr;
    }


    public static String formatToyyyymmddDate(String date1){
        String outputDateStr="";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputDateStr=date1;
        try {
            Date date = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date);
        }
        catch (Exception e){

        }


        return outputDateStr;
    }

    public static String formatToddmmyyyyDate(String date1){
        String outputDateStr="";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputDateStr=date1;
        try {
            Date date = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date);
        }
        catch (Exception e){

        }


        return outputDateStr;
    }


    public static String twentyfourto12hour(String time){
         String timemstr = "23:15";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            timemstr=new SimpleDateFormat("hh:mm a").format(dateObj)+"";
//            timemstr=new SimpleDateFormat("KK:mm aa").format(dateObj)+"";
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
            Log.e("timeRESPONSETIMESLOT",time+ " "+timemstr+ " "+new SimpleDateFormat("hh:mm a").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return timemstr;
    }

    public static String strdate(Date inputdate){
        Date date = inputdate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}

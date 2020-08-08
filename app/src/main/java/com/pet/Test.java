package com.pet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.IOUtils;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.provider.Connect;
import com.pet.mypet.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;



public class Test extends AppCompatActivity {
    private Calendar calendar;
    private Context context;
    String startPreviousWeekDate;
    String endPreviousWeekDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_week);

        context= Test.this;

        Set<String> paymentBrands = new LinkedHashSet<String>();

        paymentBrands.add("VISA");
        paymentBrands.add("MASTER");
        paymentBrands.add("AMEX");
        paymentBrands.add("MADA");
        paymentBrands.add("DIRECTDEBIT_SEPA");

        CheckoutSettings checkoutSettings = new CheckoutSettings("3D7616E9E629D66C80A7EB09D86726DC.uat01-vm-tx02", paymentBrands, Connect.ProviderMode.TEST);

// Set shopper result URL
        checkoutSettings.setShopperResultUrl("companyname://result");
        Intent intent = checkoutSettings.createCheckoutActivityIntent(this);

        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);


//        JuspayBrowserFragment.openJuspayConnection(context);
//
//        Bundle juspayBundle = new Bundle();
//
//// Base Parameters
//
//        juspayBundle.putString(PaymentConstants.MERCHANT_ID, "REQ11111");
//        juspayBundle.putString(PaymentConstants.CLIENT_ID, "8ac7a4c971e574c30171e91f934d07ef");
//        juspayBundle.putString(PaymentConstants.ORDER_ID, "2");
//        juspayBundle.putString(PaymentConstants.AMOUNT, "1");
//        juspayBundle.putString(PaymentConstants.CUSTOMER_ID, "98716789");
//        juspayBundle.putString(PaymentConstants.CLIENT_EMAIL, "salman@evcsaudi.com");
//        juspayBundle.putString(PaymentConstants.CLIENT_MOBILE_NO, "8766266811");
//        juspayBundle.putString(PaymentConstants.ENV, PaymentConstants.ENVIRONMENT.PRODUCTION);
//
//// Service Parameters for in.juspay.ec
//
//        juspayBundle.putString(PaymentConstants.SERVICE, "in.juspay.godel");
////        juspayBundle.putStringArrayList(PaymentConstants.END_URLS,"");
//        juspayBundle.putString(PaymentConstants.URL, "https://test.oppwa.com/v1/paymentWidgets.js?checkoutId=2D75D026A8593241236D953FF561E6BD.uat01-vm-tx01\"");
//        Intent juspayIntent = new Intent(this, PaymentActivity.class);
//        juspayIntent.putExtras(juspayBundle);
//        startActivityForResult(juspayIntent, 1);


//        Set<String> paymentBrands = new LinkedHashSet<String>();
//
//        paymentBrands.add("VISA");
//        paymentBrands.add("MASTER");
//        paymentBrands.add("DIRECTDEBIT_SEPA");
//        PaymentActivity.preFetch(Test.this, "8ac7a4c971e574c30171e91f934d07ef");
//        CheckoutSettings checkoutSettings = new CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.TEST);

// Set shopper result URL
//        checkoutSettings.setShopperResultUrl("companyname://result");

//        Bundle juspayBundle = new Bundle();

// Base Parameters

//        juspayBundle.putString(PaymentConstants.MERCHANT_ID, getJuspayMerchantID());
//        juspayBundle.putString(PaymentConstants.CLIENT_ID, getJuspayClientId());
//        juspayBundle.putString(PaymentConstants.ORDER_ID, getOrderId());
//        juspayBundle.putString(PaymentConstants.AMOUNT, getOrderAmount());
//        juspayBundle.putString(PaymentConstants.CUSTOMER_ID, getCustomerId());
//        juspayBundle.putString(PaymentConstants.CLIENT_EMAIL, getCustomerEmail());
//        juspayBundle.putString(PaymentConstants.CLIENT_MOBILE_NO, getCustomerMobile());
//        juspayBundle.putString(PaymentConstants.ENV, PaymentConstants.ENVIRONMENT.PRODUCTION);
//
// Service Parameters for in.juspay.ec
//
//        juspayBundle.putString(PaymentConstants.SERVICE, "in.juspay.ec");
//        juspayBundle.putString(PaymentConstants.CLIENT_AUTH_TOKEN , getJuspayClientAuthToken());
//        juspayBundle.putStringArrayList(PaymentConstants.END_URLS, getEndUrls());
//        juspayBundle.putString(PaymentConstants.PAYLOAD, juspayPayload.toString());
//


//        SimpleDateFormat sdf =new SimpleDateFormat();
//
        Calendar c = Calendar.getInstance();
//        String d ="2020-03-30";
        c.setTime(parseDateToddMMyyyy("2020-04-01")); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


Log.e("day",dayOfWeek+"");


//        Calendar c = Calendar.getInstance();
//        c.setTime(yourdate); // yourdate is an object of type Date
//
//        int dayOfWeek = d;
        getCurrentWeekDate(dayOfWeek);
//        Log.e("Previous: " , getPreviousWeek()+"");

    }

//     private String request() throws IOException {
//         String data="entityId=8a8294174d0595bb014d05d82e5b01d2";
//         URL url = new URL("https://test.oppwa.com/v1/checkouts/{id}/payment?"+ data);
//
//         HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//         conn.setRequestMethod("POST");
//         conn.setRequestProperty("Authorization", "Bearer OGE4Mjk0MTc0ZDA1OTViYjAxNGQwNWQ4MjllNzAxZDF8OVRuSlBjMm45aA==");
//         conn.setDoInput(true);
//         conn.setDoOutput(true);
//
//         String data = ""
//                 + "entityId=8ac7a4c971e574c30171e91f934d07ef"
//                 + "&amount=92.00"
//                 + "&currency=EUR"
//                 + "&paymentType=DB";
//
//         DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//         wr.writeBytes(data);
//         wr.flush();
//         wr.close();
//         int responseCode = conn.getResponseCode();
//         InputStream is;
//
//         if (responseCode >= 400) is = conn.getErrorStream();
//         else is = conn.getInputStream();
//
//         return IOUtils.toString(is);
//     }

// }

    public Date parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String[] getCurrentWeek() {
        this.calendar = Calendar.getInstance();
        this.calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getNextWeek();
    }

    public String[] getNextWeek() {
        DateFormat format = new SimpleDateFormat("M-dd");
        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(this.calendar.getTime());
            this.calendar.add(Calendar.DATE, 1);
            Log.e("Currentnew : " ,days[i] );

        }

        return days;
    }

    public String[] getPreviousWeek() {
        this.calendar.add(Calendar.DATE, -14);
        return getNextWeek();
    }


    public void getCurrentWeekDate(int week) {
        Calendar c = GregorianCalendar.getInstance();
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate;
        String endDate;
        startDate = df.format(c.getTime());

        // previous week calculation
        Calendar previousWeekCalendar = c;
        previousWeekCalendar.add( Calendar.DAY_OF_YEAR, -7);
        startPreviousWeekDate = df.format(previousWeekCalendar.getTime());
        previousWeekCalendar.add(Calendar.DATE, 6);
        endPreviousWeekDate = df.format(previousWeekCalendar.getTime());

        c.add(Calendar.DATE, 6);
        //for previous week
        //c.add(Calendar.DAY_OF_WEEK, -1);
        //for next week
        c.add(Calendar.DAY_OF_WEEK, week);
        endDate = df.format(c.getTime());


        // Do next week calculation same as previous week. Just check what is the value of c before starting the calculation
        System.out.println("Start Date = " + startDate);
        System.out.println("End Date = " + endDate);
        System.out.println("End Date = " + endDate);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("STRINGRESULT",data+" "+data.getStringExtra("companyname://result"));
    }
}

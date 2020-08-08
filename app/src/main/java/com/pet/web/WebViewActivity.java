package com.pet.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.pet.mypet.R;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONObject;

import java.util.HashMap;

public class WebViewActivity extends AppCompatActivity implements ResponceQueues {
    String data = "";
    WebView view;
    String web_id="";
    Context context;
    HashMap<String,String> hashMap=new HashMap<>();
    private ProgressDialog prg;
    String checout_id="";
    String PatientNumber="";
    String ConsultNumber="";
    RelativeLayout relative;
    RelativeLayout toolbar;
    ImageView image_backarrow;
    String REQ_id="";
    private  int counter=0;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);


        context = WebViewActivity.this;
        view  =findViewById(R.id.webView);
        relative  =findViewById(R.id.relative);
        toolbar  =findViewById(R.id.toolbar);
        image_backarrow  =findViewById(R.id.image_backarrow);

        Intent intent =getIntent();
        PatientNumber = (intent.getStringExtra("PatientId"));
        ConsultNumber= (intent.getStringExtra("ConsultNumber"));
        relative.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
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

        image_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        makeHttpCall(Cons.URL_HISTORYLISTDETAIL_ID+ PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""),"POST",paramObject);


//        checout_id=getIntent().getStringExtra("url");
        Log.e("checout_idresultpayment", checout_id);

//        openCheckoutUI(checout_id);



//        data = "<h4><font face=\"Times New Roman\">i am here</font></h4><p><img src=\"http://35.173.187.82/aplis/public/storage/images/aplis-images-2019-12-30%2023:59:35-5e0a420f14dcd.jpeg\"><font face=\"Times New Roman\"><br></font></p><p><u><i style=\"background-color: rgb(255, 0, 128);\"><font color=\"#ffffff\">can you see image</font></i></u></p>";



//        loadwebdata("https://test.oppwa.com/v1/checkouts/"+6BC99B9AC16189EF2D0F89FA28989715.uat01-vm-tx03+"/redirect");


    }

    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }

//    private void openCheckoutUI(String checkoutId) {
//        CheckoutSettings checkoutSettings = createCheckoutSettings(checkoutId, getString(R.string.checkout_ui_callback_scheme));
//
//        /* Set componentName if you want to receive callbacks from the checkout */
//        ComponentName componentName = new ComponentName(
//                getPackageName(), CheckoutBroadcastReceiver.class.getName());
//
//        /* Set up the Intent and start the checkout activity. */
//        Intent intent = checkoutSettings.createCheckoutActivityIntent(this, componentName);
//
//        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);
//    }

    private void loadwebdata(final String url){

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDisplayZoomControls(true);
        view.getSettings().setSupportZoom(true);
        view.getSettings().setBuiltInZoomControls(true);

        getWebview(url);


        Log.e("clickver","one"+url);


    }


    public void getWebview(String myurl)
    {





        view.setWebViewClient(new WebViewClient()
        {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                prg.show();



                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                prg.dismiss();

                super.onPageFinished(view, url);
//                view.loadUrl(url);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {



                super.onPageStarted(view, url, favicon);
            }





        });
        prg = ProgressDialog.show(WebViewActivity.this, "Please wait", "Processing...", true);
        prg.setCancelable(true);
        view.loadUrl(myurl);



    }



    @Override
    protected void onResume() {
        super.onResume();


    }



    public void onback(View view) {
        finish();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("clickverresponce","one"+responce);

        loadwebdata(responce.replace("\"", ""));

    }


//    class MyWebViewClient extends WebViewClient {
//        @Override
//        // show the web page in webview but not in web browser
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            //1 option
//            // getApplicationContext().startActivity(
//            // new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            // 2 option
//            view.loadUrl(url);
//            return true;
//        }
//    }


}

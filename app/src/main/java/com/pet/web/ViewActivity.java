package com.pet.web;

import android.app.ProgressDialog;
import android.content.ComponentName;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.pet.mypet.R;
import com.pet.payment.receiver.CheckoutBroadcastReceiver;
import com.pet.webservice.Cons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewActivity extends AppCompatActivity  {
    String data = "";
    WebView view;
    TextView text_detail;
    String web_id="";
    Context context;
    HashMap<String,String> hashMap=new HashMap<>();
    private ProgressDialog prg;
    String checout_id="";
    String REQ_id="";
    private  int counter=0;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        view  =findViewById(R.id.webView);
        text_detail  =findViewById(R.id.text_detail);
        context =ViewActivity.this;
        text_detail.setText("About Us");
        loadwebdata(Cons.URL_ABOUTUS);

        Log.e("checout_idresultpayment", checout_id);

//        openCheckoutUI(checout_id);



//        data = "<h4><font face=\"Times New Roman\">i am here</font></h4><p><img src=\"http://35.173.187.82/aplis/public/storage/images/aplis-images-2019-12-30%2023:59:35-5e0a420f14dcd.jpeg\"><font face=\"Times New Roman\"><br></font></p><p><u><i style=\"background-color: rgb(255, 0, 128);\"><font color=\"#ffffff\">can you see image</font></i></u></p>";

//        final ZoomLinearLayout zoomLinearLayout = (ZoomLinearLayout) findViewById(R.id.zoom_linear_layout);
//        zoomLinearLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                zoomLinearLayout.init(ViewActivity.this);
//                return false;
//            }
//        });

//        loadwebdata("https://test.oppwa.com/v1/checkouts/"+6BC99B9AC16189EF2D0F89FA28989715.uat01-vm-tx03+"/redirect");


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
        view.getSettings().setAppCacheEnabled(true);
        view.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getPath());
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

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
        prg = ProgressDialog.show(ViewActivity.this, "Please wait", "Processing...", true);
        prg.setCancelable(true);
        view.loadUrl(myurl);



    }



    @Override
    protected void onResume() {
        super.onResume();


    }

    public void ontopic(View view) {
        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }



    public void back(View view) {
        finish();
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

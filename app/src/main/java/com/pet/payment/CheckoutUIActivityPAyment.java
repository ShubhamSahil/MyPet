package com.pet.payment;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.dialog.GooglePayHelper;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSkipCVVMode;
import com.oppwa.mobile.connect.provider.Connect;
import com.pet.mypet.R;
import com.pet.payment.common.Constants;
import com.pet.payment.receiver.CheckoutBroadcastReceiver;
import com.pet.web.ZoomLinearLayout;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONObject;


public class CheckoutUIActivityPAyment extends AppCompatActivity implements ResponceQueues {
    Context context;
    WebView view;
    String checout_id="";
    String REQ_id="";
    private ProgressDialog prg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_layout);
        context= CheckoutUIActivityPAyment.this;
        view  =findViewById(R.id.webView);
        String amount = Constants.Config.AMOUNT + " " + Constants.Config.CURRENCY;
//        Button btn_checkput=findViewById(R.id.button_proceed_to_checkout);
//        ((TextView) findViewById(R.id.amount_text_view)).setText(amount);

        checout_id=getIntent().getStringExtra("confirmationCode");
        REQ_id=getIntent().getStringExtra("REQ_id");



        Log.e("checout_idresultpayment", checout_id);

        openCheckoutUI(checout_id);

//        btn_checkput.performClick();
//        btn_checkput .setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                requestCheckoutId(getString(R.string.checkout_ui_callback_scheme));
//            }
//        });
    }


    private void openCheckoutUI(String checkoutId) {
//        loadwebdata("https://test.oppwa.com/v1/checkouts/"+checout_id+"/redirect");
        CheckoutSettings checkoutSettings = createCheckoutSettings(checkoutId, getString(R.string.checkout_ui_callback_scheme));

        /* Set componentName if you want to receive callbacks from the checkout */
        ComponentName componentName = new ComponentName(
                getPackageName(), CheckoutBroadcastReceiver.class.getName());

        /* Set up the Intent and start the checkout activity. */
        Intent intent = checkoutSettings.createCheckoutActivityIntent(this, componentName);

        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultpayment", requestCode + " result" + resultCode+ " "+REQ_id );
        if (requestCode == CheckoutActivity.REQUEST_CODE_CHECKOUT) {
            switch (resultCode) {
                case CheckoutActivity.RESULT_OK:
                    JSONObject paramObject=new JSONObject();
                    makeHttpCall(Cons.URL_PAYMENTSATTYS+REQ_id,"GET",paramObject);


                    break;
            }
        }
    }

    private void makeHttpCall(String url, String posttype, JSONObject paramObject) {
        Log.e("resultpaymentTAGERROR", "des" + " " + url);
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    protected CheckoutSettings createCheckoutSettings(String checkoutId, String callbackScheme) {
        return new CheckoutSettings(checkoutId, Constants.Config.PAYMENT_BRANDS,
                Connect.ProviderMode.LIVE)
                .setSkipCVVMode(CheckoutSkipCVVMode.FOR_STORED_CARDS)
                .setShopperResultUrl(callbackScheme + "://callback")
                .setGooglePayPaymentDataRequest(getGooglePayRequest());
    }

    private PaymentDataRequest getGooglePayRequest() {
        return GooglePayHelper.preparePaymentDataRequestBuilder(
                Constants.Config.AMOUNT,
                Constants.Config.CURRENCY,
                Constants.MERCHANT_ID,
                getPaymentMethodsForGooglePay(),
                getDefaultCardNetworksForGooglePay()
        ).build();
    }

    private Integer[] getPaymentMethodsForGooglePay() {
        return new Integer[] {
                WalletConstants.PAYMENT_METHOD_CARD,
                WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
        };
    }

    private Integer[] getDefaultCardNetworksForGooglePay() {
        return new Integer[] {
                WalletConstants.CARD_NETWORK_VISA,
                WalletConstants.CARD_NETWORK_MASTERCARD,
                WalletConstants.CARD_NETWORK_AMEX
        };
    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("resultpaymentTAGERROR", "desresponce" + " " + responce);

        if (url.contains(Cons.URL_PAYMENTSATTYS)){
            if (responce.equals("\"Success\"")){
//                Toast.makeText(context, "Thank you for booking the services,Service man will assign you shortly", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }


    private void loadwebdata(final String url){

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAppCacheEnabled(true);
        view.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getPath());
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        JavaScriptInterface JSAndroidBindingClass = new JavaScriptInterface();
//        webView.addJavascriptInterface( JSAndroidBindingClass, "locater");
//        view.loadUrl("(function(){\n" +
//                "\tvar wpwl = wpwl || {};\n" +
//                "\n" +
//                "\t// timestamp\n" +
//                "\twpwl.timestamp = new Date().toUTCString();\n" +
//                "\t\n" +
//                "\t// paymentWidgets.js request\n" +
//                "\twpwl.minified = true;\n" +
//                "\n" +
//                "\t// environment\n" +
//                "\twpwl.apiVersion = \"1\";\n" +
//                "\n" +
//                "\t// ndc\n" +
//                "\twpwl.ndc = \"2D75D026A8593241236D953FF561E6BD.uat01-vm-tx01\";\n" +
//                "\t\n" +
//                "\t// checkout\n" +
//                "\twpwl.checkout = {\"id\":\"2D75D026A8593241236D953FF561E6BD.uat01-vm-tx01\",\"amount\":\"1\",\"currency\":\"SAR\",\"paymentType\":\"DB\",\"config\":{\"brandConfig\":{\"brands\":[],\"overrideShopBrands\":false,\"activateBrands\":false},\"registrations\":[],\"detectIp\":true,\"redShieldDeviceIdInMsdkActive\":false,\"overrideHolder\":false,\"threeDSecureV2Config\":{\"visaThreeDV2\":false,\"masterThreeDV2\":false,\"amexThreeDV2\":false,\"bcmcThreeDV2\":false,\"dinersThreeDV2\":false,\"jcbThreeDV2\":false,\"cartebancaireThreeDV2\":false},\"environmentConfig\":{\"url\":\"https://test.oppwa.com\",\"defaultPaymentMode\":\"INTEGRATOR_TEST\",\"cacheVersion\":\"35ac875b573e723e8a530a3ec4598424\"},\"workflowSpecificConfig\":{\"aliRiskConfig\":{\"active\":false,\"aliRiskParameters\":{\"clientAppName\":\"PAY.ON\"}},\"kountConfig\":{\"active\":false,\"merchantId\":\"\",\"sessionId\":\"\",\"baseUrl\":\"https://tst.kaptcha.com/\"},\"iovationConfig\":{\"active\":false,\"msdkActive\":false},\"affirmConfig\":{\"active\":false,\"publicKey\":\"\"},\"forterConfig\":{\"active\":false,\"siteId\":\"\",\"sessionId\":\"2D75D026A8593241236D953FF561E6BD.uat01-vm-tx01\"}},\"klarnaMerchantIds\":[],\"paypalRestConfig\":{}},\"endpoint\":\"/payment\",\"resourcePath\":\"/v1/checkouts/2D75D026A8593241236D953FF561E6BD.uat01-vm-tx01/payment\"};\n" +
//                "\n" +
//                "\t// expose wpwl to global\n" +
//                "\twindow.wpwl = wpwl;\n" +
//                "\t\n" +
//                "\t// load static files async\n" +
//                "\t(function(d,t,w,s,j){\n" +
//                "\t\ts = d.createElement(t), j = d.getElementsByTagName(t)[0];\n" +
//                "\t\tvar e = w.checkout.config.environmentConfig;\n" +
//                "\t\ts.src = [e.url, \"/v\", w.apiVersion, \"/static/\", e.cacheVersion, \"/js/static\", w.minified ? \".min\" : \"\", \".js\"].join(\"\");\n" +
//                "\t\ts.async = true;\n" +
//                "\t\tj.parentNode.insertBefore(s, j);\n" +
//                "\t}(document, \"script\", wpwl));\n" +
//                "}());");


//        view.setWebViewClient(new WebViewClient());

        //URL gir "....."
        getWebview(url);
//        webView.setWebViewClient(new MyWebViewClient());


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
                view.loadUrl(url);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {



                super.onPageStarted(view, url, favicon);
            }





        });
        prg = ProgressDialog.show(CheckoutUIActivityPAyment.this, "Please wait", "Processing...", true);
        prg.setCancelable(true);
        view.loadUrl(myurl);



    }

}

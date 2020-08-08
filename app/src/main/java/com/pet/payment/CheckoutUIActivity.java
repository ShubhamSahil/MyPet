package com.pet.payment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.dialog.GooglePayHelper;
import com.oppwa.mobile.connect.checkout.meta.CheckoutCardBrandsDisplayMode;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;

import com.oppwa.mobile.connect.checkout.meta.CheckoutSkipCVVMode;
import com.oppwa.mobile.connect.provider.Connect;
import com.pet.mypet.R;
import com.pet.payment.common.Constants;
import com.pet.payment.receiver.CheckoutBroadcastReceiver;
import com.pet.web.ViewActivity;
import com.pet.web.ZoomLinearLayout;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Represents an activity for making payments via {@link CheckoutActivity}.
 */
public class CheckoutUIActivity extends AppCompatActivity implements ResponceQueues {
    static Context context;
    static WebView view;
    String checout_id="";
    String REQ_id="";
    String url="";
    String cardtype="";

    private static ProgressDialog prg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_layout);
        context=CheckoutUIActivity.this;
        view  =findViewById(R.id.webView);
        String amount = Constants.Config.AMOUNT + " " + Constants.Config.CURRENCY;
//        Button btn_checkput=findViewById(R.id.button_proceed_to_checkout);
//        ((TextView) findViewById(R.id.amount_text_view)).setText(amount);

        checout_id=getIntent().getStringExtra("confirmationCode");
        REQ_id=getIntent().getStringExtra("REQ_id");
        url=getIntent().getStringExtra("url");
        cardtype=getIntent().getStringExtra("cardtype");


        Log.e("checout_idresultpayment", checout_id+ " "+cardtype);

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
        CheckoutSettings checkoutSettings = createCheckoutSettings(checkoutId, getString(R.string.checkout_ui_callback_scheme),cardtype);
//checkoutSettings.set("Enter debit");
//x/checkoutSettings.setPaymentMethods()
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
                    makeHttpCall(url+REQ_id,"GET",paramObject);

                    break;

                    case CheckoutActivity.RESULT_CANCELED:
                        Intent intent=new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                        break;

                        case CheckoutActivity.RESULT_ERROR:
                            Intent intent1=new Intent();
                            setResult(RESULT_CANCELED, intent1);
                            finish();
                            break;

            }
        }
    }

    private void makeHttpCall(String url, String posttype, JSONObject paramObject) {
        Log.e("resultpaymentTAGERROR", "des" + " " + url);
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    protected CheckoutSettings createCheckoutSettings(String checkoutId, String callbackScheme,String cardtype) {
          final Set<String> PAYMENT_BRANDS= new LinkedHashSet<>();

        if (cardtype.equals("CREDIT")){

                PAYMENT_BRANDS.add("VISA");
                PAYMENT_BRANDS.add("MASTER");
                PAYMENT_BRANDS.add("AMEX");

        }
        else if (cardtype.equals("DEBIT")){
            PAYMENT_BRANDS.add("MADA");
//            PAYMENT_BRANDS.add("MASTER");
//            PAYMENT_BRANDS.add("AMEX");

        }
        Log.e("PAYMENT_BRANDS",PAYMENT_BRANDS+"");
        return new CheckoutSettings(checkoutId, PAYMENT_BRANDS,
                Connect.ProviderMode.LIVE)
                .setSkipCVVMode(CheckoutSkipCVVMode.FOR_STORED_CARDS)
                .setShopperResultUrl(callbackScheme + "://callback")
                .setGooglePayPaymentDataRequest(getGooglePayRequest()
                );
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

        if (url.contains(Cons.URL_VERIFYPAYMENTANDDONEAPPOINTMENT)){
            if (responce.equals("\"Success\"")){
//                Toast.makeText(context, "Thank you for booking the services,Service man will assign you shortly", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            else{
                Intent intent=new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }
        if (url.contains(Cons.URL_PAYMENTSATTYS)){
            if (responce.equals("\"Success\"")){
//                Toast.makeText(context, "Thank you for booking the services,Service man will assign you shortly", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            else{
                Intent intent=new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }


        }
    }


    public static void loadwebdata(final String url){

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAppCacheEnabled(true);
        view.getSettings().setAppCachePath(context.getCacheDir().getPath());
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getWebview(url);


        Log.e("clickver","one"+url);


    }


    public static void getWebview(String myurl)
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
        prg = ProgressDialog.show(context, "Please wait", "Processing...", true);
        prg.setCancelable(true);
        view.loadUrl(myurl);



    }

}

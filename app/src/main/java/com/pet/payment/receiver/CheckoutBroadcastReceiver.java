package com.pet.payment.receiver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.pet.payment.CheckoutUIActivity;
import com.pet.web.ViewActivity;


/**
 * Broadcast receiver to listen the intents from CheckoutActivity.
 */
public class CheckoutBroadcastReceiver extends BroadcastReceiver {
    private ProgressDialog prg;
private Context context;
    WebView webView;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String action = intent.getAction();

        if (CheckoutActivity.ACTION_ON_BEFORE_SUBMIT.equals(action)) {
            String paymentBrand = intent.getStringExtra(CheckoutActivity.EXTRA_PAYMENT_BRAND);
            String checkoutId = intent.getStringExtra(CheckoutActivity.EXTRA_CHECKOUT_ID);
            Log.e("payment_url",checkoutId);
//            CheckoutUIActivity.loadwebdata("https://test.oppwa.com/v1/checkouts/"+checkoutId+"/redirect");
//             webView=new WebView(context);



            ComponentName senderComponentName = intent.getParcelableExtra(
                    CheckoutActivity.EXTRA_SENDER_COMPONENT_NAME);

            /* This callback can be used to request a new checkout ID if selected payment brand requires
               some specific parameters or just send back the same checkout id to continue checkout process */
            intent = new Intent(CheckoutActivity.ACTION_ON_BEFORE_SUBMIT);
            intent.setComponent(senderComponentName);
            intent.setPackage(senderComponentName.getPackageName());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(CheckoutActivity.EXTRA_CHECKOUT_ID, checkoutId);

            /* Also it can be used to cancel the checkout process by sending
               the CheckoutActivity.EXTRA_CANCEL_CHECKOUT */
            intent.putExtra(CheckoutActivity.EXTRA_TRANSACTION_ABORTED, false);

            context.startActivity(intent);
        }
    }

//    private void loadwebdata(final String url){
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setAppCachePath(context.getCacheDir().getPath());
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//
//        getWebview(url);
//
//
//        Log.e("clickver","one"+url);
//
//
//    }


//    public void getWebview(String myurl)
//    {
//
//
//
//
//
//        webView.setWebViewClient(new WebViewClient()
//        {
//
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//
//                prg.show();
//
//
//
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//                prg.dismiss();
//
//                super.onPageFinished(view, url);
//                view.loadUrl(url);
//            }
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//
//
//                super.onPageStarted(view, url, favicon);
//            }
//
//
//
//
//
//        });
//        prg = ProgressDialog.show(context, "Please wait", "Processing...", true);
//        prg.setCancelable(true);
//        webView.loadUrl(myurl);
//
//
//
//    }
}

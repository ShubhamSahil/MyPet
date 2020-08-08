package com.pet.mypet.web;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.pet.mypet.R;

import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {
    String data = "";
    WebView view;
    String web_id="";
    Context context;
    HashMap<String,String> hashMap=new HashMap<>();
    private ProgressDialog prg;
    String url="";
    public ValueCallback<Uri[]> uploadMessage;
    private ValueCallback<Uri> mUploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private  int counter=0;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        view  =findViewById(R.id.webView);
        context =ViewActivity.this;
        url= getIntent().getStringExtra("url");
//        data = "<h4><font face=\"Times New Roman\">i am here</font></h4><p><img src=\"http://35.173.187.82/aplis/public/storage/images/aplis-images-2019-12-30%2023:59:35-5e0a420f14dcd.jpeg\"><font face=\"Times New Roman\"><br></font></p><p><u><i style=\"background-color: rgb(255, 0, 128);\"><font color=\"#ffffff\">can you see image</font></i></u></p>";




        loadwebdata(url);


    }



    private void loadwebdata(final String url){
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setAppCacheEnabled(true);
        view.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getPath());
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        view.setWebViewClient(new WebViewClient());
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setAllowContentAccess(true);
        view.getSettings().setAllowFileAccess(true);

        view.addJavascriptInterface(new Object()
        {
            public void performClick()
            {
                Toast.makeText(ViewActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();

                // Deal with a click on the OK button
            }
        }, "goBack");


//        view.getSettings().setSupportMultipleWindows(true);
//        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//        view.addJavascriptInterface(new WebAppInterface(this),"cancel");

        view.setWebChromeClient(new MyWebChromeClient());



//        view.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                //Required functionality here
//                return super.onJsAlert(view, url, message, result);
//            }
//
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                Log.e("file","chooser");
//                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
//            }
//        });


        //URL gir "....."
        getWebview(url);
//        webView.setWebViewClient(new MyWebViewClient());


        Log.e("clickver","one"+url);


    }

//    private class WebAppInterface {
//        Context mContext;
//        WebAppInterface(Context c) {
//            mContext = c;
//        }
//        @JavascriptInterface
//        public void showToast(String toast) {
//            final String string = toast;
//            Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
//
//        }
//    }
    public void getWebview(String myurl)
    {





        view.setWebViewClient(new WebViewClient()
        {



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e("clickver","one"+url);

                if (url.contains("mobhome.asp")) {
                    finish();
                    Log.e("clickver","one"+url);

                }
                try {
                    prg.show();
                }
                catch (Exception e){

                }
                view.loadUrl(url);


                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                try {
                    prg.dismiss();

                }
                catch (Exception e){

                }




                super.onPageFinished(view, url);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {



                super.onPageStarted(view, url, favicon);
            }




        });
        prg = ProgressDialog.show(ViewActivity.this, "Please wait", "Loading...", true);
        prg.setCancelable(true);
        view.loadUrl(myurl);



    }



    @Override
    protected void onResume() {
        super.onResume();


    }


    class MyWebChromeClient extends WebChromeClient {

//        @Override
//        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg)
//        {
//            WebView.HitTestResult result = view.getHitTestResult();
//            String data = result.getExtra();
//            Context context = view.getContext();
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
//            context.startActivity(browserIntent);
//            return true;
//        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //Required functionality here
            return super.onJsAlert(view, url, message, result);
        }


//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//            return  super.onJsConfirm(view, url, message, result);
//        }
//
//        @Override
//        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//            return  super.onJsPrompt(view, url, message,defaultValue, result);
//        }


        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(ViewActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != ViewActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(ViewActivity.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (view.canGoBack()) {
            view.goBack();
        } else {
            finish();
        }
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

package com.pet.mypet.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.pet.mypet.R;
import com.pet.mypet.appointment.AddAppointment;
import com.pet.mypet.appointment.GetAppointmentActivity;
import com.pet.mypet.appointment.Cliniclist;
import com.pet.mypet.ui.address.AddressListActivity;
import com.pet.mypet.ui.history.GetHistoryActivity;
import com.pet.mypet.ui.offer.GetOfferActivity;
import com.pet.mypet.ui.payment.PaymentActivity;
import com.pet.mypet.ui.pets.GetPetActivity;
import com.pet.preference.PrefrenceUtils;
import com.pet.web.ViewActivity;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements ResponceQueues, ClickAdapter {

    private HomeViewModel homeViewModel;
    Context context;
    ViewPager pageView;
    Handler ha = new Handler();
    Service servicemodel;
    private RecyclerView recycler_view;
    ArrayList<Service> productDetailthumbProductsArrayList;
    GridModelAdapter gridModelAdapter;
    PagerAdapter adapter;
    HashMap<String, String> hashMap = new HashMap<>();
    int currentindex = 0;
    ArrayList<ServiceForRecyclerView> serviceForRecyclerViewArrayList;
    ServiceForRecyclerView serviceForRecyclerView;
    String[] iname= {"Appointment","Pets","About Elites","Contact details","History","Offers"};
    int[] imgname= {R.drawable.appointment,R.drawable.pet_image,
           R.drawable.appnewicons,R.drawable.address, R.drawable.history,R.drawable.offer};

    ImageView image_chat;
    ImageView image_mail;
    ImageView image_taxi;
    private static final int GET_STORAGE_REQUEST_CODE = 1;

    TextView text_home;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();
        pageView = (ViewPager) root.findViewById(R.id.kk_pager);
        recycler_view = (RecyclerView) root.findViewById(R.id.recycler_view);
        image_chat =  root.findViewById(R.id.image_chat);
        image_mail =  root.findViewById(R.id.image_mail);
        image_taxi =  root.findViewById(R.id.image_taxi);
        productDetailthumbProductsArrayList = new ArrayList<>();
        serviceForRecyclerViewArrayList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, GridLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(new GridLayoutManager(context, 3));;
        recycler_view.setItemAnimator(new DefaultItemAnimator());



        image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://wa.me/966564669046?text=Hi";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        image_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showhelpmethod("support@evcsaudi.com");
            }
        });image_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getActivity());
            }
        });
        for (int i=0;i<6;i++){
            serviceForRecyclerView = new ServiceForRecyclerView();
            serviceForRecyclerView.setName(iname[i]);
            serviceForRecyclerView.setImage(imgname[i]);
            serviceForRecyclerViewArrayList.add(serviceForRecyclerView);
            gridModelAdapter = new GridModelAdapter(serviceForRecyclerViewArrayList, context,this);

//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(gridModelAdapter);

        }
        pageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ha.removeCallbacksAndMessages(null);
                return false;
            }
        });

//        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });


JSONObject paramObject=new JSONObject();
        makeHttpCall(Cons.OFFER_URL,"GET",paramObject);


//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClick2();
//            }
//        });

        return root;
    }

    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }

    public void onClick2() {
//        GetOfferActivity fragment2 = new GetOfferActivity();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.nav_host_fragment, fragment2);
//        fragmentTransaction.commit();
    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {
if (url.contains(Cons.URL_PAYMENTLIST)){
    try{
        JSONArray jsonArray = new JSONArray(responce);
        if (jsonArray.length()>0){
            showPaymentAlert();
        }
    }
    catch (Exception e){

    }
}
else if (url.contains(Cons.OFFER_URL)) {
    try {
        JSONArray jsonArray = new JSONArray(responce);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String offer_Heading = jsonObject.getString("offer_Heading");
            String offer_Description = jsonObject.getString("offer_Description");
            String offer_Start = jsonObject.getString("offer_Start");
            String offer_End = jsonObject.getString("offer_End");
            String offer_Img = jsonObject.getString("offer_Img");
            servicemodel = new Service();
            servicemodel.setId(id);
            servicemodel.setName(offer_Heading);
            servicemodel.setDes(offer_Description);
            servicemodel.setStart_date(offer_Start);
            servicemodel.setEnd_date(offer_End);
            servicemodel.setImage(offer_Img);
            productDetailthumbProductsArrayList.add(servicemodel);

        }

        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.URL_PAYMENTLIST + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "GET", paramObject);

    } catch (Exception e) {
//            ((MainActivity)getActivity()).call();
//            startActivity(new Intent(context, LoginActivity.class));

    }
}
        Log.e("RESPONSEOFFER", responce);


        try {


            adapter = new PagerAdapter(getFragmentManager());
            pageView.setAdapter(adapter);
            pageView.setCurrentItem(0);

            autoslidepager(pageView.getCurrentItem(), productDetailthumbProductsArrayList.size());
        } catch (Exception e1) {

        }

    }

    @Override
    public void clickoncard(int position, String id) {
        switch (position){


            case 0:
                startActivity(new Intent(context,GetAppointmentActivity.class));

                break;


            case 1:
                Log.e("restrypetfragment","done");
                startActivity(new Intent(context,GetPetActivity.class));


                break;

            case 2:

                startActivity(new Intent(context, ViewActivity.class));



                break;

            case 3:
                startActivity(new Intent(context, AddressListActivity.class));


                break;

            case 4:
                startActivity(new Intent(context, GetHistoryActivity.class));

                break;

            case 5:
                startActivity(new Intent(context, GetOfferActivity.class));

                break;

            case 6:
                break;

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager appointmentfragmentManager = getFragmentManager();
        FragmentTransaction appointmentfragmentTransaction = appointmentfragmentManager.beginTransaction();
        appointmentfragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        appointmentfragmentTransaction.addToBackStack("");
        appointmentfragmentTransaction.commit();
    }


    private class PagerAdapter extends FragmentPagerAdapter {

            public PagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                String name_value = productDetailthumbProductsArrayList.get(position).getImage();
                try {
                    currentindex = productDetailthumbProductsArrayList.indexOf(name_value);
                    Log.e("pro_Id", "index:" + currentindex + "");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("pro_Id", e + "");

                }
                    return PagerFragment1.newInstance(name_value, context);

            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
            }

            @Override
            public int getCount() {
                return productDetailthumbProductsArrayList.size();
            }


        }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }



    private void requestPermission(String getAccounts, int getEmailRequestCode, Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, getAccounts)) {
//            Toast.makeText(Mobile.this, "Calling is Active", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{getAccounts}, getEmailRequestCode);
        }
    }

    private boolean checkPermission(String getAccounts, Context applicationContext) {
        int result = ContextCompat.checkSelfPermission(applicationContext, getAccounts);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==200){

        }
        else{
            requestPermission( Manifest.permission.ACCESS_FINE_LOCATION, GET_STORAGE_REQUEST_CODE, getActivity());
        }
    }


        public void autoslidepager ( int currentItem, final int NUM_PAGES){
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        //call function
                        if (pageView.getCurrentItem() == NUM_PAGES - 1) {
                            pageView.setCurrentItem(0, true);
                            currentindex = 0;
                        } else {
                            Log.e("c_index", currentindex + "");
                            pageView.setCurrentItem(currentindex, true);
                            currentindex = currentindex + 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ha.postDelayed(this, 2000);
                }
            }, 2000);
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//
//
//            }
//        };

//        timer = new Timer(); // This will create a new Thread
//        timer .schedule(new TimerTask() { // task to be scheduled
//
//            @Override
//            public void run() {
//
////                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
        }

    private void showhelpmethod(String email_id) {
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email_id });

//        intent.putExtra(Intent.EXTRA_TEXT, "Mobile:"+ ""+PrefrenceUtils.readString(context,PrefrenceUtils.PREF_LASTNAME,""));
//        intent.putExtra(Intent.EXTRA_TEXT, "Email Id:"+ ""+PrefrenceUtils.readString(context,PrefrenceUtils.PREF_LASTNAME,""));
        startActivity(intent);
    }


    private void showPaymentAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert")
                .setMessage("Please pay due amount.!!")
                .setPositiveButton("Pay now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        startActivity(new Intent(context, PaymentActivity.class));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }


    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_screen);


        Button btn_book = (Button) dialog.findViewById(R.id.btn_book);
        Button btn_later = (Button) dialog.findViewById(R.id.btn_later);
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(context, AddAppointment.class)
                                .putExtra("clinic_id", "2")
                                .putExtra("clinic_type","1"),
                        2);
//                startActivity(new Intent(context,Cliniclist.class));

            }
        });

        dialog.show();

    }

}

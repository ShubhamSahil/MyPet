package com.pet;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.pet.mypet.R;
import com.pet.mypet.ui.home.HomeFragment;


public class MainNavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView allsong, albums, fav;
    DrawerLayout drawer_layout;
    Boolean isVisibleFragment=false;
    Boolean mSlideState=false;
    ImageView imageview_slider;
    TextView text_darkmode;
    ImageView imageview_back;
    ImageView imageView_search;
    Context context;
    public Boolean isVisible=false;
    ImageView imageview_settings;
    private GoogleApiClient client;
    FrameLayout framgment_container;
    String c_theme="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
//        getSupportActionBar().se                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              tIcon(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activitynavigation);
        context=MainNavigationActivity.this;





    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setFragment(int position) {

        switch (position) {
            case 0:
                replaceFragment(new HomeFragment());
                break;

        }
    }



        public void replaceFragment(Fragment fragment) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame, fragment);

            if(isVisibleFragment) {
                ft.addToBackStack("root_fragment");
            }
            isVisibleFragment=true;

            ft.commit();
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                setFragment(0);
                break;

//                case R.id.home:
//                setFragment(0);
//                break;


            default:
                break;
        }
    }


}

package com.pet.mypet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pet.mypet.account.LoginActivity;
import com.pet.mypet.appointment.GetAppointmentActivity;
import com.pet.mypet.ui.address.AddressListActivity;
import com.pet.mypet.ui.pets.GetPetActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        drawer = findViewById(R.id.drawer_layout);
    }

public  void call(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finishAffinity();
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:

                //do somthing
                break;
                case R.id.nav_menu_appointment:
                    startActivity(new Intent(context, GetAppointmentActivity.class));

                    break;
            case R.id.nav_menu_pet:
                startActivity(new Intent(context, GetPetActivity.class));

                break;

            case R.id.nav_menu_address:
                startActivity(new Intent(context, AddressListActivity.class));

                break;

                case R.id.nav_menu_logout:
                startActivity(new Intent(context, LoginActivity.class));
                finishAffinity();
                break;
            }

        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;    }
}

package com.pet.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pet.mypet.R;
import com.pet.mypet.account.LoginActivity;
import com.pet.mypet.appointment.GetAppointmentActivity;
import com.pet.mypet.ui.address.AddressListActivity;
import com.pet.mypet.ui.history.GetHistoryActivity;
import com.pet.mypet.ui.history.History_Detail;
import com.pet.mypet.ui.home.HomeFragment;
import com.pet.mypet.ui.offer.GetOfferActivity;
import com.pet.mypet.ui.payment.PaymentActivity;
import com.pet.mypet.ui.pets.GetPetActivity;
import com.pet.web.ViewActivity;

public class MainNaviagtionActivity extends AppCompatActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitynewmain);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        context = MainNaviagtionActivity.this;
        setupToolbar();

        DataModel[] drawerItem = new DataModel[9];

        drawerItem[0] = new DataModel(R.drawable.home, "Home");
        drawerItem[1] = new DataModel(R.drawable.home, "About Elite");
        drawerItem[2] = new DataModel(R.drawable.appointments, "Appointment");
        drawerItem[3] = new DataModel(R.drawable.address, "Address");
        drawerItem[4] = new DataModel(R.drawable.pet_image, "Pet");
        drawerItem[5] = new DataModel(R.drawable.payment, "Payment");
        drawerItem[6] = new DataModel(R.drawable.offer, "Offer");
        drawerItem[7] = new DataModel(R.drawable.history, "History");
        drawerItem[8] = new DataModel(R.drawable.logout, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        selectItem(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;

                case 1:
                    mDrawerLayout.closeDrawer(mDrawerList);
                    startActivity(new Intent(context, ViewActivity.class));
                break;
            case 2:
                mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, GetAppointmentActivity.class));

//                fragment = new FixturesFragment();
                break;
            case 3:
                mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, AddressListActivity.class));

//                fragment = new TableFragment();
                break;
                case 4:
                    mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, GetPetActivity.class));

//                fragment = new TableFragment();
                break;
                case 5:
                    mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, PaymentActivity.class));

//                fragment = new TableFragment();
                break;
                case 6:
                    mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, GetOfferActivity.class));

//                fragment = new TableFragment();
                break;
            case 7:
                mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, GetHistoryActivity.class));
                break;
                case 8:
                    mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(context, LoginActivity.class));
                finishAffinity();

//                fragment = new TableFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
}

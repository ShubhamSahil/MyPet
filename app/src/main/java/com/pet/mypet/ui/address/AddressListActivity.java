package com.pet.mypet.ui.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pet.mypet.R;
import com.pet.mypet.ui.home.HomeFragment;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;
import com.pet.webservice.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddressListActivity extends AppCompatActivity implements ResponceQueues {

    ImageView fab;
    Context context;

    EditText ed_addhouse;
    EditText ed_addcolony;
    EditText ed_addlandmark;
    EditText ed_addstate;
    EditText ed_addpostcode;
    EditText ed_addcontact;
    EditText ed_addemail;

    TextView editaddress;
    TextView address;
    LinearLayout linear_showaddress;
    Button addaddre;
    LinearLayout top_layer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_detail);

        context = AddressListActivity.this;


        ed_addhouse =findViewById(R.id.ed_addhouse);
        ed_addcolony =findViewById(R.id.ed_addcolony);
        ed_addlandmark = findViewById(R.id.ed_addlandmark);
        ed_addstate = findViewById(R.id.ed_addstate);
        ed_addpostcode = findViewById(R.id.ed_addpostcode);
        ed_addcontact = findViewById(R.id.ed_addcontact);
        ed_addemail = findViewById(R.id.ed_addemail);

        editaddress = findViewById(R.id.editaddress);
        address = findViewById(R.id.addaddress);
        linear_showaddress = findViewById(R.id.linear_showaddress);

        if (PrefrenceUtils.readString(context,PrefrenceUtils.PREF_HOUSE,"").equalsIgnoreCase("")){
//            top_layer.setVisibility(View.GONE);
//            ed_phname.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_HOUSE,""));
//            ed_pcname.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_COLONY,""));
//            ed_landmark.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_LANDMARK,""));
//            ed_state.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_STATE,""));
//            edpincode.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_POSTCODE,""));
//            edmobile.setText( PrefrenceUtils.readString(context,PrefrenceUtils.PREF_MOBILE,""));
        }



        getAddress();


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isLocationEnabled(context)) {
                    Utility.showAlert(context);
                }
                else {
                    startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra("task", 1), 2);
                }

            }
        });
        editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isLocationEnabled(context)) {
                    Utility.showAlert(context);
                }
                else {
                    startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra("task", 1), 2);
                }
//                saveaddress();
            }
        });

    }

    private void getAddress() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETADDRESS_URL+PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), "GET", paramObject);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
    if (resultCode==1){
    getAddress();

}
        }
    }



    private void makeHttpCall(String url, String posttype, JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }


    @Override
    public void responceQue(String responce, String url, String extra_text) {

        Log.e("HELLOTAG_RESPONSE","trybree +"+responce);

        if (url.contains(Cons.SETADDRESS_URL)) {

            try {
                if (responce.equalsIgnoreCase("\"Sucess\"")){

                    Log.e("TAG_ADDRESS_GET", PrefrenceUtils.readString(context,PrefrenceUtils.PREF_HOUSE,"")+" \n"+
                            PrefrenceUtils.readString(context,PrefrenceUtils.PREF_COLONY,"")+" \n"+
                            PrefrenceUtils.readString(context,PrefrenceUtils.PREF_LANDMARK,"")+" \n"+
                            PrefrenceUtils.readString(context,PrefrenceUtils.PREF_STATE,"")+" \n"+
                            PrefrenceUtils.readString(context,PrefrenceUtils.PREF_POSTCODE,"")+" \n"+
                            PrefrenceUtils.readString(context,PrefrenceUtils.PREF_MOBILE,"")+" \n");

                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_HOUSE,ed_addhouse.getText().toString());
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_COLONY,ed_addcolony.getText().toString());
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_LANDMARK,ed_addlandmark.getText().toString());
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_STATE,ed_addstate.getText().toString());
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_POSTCODE,ed_addpostcode.getText().toString());
                    PrefrenceUtils.writeString(context,PrefrenceUtils.PREF_MOBILE,ed_addcontact.getText().toString().replaceFirst("^0+(?!$)",""));

                    Toast.makeText(context,"Address update successfully",Toast.LENGTH_LONG).show();

                  finish();


                }

                else{
                    Toast.makeText(context,"Server error"+" "+responce,Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

            }
        }

        else if (url.contains(Cons.GETADDRESS_URL)){
            try{
                JSONArray jsonArray=new JSONArray(responce);
                String mobile =jsonArray.getJSONObject(0).getString("mobile");
                String address1 =jsonArray.getJSONObject(0).getString("address1");
                String address2 =jsonArray.getJSONObject(0).getString("address2");
                String address3 =jsonArray.getJSONObject(0).getString("address3");
                String state =jsonArray.getJSONObject(0).getString("state");
                String postcode =jsonArray.getJSONObject(0).getString("postcode");
                String emailId =jsonArray.getJSONObject(0).getString("emailId");


                ed_addhouse.setText(address1);
                ed_addcolony.setText(address2);
                ed_addlandmark.setText(address3);
                ed_addstate.setText(state);
                ed_addpostcode.setText(postcode);
                ed_addcontact.setText(mobile);
                ed_addemail.setText(emailId);

                if (address1.equalsIgnoreCase("null")||address1==null){
                    address.setVisibility(View.VISIBLE);
                    linear_showaddress.setVisibility(View.GONE);
//                    address.setText("+ Add a new address");
                    PrefrenceUtils.writeBoolean(context,PrefrenceUtils.PREF_ADDRESSESAVED,false);

                }
                else {
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_HOUSE, ed_addhouse.getText().toString());
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_COLONY, ed_addcolony.getText().toString());
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_LANDMARK, ed_addlandmark.getText().toString());
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_STATE, ed_addstate.getText().toString());
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_POSTCODE, ed_addpostcode.getText().toString());
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_MOBILE, ed_addcontact.getText().toString().replaceFirst("^0+(?!$)",""));
                    PrefrenceUtils.writeBoolean(context, PrefrenceUtils.PREF_ADDRESSESAVED, true);
                    PrefrenceUtils.writeString(context, PrefrenceUtils.PREF_EMAIL, emailId);

                    linear_showaddress.setVisibility(View.VISIBLE);
                }


            }
            catch (Exception e){



            }
        }
    }

    public void imagearrow(View view) {
        finish();
    }



//    private void saveaddress(){
//        try {
//            JSONObject paramObject = new JSONObject();
//            paramObject.put("Address1", ed_phname.getText().toString());
//            paramObject.put("Address2", ed_pcname.getText().toString());
//            paramObject.put("Address3", ed_landmark.getText().toString());
//            paramObject.put("State", ed_state.getText().toString());
//            paramObject.put("Postcode", edpincode.getText().toString());
//            paramObject.put("EmailId", "");
//            paramObject.put("Mobile", PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""));
//            paramObject.put("From", "ANDROID");
//            makeHttpCall(Cons.SETADDRESS_URL+PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), "POST", paramObject);
//        }
//        catch (Exception e){
//
//        }
//    }

    }


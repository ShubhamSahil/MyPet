package com.pet.mypet.ui.pets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.ClickAdapter;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetPetActivity extends AppCompatActivity implements ResponceQueues, ClickAdapter,PetDetailsinterface {

    private PetViewModel toolsViewModel;
    ImageView fab;
    Context context;
    ArrayList<PetlistModel> petlistModelArrayList;
    PetlistModel petlistModel;
    RecyclerView recyclerView;
    PetsAdapter favBookAdapter;
    TextView text_title;
    FrameLayout coordinatorLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_list);
        context = GetPetActivity.this;
//        getActivity().setTitle("Your title");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,AddPetsActivity.class),2);
            }
        });

        text_title = findViewById(R.id.text_title);
        recyclerView =findViewById(R.id.recyclerview);
        coordinatorLayout =findViewById(R.id.coordinatorLayout);
        petlistModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETPATIENT_URL+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);

    }

    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        Log.e("TAGERROR", "des" + " " + url);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();
        ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
        apiService.execute();
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {
        Log.e("restry", "try" + "\n" + responce);

        try {

            if (url.contains(Cons.GETPATIENT_URL)) {
                petlistModelArrayList.clear();
                Log.e("restry", "try" + "\n" + responce);
                JSONArray jsonArray = new JSONArray(responce);
                for (int i=jsonArray.length()-1; i>=0 ; i--) {
                    petlistModel = new PetlistModel();
                    petlistModel.setId(jsonArray.getJSONObject(i).getString("$id"));
                    petlistModel.setNumber(jsonArray.getJSONObject(i).getString("patient_Number"));
                    petlistModel.setName(jsonArray.getJSONObject(i).getString("patient_Name"));
                    petlistModel.setImage(jsonArray.getJSONObject(i).getString("patient_Image"));
                    petlistModel.setSpecies(jsonArray.getJSONObject(i).getString("patient_Species"));
                    petlistModel.setBreed(jsonArray.getJSONObject(i).getString("patient_Breed"));
                    petlistModel.setColor(jsonArray.getJSONObject(i).getString("patient_Colour"));
                    petlistModel.setSex(jsonArray.getJSONObject(i).getString("patient_Sex"));
                    petlistModel.setWeight(jsonArray.getJSONObject(i).getString("patient_Weight"));
                    petlistModel.setDob(jsonArray.getJSONObject(i).getString("patient_Date_of_Birth"));
                    petlistModel.setMicrochip(jsonArray.getJSONObject(i).getString("microchip"));
                    petlistModel.setEd_pdesexed(jsonArray.getJSONObject(i).getString("patient_Desexed"));
                    if (jsonArray.getJSONObject(i).getString("deactivated_Patient").equalsIgnoreCase("true")){
                        petlistModel.setIsActivated("#11000000");
                    }
                    else{
                        petlistModel.setIsActivated("#ffffff");
                    }
                    ;
                    petlistModelArrayList.add(petlistModel);
                }
                favBookAdapter = new PetsAdapter(context, petlistModelArrayList, this);
                recyclerView.setAdapter(favBookAdapter);
            }


        } catch (Exception e) {
            Log.e("restry", "catch" + e + "");


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (resultCode==RESULT_OK){
                startActivityForResult(new Intent(context,AddPetsActivity.class),2);

            }
            else if (resultCode==RESULT_CANCELED) {

                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.GETPATIENT_URL + PrefrenceUtils.readString(context, PrefrenceUtils.PREF_ID, ""), "POST", paramObject);
            }
        }
        else if (requestCode==3){
            if (resultCode == 1) {
                JSONObject paramObject = new JSONObject();
                makeHttpCall(Cons.GETPATIENT_URL+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);
            }

        }
    }

    @Override
    public void clickoncard(int position, String id) {

    }


    @Override
    public void clickoncard(int position, String id, String pimageurl, String pname, String pspecies, String pbreed, String pcolor, String psex, String pdob,String pweight, String pmicrochip,String ed_pdesexed) {
        Log.e("PETDELTERESPONSE","done"+id);
        startActivityForResult(new Intent(context,Pet_Details.class)
        .putExtra("id",id)
        .putExtra("pimageurl",pimageurl)
        .putExtra("pname",pname).putExtra("pspecies",pspecies).putExtra("pbreed",pbreed)
        .putExtra("pcolor",pcolor).putExtra("psex",psex).putExtra("pdob",pdob.split("T")[0]).putExtra("pweight",pweight)
        .putExtra("pmicrochip",pmicrochip).putExtra("pdesexed",ed_pdesexed),2);
    }

    public void imagearrow(View view) {
        finish();
    }
}
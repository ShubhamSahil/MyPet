package com.pet.mypet.ui.pets;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.pet.mypet.R;
import com.pet.mypet.appointment.AddAppointment;
import com.pet.mypet.appointment.Cliniclist;
import com.pet.mypet.appointment.DateTimeFormat;
import com.pet.mypet.compressor.ImageCompression;
import com.pet.mypet.compressor.ImageFilePath;
import com.pet.preference.PrefrenceUtils;
import com.pet.webservice.ApiService;
import com.pet.webservice.Cons;
import com.pet.webservice.ResponceQueues;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddPetsActivity extends AppCompatActivity implements ResponceQueues {
    int day,month,year;

    EditText eddobsignup;
    EditText ed_pname;
    EditText ed_pmicrochip;
    EditText ed_pweight;

    public String global_strdate = "";
    public String photoFileName = "";
    File mediaStorageDir;
    public final String APP_TAG = "MyCustomApp";
    private static final int GET_STORAGE_REQUEST_CODE = 1;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORphoto_school = 1034;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORGALLERY_school = 1033;
    File photo_school;
    String path_CAMERA = "";
    String path= "";
    Context context;
    MaterialBetterSpinner species_list;
    AutoCompleteTextView species_auto;
    MaterialBetterSpinner breed_list;
    AutoCompleteTextView breed_auto;
    MaterialBetterSpinner color_list;
    AutoCompleteTextView color_auto;
    MaterialBetterSpinner sex_list;

    ImageView ivPreview;
    ArrayList<PetSpeciesModel> petSpeciesModelArrayList;
    ArrayList<String> petSpeciesStringArrayList;
    PetSpeciesModel petSpeciesModel;
    String petspecies_value = "";

    ArrayList<PetBreedModel> breedModelArrayList;
    ArrayList<String> breedStringArrayList;
    PetBreedModel petBreedModel;
    String breed_value = "";

    ArrayList<String> colorStringArrayList;
    String color_value = "";

    ArrayList<String> sexStringArrayList;
    String sex_value = "";
    String imageString="";

    CheckBox cb_Desexed;
    String isselected=  "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pet);

        ed_pname = findViewById(R.id.ed_pname);
        ed_pmicrochip = findViewById(R.id.ed_pmicrochip);
        ed_pweight = findViewById(R.id.ed_pweight);
        eddobsignup = findViewById(R.id.eddobsignup);
        ivPreview = findViewById(R.id.ed_photographschoolcamera);

        species_list = findViewById(R.id.species_list);
        species_auto = findViewById(R.id.species_auto);
        breed_list = findViewById(R.id.breed_list);
        breed_auto = findViewById(R.id.breed_auto);
        color_list = findViewById(R.id.color_list);
        color_auto = findViewById(R.id.color_auto);
        sex_list = findViewById(R.id.sex_list);
        cb_Desexed = findViewById(R.id.cb_Desexed);


        mediaStorageDir  = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        context = AddPetsActivity.this;
        petSpeciesModelArrayList =new ArrayList<>();
        petSpeciesStringArrayList =new ArrayList<>();

        breedModelArrayList =new ArrayList<>();
        breedStringArrayList =new ArrayList<>();

        colorStringArrayList =new ArrayList<>();

        sexStringArrayList =new ArrayList<>();


//        petSpeciesStringArrayList.add("Select species");
//        breedStringArrayList.add("Select breed");
//        colorStringArrayList.add("Select color");
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {

        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,GET_STORAGE_REQUEST_CODE, AddPetsActivity.this);
        }

        cb_Desexed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isselected="true";
                }
                else{
                    isselected = "false";
                }
            }
        });

        JSONObject paramObject = new JSONObject();

        makeHttpCall(Cons.GETSPECIES_URL,"GET",paramObject);
//        getBreedData();
        getSexData();

        eddobsignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showDialog(0);
                return true;
            }


        });

    }

    private void getSpeciesData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, petSpeciesStringArrayList);
        species_auto.setThreshold(1);
        species_auto.setAdapter(adapter);

        species_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i>0) {
                    petspecies_value = species_auto.getText().toString();
                    JSONObject paramObject = new JSONObject();

                    makeHttpCall(Cons.GETBREED_URL + (petSpeciesModelArrayList.get(petSpeciesStringArrayList.indexOf(species_auto.getText().toString())).getId()), "POST", paramObject);
//                    makeHttpCall(Cons.GETBREED_URL + (petSpeciesModelArrayList.get(i-1).getId()), "POST", paramObject);

                    Log.e("TAG", i + " " + petSpeciesStringArrayList.size() + " "+petSpeciesStringArrayList.indexOf(species_auto.getText().toString()));
//                }

            }

        });
    }

    private void getBreedData() {
        Log.e("RESPONSETAG",+breedStringArrayList.size()+"");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, breedStringArrayList);
        breed_auto.setThreshold(1);
        breed_auto.setAdapter(adapter);

        breed_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    breed_value = breed_auto.getText().toString();
                    JSONObject paramObject = new JSONObject();

                    makeHttpCall(Cons.GETCOLORLIS_URL, "GET", paramObject);



            }

        });
    }
    private void getColorData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, colorStringArrayList);
        color_auto.setThreshold(1);
        color_auto.setAdapter(adapter);
//        color_list.setAdapter(adapter);

        color_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    color_value = color_auto.getText().toString();
                }
//                makeHttpCall(Cons.GETCOLORLIS_URL+(i+1),"POST");

                Log.e("TAG",i+" "+colorStringArrayList.size()+"");

            }

        });
    }


    private void getSexData() {
        sexStringArrayList.add("MALE");
        sexStringArrayList.add("FEMALE");
//        sexStringArrayList.add("NEUTRAL");
        sexStringArrayList.add("UNKNOWN");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, sexStringArrayList);
        sex_list.setAdapter(adapter);

        sex_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    sex_value = sexStringArrayList.get(i);

//                makeHttpCall(Cons.GETCOLORLIS_URL+(i+1),"POST");

                Log.e("TAG",i+" "+sexStringArrayList.size()+"");

            }

        });
    }


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month= c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,  datePickerListener, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        return dialog;
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(calendar.getTime());
            global_strdate = strDate;
            Log.e("RESPONSETIMESLOT",strDate);
            strDate= DateTimeFormat.formatToddmmyyyyDate(strDate);
//            datePickerListener.se(System.currentTimeMillis());
//            if(month < 10){
//
//                month = "0" + month;
//            }
//            if(day < 10){
//
//                day  = "0" + day ;
///            }


            eddobsignup.setText(strDate);
        }
    };

    public void petimage(View view) {
        selectImage();
    }


    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPetsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    onLaunchCamera(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORphoto_school,photo_school);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORGALLERY_school);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onLaunchCamera(int reqcode, File photoFile) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFileName  =System.currentTimeMillis()+"photo.jpg";
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(AddPetsActivity.this, "com.pet.mypet.provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, reqcode);
        }

    }

    public File getPhotoFileUri(String fileName) {

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            mediaStorageDir.mkdir();
            Log.d("APP_TAG", "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void requestPermission(String getAccounts,String getmore,String getLocation,int getEmailRequestCode, AddPetsActivity setting) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(setting, getAccounts)) {
//            Toast.makeText(Mobile.this, "Calling is Active", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(setting, new String[]{getAccounts,getmore,getLocation}, getEmailRequestCode);
        }
    }

    private boolean checkPermission(String getAccounts, Context applicationContext) {
        int result = ContextCompat.checkSelfPermission(applicationContext, getAccounts);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {



            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORphoto_school) {

                if (resultCode == RESULT_OK) {
//                path_School  = mediaStorageDir.getPath() + File.separator +photoFileName;
//                Log.e("FILEPATH","photo "+path_School);

                    path = ImageCompression.compressImage("", mediaStorageDir.getPath() + File.separator + photoFileName);
                    Log.e("FILEPATH", "photo " + path);
                    Glide.with(this).load(path).into(ivPreview);
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                     imageString = Base64.encodeToString(b, Base64.DEFAULT);
                    //          photo_school = saveFile(imageBitmap);
                    // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview

                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORGALLERY_school) {
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();


                    path = ImageFilePath.getPath(AddPetsActivity.this, uri);
                    path = ImageCompression.compressImage("", path);

//                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());


//                    path_GALLERY = mediaStorageDir.getPath() + File.separator + photoFileName;
                    Log.e("FILEPATH", "photo " + path);

                    Glide.with(this).load(path).into(ivPreview);
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    imageString = Base64.encodeToString(b, Base64.DEFAULT);
                    // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview

                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void responceQue(String responce, String url, String extra_text) {

        Log.e("HELLOTAG_RESPONSE1",url+"trybree + "+responce);

            if (url.contains(Cons.GETSPECIES_URL)) {
                try {
//                    petSpeciesModelArrayList.clear();
//                    petSpeciesStringArrayList.clear();
                    JSONArray jsonArray = new JSONArray(responce);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String speciesname = jsonObject.getString("species1");
                        String species_Code = jsonObject.getString("species_Code");
                        petSpeciesModel = new PetSpeciesModel();
                        petSpeciesModel.setName(speciesname);
                        petSpeciesModel.setId(species_Code);
                        petSpeciesModelArrayList.add(petSpeciesModel);
                        petSpeciesStringArrayList.add(speciesname);
                    }
                    getSpeciesData();

                } catch (Exception e) {
                }
            }
             if (url.contains(Cons.BASE_URL+"/api/Species/GetBreed")){
                Log.e("HELLOTAG_RESPONSE","trybree");

                try {
                     breedModelArrayList.clear();
                     breedStringArrayList.clear();
                     JSONArray jsonArray  =new JSONArray(responce);
                     for (int i=0;i<jsonArray.length();i++){
                         String breedname=jsonArray.getString(i);
                         Log.e("TAG_RESPONSEname",breedname);
                         petBreedModel =new PetBreedModel();
                         petBreedModel.setName(breedname);
                         breedModelArrayList.add(petBreedModel);
                         breedStringArrayList.add(breedname);
                     }
                     Log.e("HELLOTAG_RESPONSE","try");

                     getBreedData();
                 }
                 catch (Exception e) {
                     Log.e("HELLOTAG_RESPONSE",e+"");

                 }

            }

              if (url.contains(Cons.GETCOLORLIS_URL)){
                 try {
                     JSONArray jsonArray  =new JSONArray(responce);
                     for (int i=0;i<jsonArray.length();i++) {
                         String jsonObject=jsonArray.getJSONObject(i).getString("color");
                         colorStringArrayList.add(jsonObject);
                     }
                 }
                 catch (Exception e) {
                 }
                getColorData();
            }
               if (url.contains(Cons.SAVEPATIENT_URL)){
                   Log.e("TAG_RESPONSECons.SAVEPATIENT_URL",Cons.SAVEPATIENT_URL);
        if (responce.equalsIgnoreCase("\"Success\"")) {
         Toast.makeText(context, "Pet add successfully", Toast.LENGTH_LONG).show();
            selectoption();
        }
        else{
                Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();


}

              }

        Log.e("TAG_RESPONSE",url +"\n"+responce);
    }

        private void savepatients(){
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("patient_Name", ed_pname.getText().toString());
            paramObject.put("patient_Species", petspecies_value);
            paramObject.put("patient_Breed", breed_value);
            paramObject.put("patient_Colour", color_value);
            paramObject.put("patient_Sex", sex_value);
            paramObject.put("patient_Weight",  ed_pweight.getText().toString());
            paramObject.put("patient_Date_of_Birth", global_strdate);
            paramObject.put("patient_Desexed", isselected);
            paramObject.put("deactivated_Patient", false);
            paramObject.put("patient_Image", imageString);
            paramObject.put("microchip", ed_pmicrochip.getText().toString());
            paramObject.put("clinic_Code", 1);
            paramObject.put("from", "ANDROID");

//            ed_pweight.setText(imageString);
            Log.e("TAG_RESPONSE",paramObject.getString("patient_Image"));

            makeHttpCall(Cons.SAVEPATIENT_URL+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""), "POST", paramObject);
        }
        catch (Exception e){

        }
    }

    public void addbtn(View view) {
        if(!isFormValid()){
            Toast.makeText(context,"Please select all mandatory fields",Toast.LENGTH_LONG).show();
            return;
        }
        if (ed_pmicrochip.getText().toString().length()>0){
            if(!ismicrochipvalid()){
                Toast.makeText(context,"Enter a valid 15 digit microchip Id",Toast.LENGTH_LONG).show();
                return;
            }

        }
        savepatients();
    }

    private boolean isFormValid(){
        if((ed_pname.getText().toString().equalsIgnoreCase(""))){
            ed_pname.setError("Select pet name");
            return false;
        }
        if((petspecies_value.equalsIgnoreCase(""))){
            return false;
        }if((breed_value.equals(""))){
            return false;

        }if((sex_value.equals(""))){
            return false;

        }
    if((isselected.equals(""))){
            return false;

        }



        return true;
    }

    private boolean ismicrochipvalid(){
        if((ed_pmicrochip.getText().toString().length())<15){
            ed_pmicrochip.setError("Enter a valid 15 digit microchip Id");

            return false;

        }
        return true;
    }

    public void imagearrow(View view) {
        finish();
    }


    private void selectoption() {
        final CharSequence[] options = { "You want to take appointment", "Add new patient","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPetsActivity.this);
        builder.setTitle("Choose below one option!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("You want to take appointment"))
                {
                    finish();
                    startActivity(new Intent(context, Cliniclist.class));

                }
                else if (options[item].equals("Add new patient"))
                {
                    Intent intent=new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent=new Intent();
                    setResult(RESULT_CANCELED, intent);

                    finish();
                }
            }
        });
        builder.show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == RESULT_OK) {
//
//
//
//            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORphoto_school) {
//
//                if (resultCode == RESULT_OK) {
//                    ImageView ivPreview = findViewById(R.id.ed_photographschool);
////                path_School  = mediaStorageDir.getPath() + File.separator +photoFileName;
////                Log.e("FILEPATH","photo "+path_School);
//
//                    path_CAMERA = ImageCompression.compressImage("", mediaStorageDir.getPath() + File.separator + photoFileName);
//                    Log.e("FILEPATH", "photo " + path_School);
//                    Glide.with(this).load(mediaStorageDir.getPath() + File.separator + photoFileName).into(ivPreview); //
//                    //          photo_school = saveFile(imageBitmap);
//                    // by this point we have the camera photo on disk
////                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                    // RESIZE BITMAP, see section below
//                    // Load the taken image into a preview
//
//                } else { // Result was a failure
//                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORGALLERY_school) {
//                if (resultCode == RESULT_OK) {
//
//                    path_GALLERY = mediaStorageDir.getPath() + File.separator + photoFileName;
//                    Log.e("FILEPATH", "photo " + path_School_ground);
//
//                    ImageView ivPreview = findViewById(R.id.ed_photographschoolground);
//                    Glide.with(this).load(mediaStorageDir.getPath() + File.separator + photoFileName).into(ivPreview); //
//
//                    // by this point we have the camera photo on disk
////                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                    // RESIZE BITMAP, see section below
//                    // Load the taken image into a preview
//
//                } else { // Result was a failure
//                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }


}

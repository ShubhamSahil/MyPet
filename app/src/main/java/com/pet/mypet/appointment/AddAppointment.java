package com.pet.mypet.appointment;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
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
import com.pet.mypet.compressor.ImageCompression;
import com.pet.mypet.compressor.ImageFilePath;
import com.pet.mypet.ui.pets.PetBreedModel;
import com.pet.mypet.ui.pets.PetSpeciesModel;
import com.pet.mypet.ui.pets.PetlistModel;
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

public class AddAppointment extends AppCompatActivity implements ResponceQueues {
    int day,month,year;
    EditText edappointmentdate;
    EditText ed_vmobile;
    EditText ed_appointmentreason;

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

    MaterialBetterSpinner clinic_list;
    MaterialBetterSpinner doctor_list;
    MaterialBetterSpinner pet_list;

    ImageView ivPreview;

    ArrayList<GetDoctormodel> doctormodelArrayList;
    ArrayList<String> doctorstringarraylist;
    GetDoctormodel doctormodel;
    String getdoctor_value = "";

    ArrayList<GetClinicmodel> clinicmodelArrayList;
    ArrayList<String> clinicStringArrayList;
    GetClinicmodel clinirmodel;
    String getclinic_value ="";

    ArrayList<GetPetModel> petmodelArrayList;
    ArrayList<String> petStringArrayList;

    ArrayList<GetReasonModel> getreasonmodelArrayList;
    ArrayList<String> getreasonStringArrayList;
    GetPetModel petlistModel;
    GetReasonModel getReasonModel;
    String getpet_value ="";
    String clinic_type ="";
    public static String imageString="";
    Button add;
    AutoCompleteTextView autocomplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_appointment);

        ed_vmobile = findViewById(R.id.ed_vmobile);
        ed_appointmentreason = findViewById(R.id.ed_appointmentreason);
        edappointmentdate = findViewById(R.id.edappointmentdate);
        ivPreview = findViewById(R.id.ed_picturepet);

        clinic_list = findViewById(R.id.clinic_list);
        doctor_list = findViewById(R.id.doctor_list);
        pet_list = findViewById(R.id.pet_list);
        add = findViewById(R.id.add);
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);
        mediaStorageDir  = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        context = AddAppointment.this;

        doctormodelArrayList =new ArrayList<>();
        doctorstringarraylist =new ArrayList<>();

        clinicmodelArrayList =new ArrayList<>();
        clinicStringArrayList =new ArrayList<>();

        petmodelArrayList =new ArrayList<>();
        petStringArrayList =new ArrayList<>();

        getreasonmodelArrayList =new ArrayList<>();
        getreasonStringArrayList =new ArrayList<>();



        doctorstringarraylist.add("Choose Doctor");
        clinicStringArrayList.add("Select Clinic");
        petStringArrayList.add("Select pet");

        getclinic_value  =getIntent().getStringExtra("clinic_id");
        clinic_type  =getIntent().getStringExtra("clinic_type");

        Log.e("RESPONSETAG",getclinic_value+" "+clinic_type);

        ed_vmobile.setText(PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""));
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {

        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,GET_STORAGE_REQUEST_CODE, AddAppointment.this);
        }


//        getClinicList();
        getDoctorList();
        getPetList();
        getReasonList();

        edappointmentdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showDialog(0);
                return true;
            }

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFormValid()){
                    Toast.makeText(context,"Please select all fields",Toast.LENGTH_LONG).show();
                    return;
                }

                startActivityForResult(new Intent(context,AddBookTimeslot.class)
                        .putExtra("date",edappointmentdate.getText().toString())
                        .putExtra("Cliniccode",getclinic_value)
                        .putExtra("doctor_id",getdoctor_value)
                        .putExtra("visitorContactNo",ed_vmobile.getText().toString().replaceFirst("^0+(?!$)",""))
                        .putExtra("reason",autocomplete.getText().toString())
                        .putExtra("patient_Number",getpet_value.toString())
//                        .putExtra("imageString",imageString)
                        .putExtra("clinic_type",clinic_type),2);            }
        });

    }

    private void getPetList() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETPATIENT_URL+ PrefrenceUtils.readString(context,PrefrenceUtils.PREF_ID,""),"POST",paramObject);

    }

    private void getReasonList() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETREASON_URL,"GET",paramObject);

    }

    private void getDoctorList() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETDCOTOR_URLBYCLINIC+"Clinic_Code eq "+getclinic_value,"GET",paramObject);

    }

    private void getClinicList() {
        JSONObject paramObject = new JSONObject();
        makeHttpCall(Cons.GETCLINIC_URL,"GET",paramObject);
    }

    private void setClinicData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, clinicStringArrayList);
        clinic_list.setAdapter(adapter);

        clinic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    getclinic_value = clinicmodelArrayList.get(i-1).getClinic_Code();
                    Log.e("TAG", i + " " + clinicStringArrayList.size() + "");
                }

            }

        });
    }

    private void setDoctorData() {
        Log.e("RESPONSETAG",+doctorstringarraylist.size()+"");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, doctorstringarraylist);
        doctor_list.setAdapter(adapter);

        doctor_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    getdoctor_value = doctormodelArrayList.get(i - 1).getDoctor_code();
                    Log.e("DOCTORE_ID",getdoctor_value+"");
                }




            }

        });
    }
    private void setpetsData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, petStringArrayList);
        pet_list.setAdapter(adapter);

        pet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    getpet_value = petmodelArrayList.get(i-1).getNumber();
                }

                Log.e("TAG",i+" "+petStringArrayList.size()+"");

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
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
            strDate= DateTimeFormat.formatToddmmyyyyDate(strDate);
//            if(month < 10){
//
//                month = "0" + month;
//            }
//            if(day < 10){
//
//                day  = "0" + day ;
///            }


            edappointmentdate.setText(strDate);
        }
    };



    private void makeHttpCall(String url,String posttype,JSONObject paramObject) {
        try {

            ApiService apiService = new ApiService(context, this, url, posttype,paramObject);
            apiService.execute();
        } catch (Exception e) {

        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAppointment.this);
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
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

        Uri fileProvider = FileProvider.getUriForFile(AddAppointment.this, "com.pet.mypet.provider", photoFile);
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

    private void requestPermission(String getAccounts,String getmore,String getLocation,int getEmailRequestCode, AddAppointment setting) {
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
        Log.e("code_pro",requestCode+ " "+resultCode);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
           else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        } else if (resultCode == RESULT_OK) {


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
            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODEFORGALLERY_school) {
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();


                    path = ImageFilePath.getPath(AddAppointment.this, uri);
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

        Log.e("HELLOTAG_RESPONSE", "trybree +" + extra_text);

        if (url.contains(Cons.GETCLINIC_URL)) {
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String clinic_Code = jsonArray.getJSONObject(i).getString("clinic_Code");
                    String clinicName = jsonArray.getJSONObject(i).getString("clinicName");
                    String c_Address = jsonArray.getJSONObject(i).getString("c_Address");
                    String latitude = jsonArray.getJSONObject(i).getString("latitude");
                    String longitude = jsonArray.getJSONObject(i).getString("longitude");
                    String contact = jsonArray.getJSONObject(i).getString("contact");
                    String clinic_Pics = jsonArray.getJSONObject(i).getString("clinic_Pics");
                    clinirmodel = new GetClinicmodel();
                    clinirmodel.setClinic_Code(clinic_Code);
                    clinirmodel.setClinicName(clinicName);
                    clinirmodel.setC_Address(c_Address);
                    clinirmodel.setLatitude(latitude);
                    clinirmodel.setLongitude(longitude);
                    clinirmodel.setContact(contact);
                    clinirmodel.setClinic_Pics(clinic_Pics);
                    clinicStringArrayList.add(clinicName);
                    clinicmodelArrayList.add(clinirmodel);
                }

            } catch (Exception e) {
                Log.e("HELLOTAG_RESPONSEGETCLINIC_URL", e+"");

            }
            setClinicData();
        } else if (url.contains(Cons.GETDCOTOR_URLBYCLINIC)) {
            Log.e("HELLOTAG_RESPONSE", "trybree");

            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String doctorId = jsonArray.getJSONObject(i).getString("$id");
                    String doctorCode = jsonArray.getJSONObject(i).getString("doctorCode");
                    String clinic_Code = jsonArray.getJSONObject(i).getString("clinic_Code");
                    String doctorName = jsonArray.getJSONObject(i).getString("doctorName");
                    Log.e("TAG_RESPONSEdoctorCode", doctorCode);
                    doctormodel = new GetDoctormodel();
                    doctormodel.setDoctorId(doctorId);
                    doctormodel.setDoctor_code(doctorCode);
                    doctormodel.setClinic_Code(clinic_Code);
                    doctormodel.setDoctorName(doctorName);
                    doctorstringarraylist.add(doctorName);
                    doctormodelArrayList.add(doctormodel);

                }
                setDoctorData();
                Log.e("HELLOTAG_RESPONSE", "try");

            } catch (Exception e) {
                Log.e("HELLOTAG_RESPONSE", e + "");

            }

        } else if (url.contains(Cons.GETPATIENT_URL)) {
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    petlistModel = new GetPetModel();
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
                    petStringArrayList.add(jsonArray.getJSONObject(i).getString("patient_Name"));
                    petmodelArrayList.add(petlistModel);
                }
            } catch (Exception e) {
                Log.e("HELLOTAG_RESPONSEGETPATIENT_URL", e+"");

            }
            setpetsData();

            Log.e("TAG_RESPONSE", url + "\n" + responce);
        }

        else if (url.contains(Cons.GETREASON_URL)){
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    getReasonModel = new GetReasonModel();
                    getReasonModel.set$id(jsonArray.getJSONObject(i).getString("$id"));
                    getReasonModel.setId(jsonArray.getJSONObject(i).getString("id"));
                    getReasonModel.setReason(jsonArray.getJSONObject(i).getString("reason"));
                    getreasonmodelArrayList.add(getReasonModel);
                    getreasonStringArrayList.add(getReasonModel.getReason());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, getreasonStringArrayList);
                autocomplete.setThreshold(1);
                autocomplete.setAdapter(adapter);
                }
            catch (Exception e){

            }
            Log.e("TAG_RESPONSE", url + "\n" + responce);


//            ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                    (this,android.R.layout.select_dialog_item, arr);
//
//            autocomplete.setThreshold(1);
//            autocomplete.setAdapter(adapter);

//Log.e("REASON",)
        }
    }
        private void addAppointments(){
//        try {
//            JSONObject paramObject = new JSONObject();
//            paramObject.put("visitorContactNo", ed_pname.getText().toString());
//            paramObject.put("preferTime", petspecies_value);
//            paramObject.put("reason", breed_value);
//            paramObject.put("is_Active", color_value);
//            paramObject.put("msg_Reg",  ed_pweight.getText().toString());
//            paramObject.put("preferredDate", eddobsignup.getText().toString());
//            paramObject.put("msg_SameDay", true);
//            paramObject.put("client_Number", false);
//            paramObject.put("patient_Number", ed_pmicrochip.getText().toString());
//            paramObject.put("clinic_Code", 1);
//            paramObject.put("doctorId", 1);
//            paramObject.put("Address1", 1);
//            paramObject.put("State", 1);
//            paramObject.put("PostCode", 1);
//            paramObject.put("latitude", 1);
//            paramObject.put("longitude", 1);
//            paramObject.put("through", "ANDROID");
//
////            ed_pweight.setText(imageString);
//            Log.e("TAG_RESPONSE",paramObject.getString("patient_Image"));
//
//            makeHttpCall(Cons.SAVEAPPOINTMENT_URL, "POST", paramObject);
//        }
//        catch (Exception e){
//
//        }
    }


    public void addpicture(View view) {
        selectImage();
    }

//    public void addbtnappointment(View view) {
////        addAppointments();
//
//        if(!isFormValid()){
//            Toast.makeText(context,"Please select all fields",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        startActivityForResult(new Intent(context,AddBookTimeslot.class)
//        .putExtra("date",edappointmentdate.getText().toString())
//        .putExtra("Cliniccode",getclinic_value)
//        .putExtra("doctor_id",getdoctor_value)
//        .putExtra("visitorContactNo",ed_vmobile.getText().toString().replaceFirst("^0+(?!$)",""))
//        .putExtra("reason",ed_appointmentreason.getText().toString())
//        .putExtra("patient_Number",getpet_value.toString())
//        .putExtra("imageString",imageString)
//        .putExtra("clinic_type",clinic_type),2);
//    }

    public void imagearrow(View view) {
        finish();
    }


    private boolean isFormValid(){
        if((edappointmentdate.getText().toString().equalsIgnoreCase(""))){
            edappointmentdate.setError("Select date");
            return false;
        }
        if((getclinic_value.equalsIgnoreCase(""))){
//            getclinic_value.setError("Select Clinic");
            return false;
        }if((getdoctor_value.equals(""))){
//            getdoctor_value.setError("Select Doctor");
            return false;

        }if((ed_vmobile.getText().toString().equals(""))){
            ed_vmobile.setError("Please select mobile number");
            return false;
        }if((getpet_value.equals(""))){
//            getpet_value.setError("Please enter designation");
            return false;
        }if((autocomplete.getText().toString().equals(""))){
            autocomplete.setError("Please enter appointment reason");
            return false;
        }

        return true;
    }

    private boolean isValidName(String name){
        String regex = "[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789_]*";
//        String regex = "^[\\p{L} .'-]+$";
        return name.matches(regex);
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

package com.pet.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by User on 7/25/2017.
 */

public class PrefrenceUtils {
    private static PrefrenceUtils prefrence;
    private Context context;
    private SharedPreferences mPrefrence;
    private static String prefName = "_KONCEPT";

    private static int MODE = Context.MODE_PRIVATE;
    public static final String PREF_ID = "_id";
    public static final String PREF_TUTO = "done";
    public static final String PREF_FIRSTNAME = "_fname";
    public static final String PREF_LASTNAME = "_lname";
    public static final String PREF_NM = "_name";
    public static final String PREF_IMAGEPATH= "_image";
    public static final String PREF_IMAGENEWSPATH= "_imagepath";
    public static final String PREF_NEARBYEVENT_ID = "_nearbyeventid";
    public static final String PREF_DEVIC_TOKEN = "_devc";
    public static final String PREF_REGISTRED= "_registered";
    public static final String PREF_LOGINTYPE= "_logintype";
    public static final String PREF_REGISTREDTOKEN= "_registeredtoken";
    public static final String PREF_USERNAME= "_username";
    public static final String PREF_EMAIL= "_email";
    public static final String PREF_MOBILE= "_mobile";
    public static final String PREF_LAT= "_lat";
    public static final String PREF_LONG= "_long";
    public static final String PREF_ADDRESSESAVED= "_address";
    public static final String PREF_OTP= "_otp";
    public static final String PREF_NAME= "_namebooking";
    public static final String PREF_PHOME= "_phonebooking";
    public static final String PREF_HOUSE= "_house";
    public static final String PREF_COLONY= "_colony";
    public static final String PREF_LANDMARK= "_landmark";
    public static final String PREF_LOCALITY= "_localitymobile";
    public static final String PREF_CITYBOOKING= "_citymobile";
    public static final String PREF_STATEBOOKING= "_statemobile";
    public static final String PREF_ZIPBOOKING= "_zipmobile";
    public static final String PREF_SERVICE= "_service_idmobile";
    public static final String PREF_COUNTRY= "_country";
    public static final String PREF_STATE= "_state";
    public static final String PREF_POSTCODE= "_postcode";
    public static final String PREF_CITY= "_city";
    public static final String PREF_ADDRESS= "_address";
    public static final String PREF_MOBILEEDIT= "_mobileedit";
    public static final String PREF_INDUSTRY= "_indus";
    public static final String PREF_COMPANY= "_company";
    public static final String PREF_DESIG= "_designation";
    public static final String PREF_FACEBOOK_EMAIL= "_fbemail";
    public static final String PREF_FACEBOOK_LOGIN= "_fblogin";
    public static final String PREF_FACEBOOK_FNAME= "_fbfname";
    public static final String PREF_FACEBOOK_LNAME= "_fblname";
    public static final String PREF_GOOGLE_EMAIL= "_gemail";
    public static final String PREF_GOOGLE_FNAME= "_gfname";
    public static final String PREF_GOOGLE_LNAME= "_glname";
    public static final String PREF_GOOGLE_LOGIN= "_glogin";
    public static final String PREF_REG_TYPE= "_regtype";
    public static final String PREF_MOBILE_NUMBER= "_mob_num";
    public static final String PREF_ENTERED_EMAIlID= "_ent_email";
    public static final String PREF_SOCIAL_EMAIlID= "_soc_email";
    public static final String PREF_PASS_GENERAED_TIME= "_pass_generated_time";
    public static final String PREF_PASS_GENERAED_LAT= "_pass_generated_lat";
    public static final String PREF_PASS_GENERAED_LONG= "_pass_generated_long";
    public static final String PREF_PASS_IMAGE= "_pass_image";
    public static final String PREF_DEVICE_TOKEN= "_pref_device_token";
    public static final String PREF_MAX_ADULTSFOREVENTS= "_pref_adults_events";
    public static final String PREF_PASS_GENERATED= "_pass_generated";
    public static final String PREF_PASS_VALIDATED= "_pass_validated";
    public static final String PREF_PASS_GENERATED_NUMOFPERSON= "_pass_generatednum";
    public static final String PREF_COMAPNY_NAME= "_company_name";
    public static  int PREF_NOTIFIATION_COUNT= 1;



    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }
public static void deltedb(Context context, String key){
     getEditor(context).remove(key).commit();
}
    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(prefName, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

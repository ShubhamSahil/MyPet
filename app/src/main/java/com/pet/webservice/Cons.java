package com.pet.webservice;

public  class Cons {
    public static String BASE_URLOLD = "http://35.173.187.82/boost/public/api";
//    public static String BASE_URL  = "https://testapi.evcsaudia.com";
    public static String BASE_URL = "https://api.evcsaudia.com";
//    public static String BASE_URL  = "https://www.enlargemedia.in";
    public static String LOGIN_URL = BASE_URL+"/api/UserMasters/";
    public static String SIGNUP_URL = BASE_URL+"/api/Clients/";
    public static String RESET_URL = BASE_URL+"/api/UserMasters/ResetPass/";
    public static String VERIFIED_URL = BASE_URL+"/api/UserMasters/Verified/";
    public static String TOKEN_URL = BASE_URL+"/token";
    public static String OFFER_URL = BASE_URL+"/api/Offers";
    public static String GETSPECIES_URL = BASE_URL+"/api/Species";
    public static String GETBREED_URL = BASE_URL+"/api/Species/GetBreed/";
    public static String GETCOLORLIS_URL = BASE_URL+"/api/PatientColors";
    public static String SAVEPATIENT_URL = BASE_URL+"/api/Patients/Save/";
    public static String SAVEAPPOINTMENT_URL = BASE_URL+"/api/Appointments";
    public static String GETPATIENT_URL = BASE_URL+"/api/Patients/GetPatient/";
    public static String GETREASON_URL = BASE_URL+"/api/Clinics/GetAppReasonSelect/0";
    public static String DEACTIVATEPATIENT_URL = BASE_URL+"/api/Patients/Deactive/";
    public static String GETCLINIC_URL = BASE_URL+"/api/Clinics/GetClinicdistance";
    public static String GETCLINIC_URLOLD = BASE_URL+"/api/Clinics";
    public static String GETDCOTOR_URL = BASE_URL+"/api/Doctors";
    public static String GETDCOTOR_URLBYCLINIC = BASE_URL+"/api/Doctors?$select=DoctorCode,Clinic_Code,DoctorName&$filter=";
    public static String SETADDRESS_URL = BASE_URL+"/api/Clients/edit/";
    public static String GETADDRESS_URL = BASE_URL+"/api/Clients/GetClient/";
    public static String URL_GET_SPEICIFCTIMESLOTS = BASE_URL+"/api/Clinics/GetBookedSlot";
    public static String URL_GET_APPOINTMENTSLIST = BASE_URL+"/api/Appointments/GetAppointmentUser/";
    public static String URL_GET_APPOINTMENTSDETAIL= BASE_URL+"/api/Appointments/";
    public static String URL_EDIT_APPOINTMENTSDETAIL= BASE_URL+"/api/Appointments/Appup/";
    public static String URL_GETDISTANCEAMOUNT= BASE_URL+"/api/Clinics/GetDistanceAmount";
    public static String URL_VERIFYPAYMENTANDDONEAPPOINTMENT= BASE_URL+"/api/Appointments/PaymentStatus/";


    public static String URL_PAYMENTLIST= BASE_URL+"/api/Payments/GetPayment/";
    public static String URL_PAYMENTLISTCHECKOUTID= BASE_URL+"/api/Payments/GetCheckout/";
    public static String URL_PAYMENTSATTYS= BASE_URL+"/api/Payments/GetPaymentStatus/";

    public static String URL_HISTORYLIST= BASE_URL+"/api/Clinics/GetRecords/";
    public static String URL_HISTORYLISTDETAIL_ID= BASE_URL+"/api/Clinics/GetRecordHistory/";
    public static String URL_ABOUTUS= "https://evcsaudia.com/en/AboutUsApp.aspx";


    public static String SENDOTP_URL = "https://mshastra.com/sendurlcomma.aspx?user=20094556&pwd=riyadh@12345&senderid=EVC&mobileno=";
    public static String SENDOTPURLAPI = BASE_URL+"/api/UserMasters/SendOTP";

    public static String LEADGENERATE_URL = BASE_URL+"/create-publicity";
    public static String ACTIVITY_URL = BASE_URL+"/create-activity";
    public static String LISTOFPUBLICITY_URL = BASE_URL+"/get-assigned-publicities";

}

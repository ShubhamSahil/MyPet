package com.pet.mypet.appointment;

public class AppointmentModel {
    public String getReq_number() {
        return req_number;
    }

    public void setReq_number(String req_number) {
        this.req_number = req_number;
    }

    public String getVisitorContactNo() {
        return visitorContactNo;
    }

    public void setVisitorContactNo(String visitorContactNo) {
        this.visitorContactNo = visitorContactNo;
    }

    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getPreferTime() {
        return preferTime;
    }

    public void setPreferTime(String preferTime) {
        this.preferTime = preferTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPatient_Number() {
        return patient_Number;
    }

    public void setPatient_Number(String patient_Number) {
        this.patient_Number = patient_Number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPatient_Img() {
        return patient_Img;
    }

    public void setPatient_Img(String patient_Img) {
        this.patient_Img = patient_Img;
    }

    private String req_number,visitorContactNo,preferredDate,preferTime,reason,patient_Number,address1,state
            ,postCode,latitude,longitude,amount,patient_Img,id,clinic_name,patient_name,clinic_code,msg_disp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getClinic_code() {
        return clinic_code;
    }

    public void setClinic_code(String clinic_code) {
        this.clinic_code = clinic_code;
    }

    public String getMsg_disp() {
        return msg_disp;
    }

    public void setMsg_disp(String msg_disp) {
        this.msg_disp = msg_disp;
    }
}

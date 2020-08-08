package com.pet.mypet.appointment;

public class GetDoctormodel {
    private String doctor_code;
    private String doctorId;
    private String clinic_Code;
    private String doctorName;

    public String getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(String doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getClinic_Code() {
        return clinic_Code;
    }

    public void setClinic_Code(String clinic_Code) {
        this.clinic_Code = clinic_Code;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}

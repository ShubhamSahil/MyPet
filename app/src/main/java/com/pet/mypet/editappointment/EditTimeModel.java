package com.pet.mypet.editappointment;


import java.util.Date;

/**
 * Created by shubham on 05/12/18.
 */

public class EditTimeModel {
    private String strtime;
    private String id;
    private String timeslot;
    private Date date;



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }
}

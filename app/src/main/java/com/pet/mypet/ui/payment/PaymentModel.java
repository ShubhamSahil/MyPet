package com.pet.mypet.ui.payment;

public class PaymentModel {
    private String id,amount_due,remarks,receiveTime,consultdate;

    public String getConsultdate() {
        return consultdate;
    }

    public void setConsultdate(String consultdate) {
        this.consultdate = consultdate;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

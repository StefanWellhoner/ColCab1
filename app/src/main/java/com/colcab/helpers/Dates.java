package com.colcab.helpers;
//Ticket information for the notifications
public class Dates {
    private String customer;
    private String caseModel;
    private String documentID;
    private String scheduledDate;

    public Dates(){

    }

    public Dates(String customer, String caseModel, String documentID, String scheduledDate) {
        this.customer = customer;
        this.caseModel = caseModel;
        this.documentID = documentID;
        this.scheduledDate = scheduledDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCaseModel() {
        return caseModel;
    }

    public void setCaseModel(String caseModel) {
        this.caseModel = caseModel;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}

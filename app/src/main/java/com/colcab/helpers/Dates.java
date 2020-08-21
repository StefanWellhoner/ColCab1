package com.colcab.helpers;
//Ticket information for the notifications
public class Dates {
    private String customer;
    private String caseModel;
    private String documentID;
    private String dateScheduled;

    public Dates(String customer, String caseModel, String documentID, String dateScheduled) {
        this.customer = customer;
        this.caseModel = caseModel;
        this.documentID = documentID;
        this.dateScheduled = dateScheduled;
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

    public String getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(String dateScheduled) {
        this.dateScheduled = dateScheduled;
    }
}

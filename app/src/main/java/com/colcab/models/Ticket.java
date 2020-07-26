package com.colcab.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class Ticket {
    private String customer;
    private String customerPO;
    private String caseModel;
    private Map<String, String> requestedBy;
    private boolean scheduled;
    private String serialNumber;
    private String warranty;
    private String caseDescription;
    private Timestamp loggedDate;
    private String scheduledDate;
    private DocumentReference contractor;

    public Ticket() {

    }

    public Ticket(String customer, String customerPO, String caseModel, Map<String, String> requestedBy, boolean scheduled, String serialNumber, String warranty, String caseDescription, Timestamp loggedDate, String scheduledDate, DocumentReference contractor) {
        this.customer = customer;
        this.customerPO = customerPO;
        this.caseModel = caseModel;
        this.requestedBy = requestedBy;
        this.scheduled = scheduled;
        this.serialNumber = serialNumber;
        this.warranty = warranty;
        this.caseDescription = caseDescription;
        this.loggedDate = loggedDate;
        this.scheduledDate = scheduledDate;
        this.contractor = contractor;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public Timestamp getLoggedDate() {
        return loggedDate;
    }

    public String getCustomerPO() {
        return customerPO;
    }

    public String getCaseModel() {
        return caseModel;
    }

    public Map<String, String> getRequestedBy() {
        return requestedBy;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getWarranty() {
        return warranty;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public DocumentReference getContractor() {
        return contractor;
    }
}

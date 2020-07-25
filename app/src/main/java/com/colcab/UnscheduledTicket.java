package com.colcab;

import com.google.firebase.Timestamp;

import java.util.Map;

public class UnscheduledTicket {
    private String customer;
    private String customerPO;
    private String caseModel;
    private Map<String, String> requestedBy;
    private boolean scheduled;
    private String serialNumber;
    private String warranty;
    private String caseDescription;
    private Timestamp loggedDate;

    public UnscheduledTicket() {

    }

    public UnscheduledTicket(String customer, String customerPO, String caseModel, Map<String, String> requestedBy, boolean scheduled, String serialNumber, String warranty, String caseDescription, Timestamp loggedDate) {
        this.customer = customer;
        this.customerPO = customerPO;
        this.caseModel = caseModel;
        this.requestedBy = requestedBy;
        this.scheduled = scheduled;
        this.serialNumber = serialNumber;
        this.warranty = warranty;
        this.caseDescription = caseDescription;
        this.loggedDate = loggedDate;
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
}

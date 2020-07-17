package com.colcab;

import com.google.firebase.Timestamp;

public class UnscheduledTickets {
    private String customer;
    private String caseDescription;
    private Timestamp loggedDate;

    public UnscheduledTickets() {

    }

    public UnscheduledTickets(String customer, String caseDescription, Timestamp loggedDate) {
        this.customer = customer;
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
}

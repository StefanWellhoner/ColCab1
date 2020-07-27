package com.colcab.models;

import com.google.firebase.Timestamp;

public class ClosedTicket {

    private String caseDescription;
    private String customer;
    private String rootCause;
    private Timestamp loggedDate;
    private Timestamp closedDate;

    public ClosedTicket() {

    }

    public ClosedTicket(String caseDescription, String customer, String rootCause, Timestamp loggedDate, Timestamp closedDate) {
        this.caseDescription = caseDescription;
        this.customer = customer;
        this.rootCause = rootCause;
        this.loggedDate = loggedDate;
        this.closedDate = closedDate;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public String getCustomer() {
        return customer;
    }

    public String getRootCause() {
        return rootCause;
    }

    public Timestamp getLoggedDate() {
        return loggedDate;
    }

    public Timestamp getClosedDate() {
        return closedDate;
    }

}

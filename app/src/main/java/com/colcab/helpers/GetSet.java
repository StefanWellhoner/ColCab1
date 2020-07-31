package com.colcab.helpers;

import com.google.firebase.Timestamp;

public class GetSet {
    private String categoryType;
    private String categoryAmount;
    private String failureType;
    private String failureAmount;
    private String clientFeedback;
    private String rootCause;
    private String clientCSAT;
    private String caseDescription;
    private Timestamp loggedDate;
    private String customer;

    public GetSet(){
        //Empty need for firebase
    }

    public GetSet(String categoryType, String categoryAmount, String failureType, String failureAmount,
                  String clientFeedback, String rootCause, String clientCSAT, String caseDescription,
                  Timestamp loggedDate, String customer) {
        this.categoryType = categoryType;
        this.categoryAmount = categoryAmount;
        this.failureType = failureType;
        this.failureAmount = failureAmount;
        this.clientFeedback = clientFeedback;
        this.rootCause = rootCause;
        this.clientCSAT = clientCSAT;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public Timestamp getLoggedDate() {
        return loggedDate;
    }

    public void setLoggedDate(Timestamp loggedDate) {
        this.loggedDate = loggedDate;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(String categoryAmount) {
        this.categoryAmount = categoryAmount;
    }

    public String getFailureType() {
        return failureType;
    }

    public void setFailureType(String failureType) {
        this.failureType = failureType;
    }

    public String getFailureAmount() {
        return failureAmount;
    }

    public void setFailureAmount(String failureAmount) {
        this.failureAmount = failureAmount;
    }

    public String getClientFeedback() {
        return clientFeedback;
    }

    public void setClientFeedback(String clientFeedback) {
        this.clientFeedback = clientFeedback;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getClientCSAT() {
        return clientCSAT;
    }

    public void setClientCSAT(String clientCSAT) {
        this.clientCSAT = clientCSAT;
    }
}

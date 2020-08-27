package com.colcab.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation class that contains methods to validate user inputs
 */

public class Validator {
    public static final Pattern patNumber = Pattern.compile("[0-9]{10}");

    public static boolean isCustomerValid(String customer) {
        return customer != null && customer.length() > 0;
    }

    public static boolean isSerialNumberValid(String serialNum) {
        return serialNum != null && serialNum.length() > 0;
    }

    public static boolean isCaseModelValid(String caseModel) {
        return caseModel != null && caseModel.length() > 0;
    }

    public static boolean isCaseDescValid(String caseDesc) {
        return caseDesc != null && caseDesc.length() > 0;
    }

    public static boolean isRequestedByValid(String fullName) {
        return fullName != null && fullName.length() > 0;
    }

    public static boolean isCustomerPOValid(String customerPO) {
        return customerPO != null && customerPO.length() > 0;
    }

    public static boolean isNumberValid(String number) {
        Matcher matcher = patNumber.matcher(number);
        return matcher.find();
    }

    public static boolean isRegionValid(String region) {
        return region != null && region.length() > 0;
    }

    public static boolean isCategoryTypeValid(String categoryType){
        return categoryType != null && categoryType.length() > 0;
    }

    public static boolean isFailureTypeValid(String failureType){
        return failureType != null && failureType.length() > 0;
    }

    public static boolean isAmountDueValid(Double amount){
        return amount != null;
    }

    public static boolean isRootCauseValid(String rootCause){
        return rootCause != null && rootCause.length() > 0;
    }

    public static boolean isFeedbackValid(String feedback){
        return feedback != null && feedback.length() > 0;
    }

    public static boolean isSatisfactionValid(int satisfactionLevel){
        return satisfactionLevel > 0 && satisfactionLevel <= 5;
    }
    
    public static boolean isSaveAsValid(String saveAs){
        return saveAs != null && saveAs.length() > 0;
    }
}

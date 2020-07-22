package com.colcab;

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
}

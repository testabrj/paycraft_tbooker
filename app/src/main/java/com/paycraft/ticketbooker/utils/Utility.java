package com.paycraft.ticketbooker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.paycraft.ticketbooker.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Utility {

   static boolean isMatching(String regex, String toBeMatched) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toBeMatched);
        return matcher.matches();
    }

    static boolean validateDrivingLicense(String docId) {

        String regex = "^[0-9a-zA-Z]{4,9}$";
        return Utility.isMatching(regex,docId);
    }

    static boolean validateVoterId(String docId) {
        String regex = "^([a-zA-Z]){3}([0-9]){7}?$";
        return Utility.isMatching(regex,docId);
    }

    static boolean validateAadhar(String docId) {

        String regex = "^[0-9]{12}";
        boolean isValidAadhar = Utility.isMatching(regex,docId);
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(docId);
        }
        return isValidAadhar;

    }

    static boolean validatePan(String docId) {
        String regex = "^[A-Z]{3}[ABCFGHLJPTF]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}";
        return Utility.isMatching(regex,docId);
    }

    static String getDocId(Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(activity.getString(R.string.docId), "123123");

    }
}

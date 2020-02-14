package com.paycraft.ticketbooker.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

import com.paycraft.ticketbooker.R

import java.util.regex.Matcher
import java.util.regex.Pattern

interface Utility {
    companion object {

        fun isMatching(regex: String, toBeMatched: String): Boolean {

            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(toBeMatched)
            return matcher.matches()
        }

        fun validateDrivingLicense(docId: String): Boolean {

            val regex = "^[0-9a-zA-Z]{4,9}$"
            return Utility.isMatching(regex, docId)
        }

        fun validateVoterId(docId: String): Boolean {
            val regex = "^([a-zA-Z]){3}([0-9]){7}?$"
            return Utility.isMatching(regex, docId)
        }

        fun validateAadhar(docId: String): Boolean {

            val regex = "^[0-9]{12}"
            var isValidAadhar = Utility.isMatching(regex, docId)
            if (isValidAadhar) {
                isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(docId)
            }
            return isValidAadhar

        }

        fun validatePan(docId: String): Boolean {
            val regex = "^[A-Z]{3}[ABCFGHLJPTF]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}"
            return Utility.isMatching(regex, docId)
        }

        fun getDocId(activity: Activity): String? {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            return sharedPref.getString(activity.getString(R.string.docId), "123123")

        }
    }
}

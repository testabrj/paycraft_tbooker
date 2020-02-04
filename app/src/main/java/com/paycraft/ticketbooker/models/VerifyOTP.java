package com.paycraft.ticketbooker.models;

import com.google.gson.annotations.SerializedName;

public class VerifyOTP {
    @SerializedName("success")
    private
    boolean isValid;

    public VerifyOTP(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "VerifyOTP{" +
                "isValid=" + isValid +
                '}';
    }
}

package com.paycraft.ticketbooker.models;

import com.google.gson.annotations.SerializedName;


public class OTP {

    @SerializedName("otp")
    private
    int otp;

    public OTP(int otp) {
        this.otp = otp;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "OTP{" +
                "otp=" + otp +
                '}';
    }
}

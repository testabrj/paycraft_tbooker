package com.paycraft.ticketbooker.models

import com.google.gson.annotations.SerializedName

class VerifyOTP(@field:SerializedName("success")
                var isValid: Boolean) {

    override fun toString(): String {
        return "VerifyOTP{" +
                "isValid=" + isValid +
                '}'.toString()
    }
}

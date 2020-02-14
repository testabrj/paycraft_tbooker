package com.paycraft.ticketbooker.models

import com.google.gson.annotations.SerializedName


class OTP(@field:SerializedName("otp")
          var otp: Int) {

    override fun toString(): String {
        return "OTP{" +
                "otp=" + otp +
                '}'.toString()
    }
}

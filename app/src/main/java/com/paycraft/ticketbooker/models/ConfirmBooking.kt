package com.paycraft.ticketbooker.models

import com.google.gson.annotations.SerializedName

class ConfirmBooking(
        @field:SerializedName("success")
        var isSuccess: Boolean, var ticketId: String?) {

    override fun toString(): String {
        return "ConfirmBooking{" +
                "success=" + isSuccess +
                ", ticketId='" + ticketId + '\''.toString() +
                '}'.toString()
    }
}


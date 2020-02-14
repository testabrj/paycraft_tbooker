package com.paycraft.ticketbooker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName

@Entity
class Stations(@field:PrimaryKey
               @field:ColumnInfo(name = "id")
               @field:SerializedName("id")
               var mStationId: Int, @field:ColumnInfo(name = "source")
               @field:SerializedName("source")
               var mSource: String, @field:ColumnInfo(name = "destination")
               @field:SerializedName("destination")
               var mDestination: String, @field:ColumnInfo(name = "price")
               @field:SerializedName("price")
               var mPrice: String) {

    fun getmStationId(): Int {
        return mStationId
    }

    fun setmStationId(mStationId: Int) {
        this.mStationId = mStationId
    }

    fun getmSource(): String {
        return mSource
    }

    fun setmSource(mSource: String) {
        this.mSource = mSource
    }

    fun getmDestination(): String {
        return mDestination
    }

    fun setmDestination(mDestination: String) {
        this.mDestination = mDestination
    }

    fun getmPrice(): String {
        return mPrice
    }

    fun setmPrice(mPrice: String) {
        this.mPrice = mPrice
    }

    override fun toString(): String {
        return "Stations{" +
                "mStationId=" + mStationId +
                ", mSource='" + mSource + '\''.toString() +
                ", mDestination='" + mDestination + '\''.toString() +
                ", mPrice='" + mPrice + '\''.toString() +
                '}'.toString()
    }
}

package com.paycraft.ticketbooker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Stations {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public int mStationId;

    @ColumnInfo(name = "source")
    @SerializedName("source")
    public String mSource;

    @ColumnInfo(name = "destination")
    @SerializedName("destination")
    public String mDestination;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    public String mPrice;

    public Stations(int mStationId, String mSource, String mDestination, String mPrice) {
        this.mStationId = mStationId;
        this.mSource = mSource;
        this.mDestination = mDestination;
        this.mPrice = mPrice;
    }

    public int getmStationId() {
        return mStationId;
    }

    public void setmStationId(int mStationId) {
        this.mStationId = mStationId;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
    }

    public String getmDestination() {
        return mDestination;
    }

    public void setmDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public String toString() {
        return "Stations{" +
                "mStationId=" + mStationId +
                ", mSource='" + mSource + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mPrice='" + mPrice + '\'' +
                '}';
    }
}

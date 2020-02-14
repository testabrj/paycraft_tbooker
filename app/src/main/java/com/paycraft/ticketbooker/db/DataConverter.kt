package com.paycraft.ticketbooker.db

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paycraft.ticketbooker.models.Stations

import java.io.Serializable
import java.lang.reflect.Type

class DataConverter : Serializable {

    @TypeConverter // note this annotation
    fun fromStationsList(Stations: List<Stations>?): String? {
        if (Stations == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Stations>>() {

        }.type
        return gson.toJson(Stations, type)
    }

    @TypeConverter // note this annotation
    fun toStationsList(StationsString: String?): List<Stations>? {
        if (StationsString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Stations>>() {

        }.type
        return gson.fromJson<List<Stations>>(StationsString, type)
    }

}
package com.paycraft.ticketbooker.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paycraft.ticketbooker.models.Stations;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromStationsList(List<Stations> Stations) {
        if (Stations == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Stations>>() {
        }.getType();
        String json = gson.toJson(Stations, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Stations> toStationsList(String StationsString) {
        if (StationsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Stations>>() {
        }.getType();
        List<Stations> productCategoriesList = gson.fromJson(StationsString, type);
        return productCategoriesList;
    }

}
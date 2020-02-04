package com.paycraft.ticketbooker.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.paycraft.ticketbooker.db.DataConverter;
import com.paycraft.ticketbooker.models.Stations;

import java.util.ArrayList;
import java.util.List;

@Dao
@TypeConverters(DataConverter.class)
public interface StationsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStations(ArrayList<Stations> station);

    @Query("Select * from Stations")
    List<Stations> getStations();

    @Query("Select * from Stations where source=:source and destination=:destination")
    Stations getCurrentStation(String source,String destination);

    @Query("Select DISTINCT source from Stations")
    List<String> getSource();

    @Query("Select DISTINCT destination from Stations")
    List<String> getDestinations();

    @Query("Select id from Stations where source=:source and destination=:destination")
    Stations getTripId(String source,String destination);

    @Query("Select * from Stations where id=:tripId")
    Stations getStationById(int tripId);
}

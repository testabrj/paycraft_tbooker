package com.paycraft.ticketbooker.db.dao

import androidx.room.*

import com.paycraft.ticketbooker.db.DataConverter
import com.paycraft.ticketbooker.models.Stations

import java.util.ArrayList

@Dao
@TypeConverters(DataConverter::class)
interface StationsDAO {

    @get:Query("Select * from Stations")
    val stations: List<Stations>

    @get:Query("Select DISTINCT source from Stations")
    val source: List<String>

    @get:Query("Select DISTINCT destination from Stations")
    val destinations: List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStations(station: ArrayList<Stations>)

    @Query("Select * from Stations where source=:source and destination=:destination")
    fun getCurrentStation(source: String, destination: String): Stations

    @Query("Select * from Stations where id=:tripId")
    fun getStationById(tripId: Int): Stations
}

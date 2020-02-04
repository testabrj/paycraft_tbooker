package com.paycraft.ticketbooker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.paycraft.ticketbooker.db.dao.StationsDAO;
import com.paycraft.ticketbooker.models.Stations;

@Database(entities = {Stations.class},version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract StationsDAO stationsDAO();
}

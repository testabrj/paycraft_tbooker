package com.paycraft.ticketbooker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.paycraft.ticketbooker.db.dao.StationsDAO
import com.paycraft.ticketbooker.models.Stations

@Database(entities = [Stations::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun stationsDAO(): StationsDAO

    companion object {
        @Volatile private var instance:AppDB? = null
        private val LOCK = Any()

        operator fun invoke(context:Context) = instance?: synchronized(LOCK) {

            instance?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,AppDB::class.java,"TicketBooker").allowMainThreadQueries().build()
    }
}

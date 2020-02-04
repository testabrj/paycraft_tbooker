package com.paycraft.ticketbooker.db;


import android.content.Context;

import androidx.room.Room;

public class DBClient {
    private static DBClient mInstance;

    //our app database object
    private AppDB appDatabase;

    private DBClient(Context mCtx) {

        appDatabase = Room.databaseBuilder(mCtx, AppDB.class, "TicketBooker").allowMainThreadQueries().build();
    }

    public static synchronized DBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DBClient(mCtx);
        }
        return mInstance;
    }

    public AppDB getAppDatabase() {
        return appDatabase;
    }
}
 
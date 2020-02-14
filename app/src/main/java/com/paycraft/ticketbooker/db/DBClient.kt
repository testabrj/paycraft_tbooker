package com.paycraft.ticketbooker.db


import android.content.Context

import androidx.room.Room

//class DBClient private constructor(mCtx: Context) {
//
//    //our app database object
//    val appDatabase: AppDB
//
//    init {
//
//        appDatabase = Room.databaseBuilder(mCtx, AppDB::class.java, "TicketBooker").allowMainThreadQueries().build()
//    }
//
//    companion object :DBClientHolder<DBClient,Context>(::DBClient) {
//        private  var mInstance: DBClient? = null;
//
//        @Synchronized
//        fun getInstance(mCtx: Context): DBClient {
//            if (mInstance == null) {
//                mInstance = DBClient(mCtx)
//            }
//            return mInstance!!
//        }
//    }
//}
//
//open class DBClientHolder<T, U>(value: KFunction1<U, T>) {
//
//}

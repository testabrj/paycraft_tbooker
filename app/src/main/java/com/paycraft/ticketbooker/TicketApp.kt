package com.paycraft.ticketbooker

import android.app.Application

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.paycraft.ticketbooker.utils.AssetInputStreamProvider

import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TicketApp : Application() {


    override fun onCreate() {
        super.onCreate()
        inputStreamProvider = AssetInputStreamProvider(applicationContext)
    }

    companion object {
        private var mRetrofit: Retrofit? = null
        private var inputStreamProvider: AssetInputStreamProvider? = null
        private val BASE_URL = "http://paycrafttickets.com"

        val retrofitInstance: Retrofit
            get() {
                val mOkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(OkHttpMockInterceptor(inputStreamProvider, 5))
                        .build()
                if (mRetrofit == null) {
                    mRetrofit = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(BASE_URL)
                            .client(mOkHttpClient)
                            .build()
                }
                return mRetrofit!!
            }
    }

}



package com.paycraft.ticketbooker;

import android.app.Application;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.paycraft.ticketbooker.utils.AssetInputStreamProvider;

import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketApp extends Application {
    private static Retrofit mRetrofit;
    private static AssetInputStreamProvider inputStreamProvider;
    private static final String BASE_URL = "http://paycrafttickets.com";


    @Override
    public void onCreate() {
        super.onCreate();
         inputStreamProvider = new AssetInputStreamProvider(getApplicationContext());
    }

    public static Retrofit getRetrofitInstance(){
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new OkHttpMockInterceptor(inputStreamProvider, 5))
                .build();
        if(mRetrofit==null) {
            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}



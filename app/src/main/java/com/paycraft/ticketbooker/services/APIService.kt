package com.paycraft.ticketbooker.services

import com.paycraft.ticketbooker.models.ConfirmBooking
import com.paycraft.ticketbooker.models.OTP
import com.paycraft.ticketbooker.models.Stations
import com.paycraft.ticketbooker.models.VerifyOTP
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*


interface APIService {

    companion object {
        const val API_VERSION = "api/v1";

    }
    @GET("$API_VERSION/stations")
    fun stations(): Observable<ArrayList<Stations>>

    @POST("$API_VERSION/otp")
    fun getOTP(@Query("docId") id: String): Observable<OTP>

    @POST("$API_VERSION/verify_otp/{docId}/{otp}")
    fun verifyOTP(@Path("docId") docId: String, @Path("otp") otp: Int): Observable<VerifyOTP>

    @POST("$API_VERSION/confirmBooking")
    fun confirmBooking(@Query("docId") id: String, @Query("destId") dest_id: Int): Observable<ConfirmBooking>



}
package com.paycraft.ticketbooker.services;

import com.paycraft.ticketbooker.models.ConfirmBooking;
import com.paycraft.ticketbooker.models.OTP;
import com.paycraft.ticketbooker.models.Stations;
import com.paycraft.ticketbooker.models.VerifyOTP;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIService {

    String API_VERSION = "api/v1";

    @GET(API_VERSION + "/stations")
    Observable<ArrayList<Stations>> getStations();

    @POST(API_VERSION + "/otp")
    Observable<OTP> getOTP(@Query("docId") String id);

    @POST(API_VERSION + "/verify_otp/{docId}/{otp}")
    Observable<VerifyOTP> verifyOTP(@Path("docId") String docId, @Path("otp") int otp);

    @POST(API_VERSION+ "/confirmBooking")
    Observable<ConfirmBooking> confirmBooking(@Query("docId") String id,@Query("destId") int dest_id);

}
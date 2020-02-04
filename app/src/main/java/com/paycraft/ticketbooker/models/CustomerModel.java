package com.paycraft.ticketbooker.models;

import com.google.gson.annotations.SerializedName;

class CustomerModel {

    @SerializedName("name")
    String mName;

    @SerializedName("dob")
    String mDOB;

    @SerializedName("docType")
    String mDocumentType;

    @SerializedName("docId")
    String mDocumentId;


}

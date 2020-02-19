package com.paycraft.ticketbooker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paycraft.ticketbooker.models.Stations

class ConfirmBookingVM : ViewModel(){

    private val _txtDocId = MutableLiveData<String>()
    val txtDocId:LiveData<String>
    get() = _txtDocId


    private val _txtTktId = MutableLiveData<String>()
    val txtTktId:LiveData<String>
    get() = _txtTktId

    private val _txtSrc = MutableLiveData<String>()
    val txtSrc:LiveData<String>
    get() = _txtSrc

    private val _txtDest = MutableLiveData<String>()
    val txtDest:LiveData<String>
    get() = _txtDest

    private val  _txtPrice = MutableLiveData<String>()
    val txtPrice:LiveData<String>
    get() = _txtPrice

    private val  _txtCfmBooking = MutableLiveData<String>()
    val txtCfmBooking:LiveData<String>
    get() = _txtCfmBooking

//    private val _txtCfmTitle = MutableLiveData<String>()
//    val txtCfmTitle:LiveData<String>
//    get() = _txtCfmTitle

    init {
        this._txtDocId.value = ""
        this._txtTktId.value = ""
        this._txtSrc.value = ""
        this._txtDest.value = ""
        this._txtPrice.value ="0"
        this._txtCfmBooking.value = "Confirm Booking"
    }



    fun changeToSummary(ticketId: String?) {
        this._txtTktId.value = ticketId
        this._txtCfmBooking.value = "Booking Summary"
    }

    fun setUpTripInfo(stations: Stations,docId:String) {
       this._txtDocId.value = docId
       this._txtSrc.value = stations.getmSource()
        this._txtDest.value = stations.getmDestination()
        this._txtPrice.value = stations.getmPrice()
    }


}
package com.paycraft.ticketbooker.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.TicketApp
import com.paycraft.ticketbooker.databinding.FragmentConfirmationBinding
import com.paycraft.ticketbooker.db.AppDB
import com.paycraft.ticketbooker.db.dao.StationsDAO
import com.paycraft.ticketbooker.models.ConfirmBooking
import com.paycraft.ticketbooker.services.APIService
import com.paycraft.ticketbooker.utils.Utility
import com.paycraft.ticketbooker.viewmodels.ConfirmBookingVM

import java.util.Objects

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ConfirmationFragment : Fragment() {

    private var onConfirmBookingListener: OnConfirmBookingListener? = null
    private var stationDAO: StationsDAO? = null
    private var apiService: APIService? = null
    private val mRetrofit = TicketApp.retrofitInstance
    private val compositeDisposable = CompositeDisposable()

    private var gTripId: Int = 0
    private lateinit var binding: FragmentConfirmationBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = mRetrofit.create(APIService::class.java)
        stationDAO =AppDB(context!!).stationsDAO()
    }

    fun setConfirmFragmentListener(onConfirmBookingListener: OnConfirmBookingListener) {
        this.onConfirmBookingListener = onConfirmBookingListener
    }


    interface OnConfirmBookingListener {

        fun onBookAgain()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirmation, container, false)
        binding.confirmationModel = ViewModelProvider(this).get(ConfirmBookingVM::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnConfirmBooking.setOnClickListener{bookTrip()}
        binding.btnBookAgain.setOnClickListener{ bookAgain() }
        return binding.root
    }

    private fun bookAgain() {
        onConfirmBookingListener!!.onBookAgain()
    }


    fun onBookingTrip(docId: String, tripId: Int) {
        gTripId = tripId
        if (stationDAO == null) {
            stationDAO = AppDB(context!!).stationsDAO()
        }
        val stations = stationDAO!!.getStationById(tripId)
        binding.confirmationModel?.setUpTripInfo(stations,docId)
    }

    private fun bookTrip() {

        AlertDialog.Builder(Objects.requireNonNull<FragmentActivity>(activity))
                .setTitle("Confirmation")
                .setMessage("Proceed with booking?")
                .setIcon(R.mipmap.ic_launcher_foreground)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton -> callBooking() }
                .setNegativeButton(android.R.string.no, null).show()


    }

    private fun callBooking() {
        val subscription = apiService!!.confirmBooking(Utility.getDocId(activity!!)!!, gTripId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(Consumer<ConfirmBooking> { this.handleResponse(it) }, Consumer<Throwable> { this.handleError(it) })
        compositeDisposable.add(subscription)
    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(activity, "Please try again!!", Toast.LENGTH_LONG).show()
    }


    private fun handleResponse(confirmBooking: ConfirmBooking) {

        if (confirmBooking.isSuccess) {

            binding.confirmationModel?.changeToSummary(confirmBooking.ticketId);
            binding.tblrTkt.visibility = View.VISIBLE
            binding.btnConfirmBooking.visibility = View.GONE
            binding.btnBookAgain.visibility = View.VISIBLE
            binding.btnBookAgain.isClickable = true

        }
    }

    companion object {

        fun newInstance(): Fragment {
            return ConfirmationFragment()
        }
    }

}

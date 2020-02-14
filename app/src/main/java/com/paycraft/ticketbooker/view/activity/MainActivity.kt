package com.paycraft.ticketbooker.view.activity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.badoualy.stepperindicator.StepperIndicator
import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.TicketApp
import com.paycraft.ticketbooker.adapters.StepsPagerAdapter
import com.paycraft.ticketbooker.services.APIService
import com.paycraft.ticketbooker.utils.CustomViewPager
import com.paycraft.ticketbooker.view.fragment.ConfirmationFragment
import com.paycraft.ticketbooker.view.fragment.OTPFragment
import com.paycraft.ticketbooker.view.fragment.RegistrationFragment
import com.paycraft.ticketbooker.view.fragment.StationFragment

import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity(), RegistrationFragment.OnRegisterListener, OTPFragment.OnVerifyOTPListener, StationFragment.OnTripBookedListener, ConfirmationFragment.OnConfirmBookingListener {

    private val mRetrofit = TicketApp.retrofitInstance
    private val compositeDisposable = CompositeDisposable()
    private var apiService: APIService? = null
    private var pager: CustomViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiService = mRetrofit.create(APIService::class.java)

        pager = findViewById(R.id.pager)
        assert(pager != null)
        pager!!.adapter = StepsPagerAdapter(supportFragmentManager)
        pager!!.setPagingEnabled(false)
        pager!!.currentItem = 0

        val indicator = findViewById<StepperIndicator>(R.id.stepper_indicator)
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager!!, false)

    }


    override fun onDestroy() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onDestroy()
    }


    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is RegistrationFragment) {
            fragment.setOnRegisterListener(this)
        } else if (fragment is OTPFragment) {

            fragment.setOnVerifyOTPListener(this)
        } else if (fragment is ConfirmationFragment) {
            fragment.setConfirmFragmentListener(this)
        } else if (fragment is StationFragment){
            fragment.setOnTripBookedListener(this)
        }
    }

    override fun RegistrationSuccess(documentId: String) {
        val tag = "android:switcher:" + R.id.pager + ":" + 1
        val fragment = supportFragmentManager.findFragmentByTag(tag) as OTPFragment?
        fragment?.onRegistrationSuccess(documentId)
        pager!!.setCurrentItem(1, true)


    }

    override fun OnOTPSuccess() {
        val tag = "android:switcher:" + R.id.pager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag) as StationFragment?
        pager!!.setCurrentItem(2, true)

    }


    override fun onTripBooked(docId: String, tripId: Int) {
        val tag = "android:switcher:" + R.id.pager + ":" + 3
        val fragment = supportFragmentManager.findFragmentByTag(tag) as ConfirmationFragment?
        fragment?.onBookingTrip(docId, tripId)

        pager!!.setCurrentItem(3, true)

    }


    override fun onBookAgain() {

        pager!!.setCurrentItem(0, false)

    }
}

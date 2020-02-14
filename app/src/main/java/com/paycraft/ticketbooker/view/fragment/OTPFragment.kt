package com.paycraft.ticketbooker.view.fragment


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.google.android.material.textfield.TextInputEditText
import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.TicketApp
import com.paycraft.ticketbooker.models.OTP
import com.paycraft.ticketbooker.models.VerifyOTP
import com.paycraft.ticketbooker.services.APIService
import com.paycraft.ticketbooker.utils.Utility

import java.util.Objects

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class OTPFragment : Fragment() {
    private lateinit  var apiService: APIService
    private val mRetrofit = TicketApp.retrofitInstance
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mVerifyOTP: Button
    private lateinit var mOTPEdtText: TextInputEditText

    private var onVerifyOTPListener: OnVerifyOTPListener? = null

    fun setOnVerifyOTPListener(onVerifyOTPListener: OnVerifyOTPListener) {
        this.onVerifyOTPListener = onVerifyOTPListener
    }

    interface OnVerifyOTPListener {
        fun OnOTPSuccess()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        apiService = mRetrofit.create(APIService::class.java)
        val view = inflater.inflate(R.layout.fragment_otp, container, false)
        mOTPEdtText = view.findViewById(R.id.edt_txt_otp)
        mVerifyOTP = view.findViewById(R.id.btn_verify_otp)
        mVerifyOTP!!.setOnClickListener{ this.OnVerify(it) }

        return view
    }

    private fun OnVerify(view: View) {

        val otpTxt = Objects.requireNonNull<Editable>(mOTPEdtText!!.text).toString()

        if (validateOTP(otpTxt)) {

            verifyOTP(otpTxt)
        }

    }

    private fun validateOTP(otpTxt: String): Boolean {
        if (TextUtils.isEmpty(otpTxt)) {
            mOTPEdtText!!.error = "OTP field cannot be empty"
            return false
        } else {
            val regex = "^[0-9]{6}"
            if (!Utility.isMatching(regex, otpTxt)) {

                mOTPEdtText!!.error = "Invalid OTP"
                return false

            }
            return true
        }

    }

    private fun verifyOTP(otp: String) {
        val otp_no = Integer.parseInt(otp)
        val subscription = apiService!!.verifyOTP("12", otp_no)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(Consumer<VerifyOTP> { this.handleResponse(it) }, Consumer<Throwable> { this.handleVerifcationError(it) })
        compositeDisposable.add(subscription)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun requestOTP(docId: String) {
        val subscription = apiService!!.getOTP(docId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(Consumer<OTP> { this.handleResponse(it) }, Consumer<Throwable> { this.handleError(it) })
        compositeDisposable.add(subscription)
    }

    private fun handleError(throwable: Throwable) {

        Toast.makeText(activity, "Please try again!!", Toast.LENGTH_LONG).show()

    }

    private fun handleVerifcationError(throwable: Throwable) {

        mOTPEdtText!!.clearComposingText()
        mOTPEdtText!!.error = "Invalid OTP"

    }

    private fun handleResponse(`object`: Any) {
        if (`object` is OTP) {
            val notificationManager = createNotificationChannel()
            val builder = NotificationCompat.Builder(Objects.requireNonNull<Context>(context), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("TicketBooker")
                    .setContentText(`object`.otp.toString() + " is your OTP for TicketBooker!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            notificationManager.notify(1, builder.build())
        } else if (`object` is VerifyOTP) {

            if (`object`.isValid) {

                onVerifyOTPListener!!.OnOTPSuccess()

            }
        }
    }


    private fun createNotificationChannel(): NotificationManager {


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = Objects.requireNonNull<FragmentActivity>(activity).getSystemService(NotificationManager::class.java)
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            Objects.requireNonNull<NotificationManager>(notificationManager).createNotificationChannel(channel)
            return notificationManager


        } else {
            val notificationManager = ContextCompat.getSystemService(
                    activity!!,
                    NotificationManager::class.java
            ) as NotificationManager

            return notificationManager
        }
    }

    fun onRegistrationSuccess(documentId: String) {

        requestOTP(documentId)
    }

    companion object {


        private val CHANNEL_ID = "234561"


        fun newInstance(): OTPFragment {
            return OTPFragment()
        }
    }


}

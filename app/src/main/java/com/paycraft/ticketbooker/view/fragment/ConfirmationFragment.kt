package com.paycraft.ticketbooker.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.TicketApp
import com.paycraft.ticketbooker.db.AppDB
import com.paycraft.ticketbooker.db.dao.StationsDAO
import com.paycraft.ticketbooker.models.ConfirmBooking
import com.paycraft.ticketbooker.models.Stations
import com.paycraft.ticketbooker.services.APIService
import com.paycraft.ticketbooker.utils.Utility

import java.util.Objects

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ConfirmationFragment : Fragment() {

    private var onConfirmBookingListener: OnConfirmBookingListener? = null
    private var stationDAO: StationsDAO? = null
    private var apiService: APIService? = null
    private val mRetrofit = TicketApp.retrofitInstance
    private val compositeDisposable = CompositeDisposable()
    private val tripId: Int = 0
    private var mDocIdTxt: TextView? = null
    private var mTktIdTxt: TextView? = null
    private var mSourceTxt: TextView? = null
    private var mDestTxt: TextView? = null
    private var mPriceTxt: TextView? = null
    private var mBtnConfirm: Button? = null
    private var gTripId: Int = 0
    private var mCfmTitle: TextView? = null
    private var mTblrTkt: TableRow? = null
    private var mBtnRepeat: Button? = null

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
        val view = inflater.inflate(R.layout.fragment_confirmation, container, false)
        mDocIdTxt = view.findViewById(R.id.txt_doc_id)
        mTktIdTxt = view.findViewById(R.id.txt_tkt_id)
        mSourceTxt = view.findViewById(R.id.txt_source)
        mDestTxt = view.findViewById(R.id.txt_destination)
        mPriceTxt = view.findViewById(R.id.txt_price)
        mBtnConfirm = view.findViewById(R.id.btn_confirm_booking)
        mCfmTitle = view.findViewById(R.id.cfm_title)
        mTblrTkt = view.findViewById(R.id.tblr_tkt)
        mCfmTitle!!.text = getString(R.string.confirmBooking)
        mBtnConfirm!!.setOnClickListener { this.bookTrip(it) }
        mBtnRepeat = view.findViewById(R.id.btn_book_again)
        mBtnRepeat!!.setOnClickListener { this.bookAgain(it) }
        return view
    }

    private fun bookAgain(view: View) {
        onConfirmBookingListener!!.onBookAgain()

    }


    fun onBookingTrip(docId: String, tripId: Int) {
        gTripId = tripId
        if (stationDAO == null) {
            stationDAO = AppDB(context!!).stationsDAO()
        }
        val stations = stationDAO!!.getStationById(tripId)
        mDocIdTxt!!.text = docId
        mSourceTxt!!.text = stations.getmSource()
        mDestTxt!!.text = stations.getmDestination()
        mPriceTxt!!.text = stations.getmPrice()
    }

    private fun bookTrip(view: View) {

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

            mTktIdTxt!!.text = confirmBooking.ticketId
            mTblrTkt!!.visibility = View.VISIBLE
            mCfmTitle!!.text = getString(R.string.bookingSummary)
            mBtnConfirm!!.visibility = View.GONE
            mBtnRepeat!!.visibility = View.VISIBLE
            mBtnRepeat!!.isClickable = true


//                        onConfirmBookingListener.OnConfirmBooking(Utility.getDocId(getActivity()),confirmBooking.getTicketId(),tripId);

        }


    }

    companion object {

        fun newInstance(): Fragment {
            return ConfirmationFragment()
        }
    }

}

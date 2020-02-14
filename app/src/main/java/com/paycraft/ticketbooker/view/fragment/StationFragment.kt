package com.paycraft.ticketbooker.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment

import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.TicketApp
import com.paycraft.ticketbooker.db.AppDB
import com.paycraft.ticketbooker.db.dao.StationsDAO
import com.paycraft.ticketbooker.models.Stations
import com.paycraft.ticketbooker.services.APIService
import com.paycraft.ticketbooker.utils.Utility

import java.util.ArrayList

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OnTripBookedListener] interface
 * to handle interaction events.
 * Use the [StationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // TODO: Rename and change types of parameters
    private val mParam1: String? = null
    private val mParam2: String? = null

    private var progressIndicator: FrameLayout? = null
    private var apiService: APIService? = null
    private val mRetrofit = TicketApp.retrofitInstance
    private val compositeDisposable = CompositeDisposable()
    private var mSpinnerSource: AppCompatSpinner? = null
    private var mSpinnerDest: AppCompatSpinner? = null
    private var stationDAO: StationsDAO? = null
    private var mPriceTxt: TextView? = null
    private var mBookBtn: Button? = null
    private var onTripBookedListener:OnTripBookedListener?=null

    fun setOnTripBookedListener(onTripBookedListener: OnTripBookedListener){
            this.onTripBookedListener = onTripBookedListener

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stationDAO = AppDB(context!!).stationsDAO()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stations, container, false)
        progressIndicator = view.findViewById(R.id.station_progress_holder)
        mSpinnerSource = view.findViewById(R.id.spinner_source)
        mSpinnerDest = view.findViewById(R.id.spinner_destination)
        mPriceTxt = view.findViewById(R.id.txt_price)
        mBookBtn = view.findViewById(R.id.btn_book)
        mBookBtn!!.setOnClickListener { this.onBookPressed(it) }
        apiService = mRetrofit.create(APIService::class.java)
        val stations = stationDAO!!.stations
        if (stations.size == 0) {
            requestStations()
        } else {

            populateStations()

        }

        return view
    }


    private fun requestStations() {
        progressIndicator!!.visibility = View.VISIBLE
        val subscription = apiService!!.stations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(Consumer<ArrayList<Stations>> { this.handleSuccess(it) },
                        Consumer<Throwable> { this.handleError(it) })
        compositeDisposable.add(subscription)
    }

    private fun handleError(throwable: Throwable) {
        progressIndicator!!.visibility = View.GONE
        Toast.makeText(activity, "Please try again!!", Toast.LENGTH_LONG).show()

    }

    private fun setUpPrice() {

        val source = mSpinnerSource!!.selectedItem.toString()
        val destination = mSpinnerDest!!.selectedItem.toString()
        if (!source.equals(destination, ignoreCase = true)) {
            val price = stationDAO!!.getCurrentStation(source, destination).getmPrice()
            mPriceTxt!!.text = price
        } else {
            mPriceTxt!!.text = "NA"
            showAlert()
        }

    }

    private fun populateStations() {

        val spinnerSourceAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, stationDAO!!.source)
        spinnerSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mSpinnerSource!!.adapter = spinnerSourceAdapter
        mSpinnerSource!!.onItemSelectedListener = this

        mSpinnerDest!!.onItemSelectedListener = this

        val spinnerDestAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, stationDAO!!.destinations)
        spinnerDestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mSpinnerDest!!.adapter = spinnerDestAdapter
        setUpPrice()

    }

    private fun handleSuccess(stations: ArrayList<Stations>) {

        stationDAO!!.insertStations(stations)
        populateStations()
        progressIndicator!!.visibility = View.GONE

    }


    // TODO: Rename method, update argument and hook method into UI event
    private fun onBookPressed(view: View) {
        stationDAO = AppDB(context!!).stationsDAO()
        val src = mSpinnerSource!!.selectedItem.toString()
        val dest = mSpinnerDest!!.selectedItem.toString()
        if (!src.equals(dest, ignoreCase = true)) {


            val stations = stationDAO!!.getCurrentStation(src, dest)
            if (stations != null) {

                this.onTripBookedListener!!.onTripBooked(Utility.getDocId(activity!!)!!, stations.getmStationId())
            }

        } else {
            showAlert()

        }
    }

    private fun showAlert() {

        AlertDialog.Builder(activity!!)
                .setTitle("Error")
                .setMessage("Origin and Destination Cannot be Same")
                .setPositiveButton(android.R.string.yes, null)
                .setCancelable(true).show()
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//    if (context is OnTripBookedListener) {
//        mListener = context
//    } else {
//        throw RuntimeException(context!!.toString() + " must implement OnTripBookedListener")
//    }
//}



    override fun onDetach() {
        super.onDetach()
        onTripBookedListener = null
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        setUpPrice()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnTripBookedListener {
        // TODO: Update argument type and name

        fun onTripBooked(docId: String, tripId: Int)
    }



    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         *
         * @return A new instance of fragment StationsFragment.
         */

        fun newInstance(): StationFragment {

            return StationFragment()
        }
    }
}// Required empty public constructor

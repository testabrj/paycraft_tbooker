package com.paycraft.ticketbooker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.paycraft.ticketbooker.R;
import com.paycraft.ticketbooker.TicketApp;
import com.paycraft.ticketbooker.db.DBClient;
import com.paycraft.ticketbooker.db.dao.StationsDAO;
import com.paycraft.ticketbooker.models.Stations;
import com.paycraft.ticketbooker.services.APIService;
import com.paycraft.ticketbooker.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTripBookedListener} interface
 * to handle interaction events.
 * Use the {@link StationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnTripBookedListener mListener;
    private FrameLayout progressIndicator;
    private APIService apiService;
    private Retrofit mRetrofit = TicketApp.getRetrofitInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AppCompatSpinner mSpinnerSource,mSpinnerDest;
    private StationsDAO stationDAO ;
    private TextView mPriceTxt;
    private Button mBookBtn;

    public StationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment StationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StationsFragment newInstance() {
        StationsFragment fragment = new StationsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationDAO =  DBClient.getInstance(getActivity()).getAppDatabase().stationsDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        progressIndicator = view.findViewById(R.id.station_progress_holder);
        mSpinnerSource = view.findViewById(R.id.spinner_source);
        mSpinnerDest = view.findViewById(R.id.spinner_destination);
        mPriceTxt = view.findViewById(R.id.txt_price);
        mBookBtn = view.findViewById(R.id.btn_book);
        mBookBtn.setOnClickListener(this::onBookPressed);
        apiService = mRetrofit.create(APIService.class);
        List<Stations> stations = stationDAO.getStations() ;
        if(stations.size()==0) {
            requestStations();
        } else {

            populateStations();

        }

        return view;
    }



    private void requestStations() {
        progressIndicator.setVisibility(View.VISIBLE);
        Disposable subscription = apiService.getStations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleSuccess,
                        this::handleError);
        compositeDisposable.add(subscription);
    }

    private void handleError(Throwable throwable) {
        progressIndicator.setVisibility(View.GONE);
        Toast.makeText(getActivity(),"Please try again!!",Toast.LENGTH_LONG);

    }

    private void setUpPrice(){

        String source =  mSpinnerSource.getSelectedItem().toString();
        String destination =  mSpinnerDest.getSelectedItem().toString();
        if(!source.equalsIgnoreCase(destination)) {
            String price = stationDAO.getCurrentStation(source, destination).getmPrice();
            mPriceTxt.setText(price);
        } else {
            mPriceTxt.setText("NA");
            showAlert();
        }

    }

    private void populateStations() {

        ArrayAdapter<String> spinnerSourceAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, stationDAO.getSource());
        spinnerSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerSource.setAdapter(spinnerSourceAdapter);
        mSpinnerSource.setOnItemSelectedListener(this);

        mSpinnerDest.setOnItemSelectedListener(this);

        ArrayAdapter<String> spinnerDestAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, stationDAO.getDestinations());
        spinnerDestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerDest.setAdapter(spinnerDestAdapter);
        setUpPrice();

    }

    private void handleSuccess(ArrayList<Stations> stations) {

        stationDAO.insertStations(stations);
        populateStations();
        progressIndicator.setVisibility(View.GONE);

    }


    // TODO: Rename method, update argument and hook method into UI event
    private void onBookPressed(View view) {
            stationDAO =  DBClient.getInstance(getActivity()).getAppDatabase().stationsDAO();
           String src =  mSpinnerSource.getSelectedItem().toString();
           String dest = mSpinnerDest.getSelectedItem().toString();
           if(!src.equalsIgnoreCase(dest)) {


               Stations stations = stationDAO.getCurrentStation(src, dest);
               if (stations != null) {

                   mListener.onTripBooked(Utility.getDocId(getActivity()), stations.getmStationId());
               }

           } else {
               showAlert();

           }
    }

    private void showAlert() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Origin and Destination Cannot be Same")
                .setPositiveButton(android.R.string.yes, null)
                .setCancelable(true).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTripBookedListener) {
            mListener = (OnTripBookedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTripBookedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setUpPrice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTripBookedListener {
        // TODO: Update argument type and name

        void onTripBooked(String docId,int tripId);
    }
}

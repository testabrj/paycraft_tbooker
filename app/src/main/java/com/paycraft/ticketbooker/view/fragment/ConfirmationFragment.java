package com.paycraft.ticketbooker.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.paycraft.ticketbooker.R;
import com.paycraft.ticketbooker.TicketApp;
import com.paycraft.ticketbooker.db.DBClient;
import com.paycraft.ticketbooker.db.dao.StationsDAO;
import com.paycraft.ticketbooker.models.ConfirmBooking;
import com.paycraft.ticketbooker.models.Stations;
import com.paycraft.ticketbooker.services.APIService;
import com.paycraft.ticketbooker.utils.Utility;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ConfirmationFragment extends Fragment {

   private OnConfirmBookingListener onConfirmBookingListener;
    private StationsDAO stationDAO;
    private APIService apiService;
    private final Retrofit mRetrofit = TicketApp.getRetrofitInstance();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int tripId;
    private TextView mDocIdTxt,mTktIdTxt,mSourceTxt,mDestTxt,mPriceTxt;
    private Button mBtnConfirm;
    private int gTripId;
    private TextView mCfmTitle;
    private TableRow mTblrTkt;
    private Button mBtnRepeat;

    public static Fragment newInstance() {
        ConfirmationFragment confirmationFragment = new ConfirmationFragment();
        return confirmationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = mRetrofit.create(APIService.class);
        stationDAO =  DBClient.getInstance(getActivity()).getAppDatabase().stationsDAO();
    }

    public void setConfirmFragmentListener(OnConfirmBookingListener onConfirmBookingListener) {
        this.onConfirmBookingListener = onConfirmBookingListener;
    }



    public interface OnConfirmBookingListener {
        void OnConfirmBooking(String docId,String ticketId,int tripId);
        void onBookAgain();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_confirmation, container, false);
        mDocIdTxt = view.findViewById(R.id.txt_doc_id);
        mTktIdTxt = view.findViewById(R.id.txt_tkt_id);
        mSourceTxt = view.findViewById(R.id.txt_source);
        mDestTxt = view.findViewById(R.id.txt_destination);
        mPriceTxt = view.findViewById(R.id.txt_price);
        mBtnConfirm = view.findViewById(R.id.btn_confirm_booking);
        mCfmTitle = view.findViewById(R.id.cfm_title);
        mTblrTkt = view.findViewById(R.id.tblr_tkt);
        mCfmTitle.setText(getString(R.string.confirmBooking));
        mBtnConfirm.setOnClickListener(this::bookTrip);
        mBtnRepeat = view.findViewById(R.id.btn_book_again);
        mBtnRepeat.setOnClickListener(this::bookAgain);
        return view;
    }

    private void bookAgain(View view) {
        onConfirmBookingListener.onBookAgain();

    }


    public void onBookingTrip(String docId,int tripId) {
        gTripId = tripId;
        if(stationDAO==null) {
            stationDAO = DBClient.getInstance(getActivity()).getAppDatabase().stationsDAO();
        }
        Stations stations = stationDAO.getStationById(tripId);
        mDocIdTxt.setText(docId);
        mSourceTxt.setText(stations.getmSource());
        mDestTxt.setText(stations.getmDestination());
        mPriceTxt.setText(stations.getmPrice());
    }

    private void bookTrip(View view) {

        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle("Confirmation")
                .setMessage("Proceed with booking?")
                .setIcon(R.mipmap.ic_launcher_foreground)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        callBooking();

                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }

    private void callBooking() {
        Disposable subscription = apiService.confirmBooking(Utility.getDocId(Objects.requireNonNull(getActivity())),gTripId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse,this::handleError);
        compositeDisposable.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(getActivity(),"Please try again!!",Toast.LENGTH_LONG).show();
    }


    private void handleResponse(ConfirmBooking confirmBooking) {


        if(confirmBooking.isSuccess()){

            mTktIdTxt.setText(confirmBooking.getTicketId());
            mTblrTkt.setVisibility(View.VISIBLE);
            mCfmTitle.setText(getString(R.string.bookingSummary));
            mBtnConfirm.setVisibility(View.GONE);
            mBtnRepeat.setVisibility(View.VISIBLE);
            mBtnRepeat.setClickable(true);


//            onConfirmBookingListener.OnConfirmBooking(Utility.getDocId(getActivity()),confirmBooking.getTicketId(),tripId);

        }


    }

}

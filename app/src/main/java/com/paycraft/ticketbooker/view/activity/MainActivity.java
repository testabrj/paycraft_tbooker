package com.paycraft.ticketbooker.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.badoualy.stepperindicator.StepperIndicator;
import com.paycraft.ticketbooker.R;
import com.paycraft.ticketbooker.TicketApp;
import com.paycraft.ticketbooker.adapters.StepsPagerAdapter;
import com.paycraft.ticketbooker.services.APIService;
import com.paycraft.ticketbooker.utils.CustomViewPager;
import com.paycraft.ticketbooker.view.fragment.ConfirmationFragment;
import com.paycraft.ticketbooker.view.fragment.OTPFragment;
import com.paycraft.ticketbooker.view.fragment.RegistrationFragment;
import com.paycraft.ticketbooker.view.fragment.StationsFragment;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RegistrationFragment.OnRegisterListener, OTPFragment.OnVerifyOTPListener, StationsFragment.OnTripBookedListener,ConfirmationFragment.OnConfirmBookingListener {

    private final Retrofit mRetrofit = TicketApp.getRetrofitInstance();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private APIService apiService;
    private CustomViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = mRetrofit.create(APIService.class);

        pager = findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(new StepsPagerAdapter(getSupportFragmentManager()));
        pager.setPagingEnabled(false);
        pager.setCurrentItem(0);

        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, false);

    }






    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof RegistrationFragment) {
            ((RegistrationFragment) fragment).setOnRegisterListener(this);
        } else if (fragment instanceof OTPFragment) {

            ((OTPFragment) fragment).setOnVerifyOTPListener(this);
        } else if(fragment instanceof ConfirmationFragment){
            ((ConfirmationFragment) fragment).setConfirmFragmentListener(this);
        }
    }
    @Override
    public void RegistrationSuccess(String documentId) {
        String tag = "android:switcher:" + R.id.pager + ":" + 1;
        OTPFragment fragment = (OTPFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            fragment.onRegistrationSuccess(documentId);
        }
        pager.setCurrentItem(1, true);


    }

    @Override
    public void OnOTPSuccess() {

        pager.setCurrentItem(2, true);

    }


    @Override
    public void onTripBooked(String docId, int tripId) {
        String tag = "android:switcher:" + R.id.pager + ":" + 3;
        ConfirmationFragment fragment = (ConfirmationFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            fragment.onBookingTrip(docId,tripId);
        }

        pager.setCurrentItem(3,true);

    }


    @Override
    public void onBookAgain() {

        pager.setCurrentItem(0,false);

    }
}

package com.paycraft.ticketbooker.view.fragment;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.paycraft.ticketbooker.R;
import com.paycraft.ticketbooker.TicketApp;
import com.paycraft.ticketbooker.models.OTP;
import com.paycraft.ticketbooker.models.VerifyOTP;
import com.paycraft.ticketbooker.services.APIService;
import com.paycraft.ticketbooker.utils.Utility;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OTPFragment extends Fragment {


    private static final String CHANNEL_ID = "234561";
    private APIService apiService;
    private final Retrofit mRetrofit = TicketApp.getRetrofitInstance();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Button mVerifyOTP;
    private TextInputEditText mOTPEdtText;

    private OnVerifyOTPListener onVerifyOTPListener;

    public void setOnVerifyOTPListener(OnVerifyOTPListener onVerifyOTPListener) {
        this.onVerifyOTPListener = onVerifyOTPListener;
    }

    public interface OnVerifyOTPListener {
        void OnOTPSuccess();
    }


    public static OTPFragment newInstance() {
        return new OTPFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        apiService = mRetrofit.create(APIService.class);
        View view =  inflater.inflate(R.layout.fragment_otp, container, false);
        mOTPEdtText = view.findViewById(R.id.edt_txt_otp);
        mVerifyOTP = view.findViewById(R.id.btn_verify_otp);
        mVerifyOTP.setOnClickListener(this::OnVerify);

        return  view;
    }

    private void OnVerify(View view) {

        String otpTxt = Objects.requireNonNull(mOTPEdtText.getText()).toString();

        if(validateOTP(otpTxt)) {

            verifyOTP(otpTxt);
        }

    }

    private boolean validateOTP(String otpTxt) {
        if(TextUtils.isEmpty(otpTxt)) {
            mOTPEdtText.setError("OTP field cannot be empty");
            return false;
        } else {
            String regex = "^[0-9]{6}";
            if (!Utility.isMatching(regex, otpTxt)) {

                mOTPEdtText.setError("Invalid OTP");
                return false;

            }
            return true;
        }

    }

    private void verifyOTP(String otp) {
        int otp_no = Integer.parseInt(otp);
        Disposable subscription = apiService.verifyOTP("12",otp_no)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse,this::handleVerifcationError);
        compositeDisposable.add(subscription);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void requestOTP(String docId) {
        Disposable subscription = apiService.getOTP(docId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse,this::handleError);
        compositeDisposable.add(subscription);
    }

    private void handleError(Throwable throwable) {

        Toast.makeText(getActivity(),"Please try again!!",Toast.LENGTH_LONG).show();

    }

    private void handleVerifcationError(Throwable throwable) {

        mOTPEdtText.clearComposingText();
        mOTPEdtText.setError("Invalid OTP");

    }

    private void handleResponse(Object object) {
        if(object instanceof OTP) {
            OTP otp = (OTP) object;
            NotificationManager notificationManager = createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getContext()), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("TicketBooker")
                    .setContentText(otp.getOtp() + " is your OTP for TicketBooker!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());
        } else if (object instanceof VerifyOTP) {

            VerifyOTP verifyOTP = (VerifyOTP) object;
           if(verifyOTP.isValid()){

               onVerifyOTPListener.OnOTPSuccess();

           }
        }
    }


    private NotificationManager createNotificationChannel() {


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            return notificationManager;


        } else {
           return (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public void onRegistrationSuccess(String documentId) {

        requestOTP(documentId);
    }


}

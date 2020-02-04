package com.paycraft.ticketbooker.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.paycraft.ticketbooker.R;
import com.paycraft.ticketbooker.utils.EditTextDatePicker;
import com.paycraft.ticketbooker.utils.Utility;

import java.util.ArrayList;
import java.util.Objects;




public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private TextView lblPage;
    private Spinner mSpinnerDocType;
    private TextInputEditText mDOBEdtTxt,mNameEdtTxt,mDocIdEdtTxt;
    private Button mRegisterBtn;
    private OnRegisterListener onRegisterListener;

    public void setOnRegisterListener(OnRegisterListener onRegisterListener){
        this.onRegisterListener = onRegisterListener;
    }

    public static RegistrationFragment newInstance() {

        final RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_registration, container, false);

        mSpinnerDocType = view.findViewById(R.id.spinner_document_type);
        mRegisterBtn = view.findViewById(R.id.btn_register);

        mDOBEdtTxt = view.findViewById(R.id.edt_txt_dob);
        mNameEdtTxt = view.findViewById(R.id.edt_txt_name);
        mDocIdEdtTxt = view.findViewById(R.id.edt_txt_doc_id);

        mRegisterBtn.setOnClickListener(this::onClick);
        mNameEdtTxt.addTextChangedListener(new ValidationTextWatcher(mNameEdtTxt));
        mDocIdEdtTxt.addTextChangedListener(new ValidationTextWatcher(mDocIdEdtTxt));
        EditTextDatePicker editTextDatePicker = new EditTextDatePicker(getContext(), mDOBEdtTxt);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getDocumentTypes());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerDocType.setAdapter(spinnerAdapter);

        return view;
    }


    private ArrayList<String> getDocumentTypes(){
        ArrayList<String> documentList = new ArrayList<>();
        documentList.add(0,"PAN");
        documentList.add(1,"Aadhar");
        documentList.add(2,"Voter id");
        documentList.add(3,"Driving License");

        return  documentList;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_register:
                ValidateFields();
                break;
        }
    }

    private void ValidateFields() {

       if(validateName()){
           if(validateDate()) {
               if(validateDocId()){
                    String docId = mDocIdEdtTxt.getText().toString();
                     saveDocId(docId);
                    onRegisterListener.RegistrationSuccess(docId);

               }
           }
       }
    }

    private void saveDocId(String docId) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.docId), docId);
        editor.apply();
    }

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;
        ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){

                case R.id.edt_txt_name:
                    validateName();
                    break;
                case R.id.edt_txt_doc_id:
                    validateDocId();
                    break;
                case R.id.edt_txt_dob:
                    validateDate();
                    break;

            }

        }
    }

    private boolean validateDate(){
        boolean isValidDOB = false;
        String dob = Objects.requireNonNull(mDOBEdtTxt.getText()).toString();
        if(!TextUtils.isEmpty(dob)){
            isValidDOB = true;
        } else {
            mDOBEdtTxt.setError("Please select a Date");
        }
        return isValidDOB;
    }

    private boolean validateDocId() {
        boolean isMatching  = false;
        String docId = mDocIdEdtTxt.getText().toString();
        switch (mSpinnerDocType.getSelectedItemPosition()){

            case 0:
                isMatching =  Utility.validatePan(docId);
                break;
            case 1:
                isMatching =  Utility.validateAadhar(docId);
                break;
            case 2:
                isMatching = Utility.validateVoterId(docId);
                break;
            case 3:
                isMatching =  Utility.validateDrivingLicense(docId);
                break;
        }

        if(!isMatching) {

            mDocIdEdtTxt.setError("Invalid Document Id");
            requestFocus(mDocIdEdtTxt);
            return false;

        }

        return true;


    }


    private boolean validateName() {
       String name =  mNameEdtTxt.getText().toString();
        if(TextUtils.isEmpty(name)) {
            mNameEdtTxt.setError("Name cannot be empty");
            requestFocus(mNameEdtTxt);
            return false;
        } else {

            boolean isMatching = Utility.isMatching("^[a-zA-Z\\s]*$",name);

            if(!isMatching) {
                mNameEdtTxt.setError("Invalid name");
                requestFocus(mNameEdtTxt);
                return false;
            }

        }

        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public interface OnRegisterListener {
        void RegistrationSuccess(String documentId);
    }
}
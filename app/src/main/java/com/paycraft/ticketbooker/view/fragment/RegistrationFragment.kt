package com.paycraft.ticketbooker.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.google.android.material.textfield.TextInputEditText
import com.paycraft.ticketbooker.R
import com.paycraft.ticketbooker.utils.EditTextDatePicker
import com.paycraft.ticketbooker.utils.Utility

import java.util.ArrayList
import java.util.Objects


class RegistrationFragment : Fragment(), View.OnClickListener {

    private val lblPage: TextView? = null
    private lateinit  var mSpinnerDocType: Spinner
    private lateinit  var mDOBEdtTxt: TextInputEditText
    private lateinit var mNameEdtTxt: TextInputEditText
    private lateinit  var mDocIdEdtTxt: TextInputEditText
    private lateinit var mRegisterBtn: Button
    private var onRegisterListener: OnRegisterListener? = null


    private val documentTypes: ArrayList<String>
        get() {
            val documentList = ArrayList<String>()
            documentList.add(0, "PAN")
            documentList.add(1, "Aadhar")
            documentList.add(2, "Voter id")
            documentList.add(3, "Driving License")

            return documentList

        }

    fun setOnRegisterListener(onRegisterListener: OnRegisterListener) {
        this.onRegisterListener = onRegisterListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        mSpinnerDocType = view.findViewById(R.id.spinner_document_type)
        mRegisterBtn = view.findViewById(R.id.btn_register)

        mDOBEdtTxt = view.findViewById(R.id.edt_txt_dob)
        mNameEdtTxt = view.findViewById(R.id.edt_txt_name)
        mDocIdEdtTxt = view.findViewById(R.id.edt_txt_doc_id)

        mRegisterBtn!!.setOnClickListener { this.onClick(it) }
        mNameEdtTxt!!.addTextChangedListener(ValidationTextWatcher(mNameEdtTxt))
        mDocIdEdtTxt!!.addTextChangedListener(ValidationTextWatcher(mDocIdEdtTxt))
        val editTextDatePicker = EditTextDatePicker(context!!, mDOBEdtTxt)


        val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, documentTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mSpinnerDocType!!.adapter = spinnerAdapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_register -> ValidateFields()
        }
    }

    private fun ValidateFields() {

        if (validateName()) {
            if (validateDate()) {
                if (validateDocId()) {
                    val docId = mDocIdEdtTxt!!.text!!.toString()
                    saveDocId(docId)
                    onRegisterListener!!.RegistrationSuccess(docId)

                }
            }
        }
    }

    private fun saveDocId(docId: String) {
        val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(getString(R.string.docId), docId)
        editor.apply()
    }

    private inner class ValidationTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            when (view.id) {

                R.id.edt_txt_name -> validateName()
                R.id.edt_txt_doc_id -> validateDocId()
                R.id.edt_txt_dob -> validateDate()
            }

        }
    }

    private fun validateDate(): Boolean {
        var isValidDOB = false
        val dob = Objects.requireNonNull<Editable>(mDOBEdtTxt!!.text).toString()
        if (!TextUtils.isEmpty(dob)) {
            isValidDOB = true
        } else {
            mDOBEdtTxt!!.error = "Please select a Date"
        }
        return isValidDOB
    }

    private fun validateDocId(): Boolean {
        var isMatching = false
        val docId = mDocIdEdtTxt!!.text!!.toString()
        when (mSpinnerDocType!!.selectedItemPosition) {

            0 -> isMatching = Utility.validatePan(docId)
            1 -> isMatching = Utility.validateAadhar(docId)
            2 -> isMatching = Utility.validateVoterId(docId)
            3 -> isMatching = Utility.validateDrivingLicense(docId)
        }

        if (!isMatching) {

            mDocIdEdtTxt!!.error = "Invalid Document Id"
            requestFocus(mDocIdEdtTxt!!)
            return false

        }

        return true


    }


    private fun validateName(): Boolean {
        val name = mNameEdtTxt!!.text!!.toString()
        if (TextUtils.isEmpty(name)) {
            mNameEdtTxt!!.error = "Name cannot be empty"
            requestFocus(mNameEdtTxt!!)
            return false
        } else {

            val isMatching = Utility.isMatching("^[a-zA-Z\\s]*$", name)

            if (!isMatching) {
                mNameEdtTxt!!.error = "Invalid name"
                requestFocus(mNameEdtTxt!!)
                return false
            }

        }

        return true

    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    interface OnRegisterListener {
        fun RegistrationSuccess(documentId: String)
    }

    companion object {

        fun newInstance(): RegistrationFragment {

            return RegistrationFragment()
        }
    }
}
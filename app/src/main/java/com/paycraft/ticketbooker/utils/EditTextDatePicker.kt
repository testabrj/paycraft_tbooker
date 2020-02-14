package com.paycraft.ticketbooker.utils

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker

import com.google.android.material.textfield.TextInputEditText

import java.util.Calendar
import java.util.TimeZone

class EditTextDatePicker(private val _context: Context, private val _editText: TextInputEditText) : View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private var _day: Int = 0
    private var _month: Int = 0
    private var _birthYear: Int = 0


    init {
        this._editText.setOnClickListener(this)
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _birthYear = year
        _month = monthOfYear
        _day = dayOfMonth

        updateDisplay()
    }

    override fun onClick(v: View) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.YEAR, -18)
        val dialog = DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))

        dialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -82)
        dialog.datePicker.minDate = calendar.timeInMillis
        dialog.show()

    }

    // updates the date in the birth date EditText
    private fun updateDisplay() {

        _editText.setText(StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "))
    }
}
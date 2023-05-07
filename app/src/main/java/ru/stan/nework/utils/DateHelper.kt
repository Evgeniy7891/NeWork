package ru.stan.nework.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

private val calendar = Calendar.getInstance()

object DateHelper {
    fun pickDate(editText: EditText?, context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, dayOfMonth, hourOfDay, minute)
                val result = GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute).time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                editText?.setText(dateFormat.format(result))
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }
}
package com.example.calory_calculator

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Date_Dialog : DialogFragment() {
    var choosenDateInterface: ChooseDateInterface? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.date_dialog, container, false)
        var calendar_view = rootView.findViewById<CalendarView>(R.id.calendarView)
        calendar_view.setOnDateChangeListener { calendar_view, year, month, dayOfMonth ->
            var montht = month + 1
            var monthtWithZero: String? = null
            var dayWithZero: String? = null
            if(montht < 10){
                monthtWithZero = "0$montht"
            }else{
                monthtWithZero = montht.toString()
            }
            if(dayOfMonth < 10){
                dayWithZero = "0$dayOfMonth"
            }else{
                dayWithZero = dayOfMonth.toString()
            }
            var nazwa = "$year-$monthtWithZero-$dayWithZero"
            Log.v("data", nazwa)
            val parse_date_local = LocalDate.parse(nazwa)
            calendar_view.date = parse_date_local.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            choosenDateInterface?.applyDate(nazwa)

            dismiss()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        choosenDateInterface = context as ChooseDateInterface
    }

}

interface ChooseDateInterface{
    fun applyDate(choosenDate: String)
}
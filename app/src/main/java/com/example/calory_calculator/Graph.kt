package com.example.calory_calculator

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calory_calculator.MODELS.days_value
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class Graph : AppCompatActivity() {
    val user = Variables.app?.currentUser()
    val config = SyncConfiguration
        .Builder(user, Variables.app?.currentUser()?.id)
        .allowQueriesOnUiThread(true)
        .allowWritesOnUiThread(true)
        .build()
    var realm : Realm = Realm.getInstance(config)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        var graph: BarChart = findViewById(R.id.graph_bar)
        var date_now = LocalDate.now()
        var parse_date = Date.from(date_now.atStartOfDay(ZoneId.systemDefault()).toInstant())
        var date_last_week = Date.from(date_now.minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        var date_plus_one_day = Date.from(date_now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        open class Graphdata(
                var day_date: String? = null,
                var calory: Long? = null
        )
        var graph_data_list = ArrayList<Graphdata>(7)
        var xvalues = ArrayList<String>()
        var temp_day_number = getDayNumberOld(parse_date)
        var number = 0
        var graph_back_button:ImageButton = findViewById(R.id.back_btn)
        var graph_forward_button:ImageButton = findViewById(R.id.forward_btn)
        var graph_text:TextView = findViewById(R.id.text_graph)

        var day = date_now.dayOfMonth
        var month = date_now.monthValue
        var year = date_now.year
        var begin_date = "$day/$month/$year"
        var end_date_tolocal = convertToLocalDate(date_last_week)
        day = end_date_tolocal?.dayOfMonth!!
        month = end_date_tolocal.monthValue
        year = end_date_tolocal.year
        var end_date = "$day/$month/$year"
        graph_text.text = "$end_date - $begin_date"

        while(xvalues.size != 7) {
            if(temp_day_number != 7){
                xvalues.add(numerToDays(temp_day_number + 1))
                temp_day_number++
            }
            else{
                xvalues.add(numerToDays(number + 1))
                number++
            }
        }

        xvalues.forEach {
            graph_data_list.add(Graphdata(it, 0))
        }

        realm.executeTransaction {
            val dates = it.where<days_value>().findAll()
            if (!dates.isEmpty()) {
                var sorted_dates = dates.sort("date")

                Log.v("datasorted", sorted_dates.size.toString())
                for (date in sorted_dates) {
                    if (date.date?.before(date_plus_one_day)!! && date.date?.after(date_last_week)!!) {
                        graph_data_list.forEach {
                            if(it.day_date == numerToDays(getDayNumberOld(date.date))){
                                it.calory = date.actual_calory
                            }
                        }
                    }
                }
            }
        }

        val lineentry = ArrayList<BarEntry>()
        for ((index, calory) in graph_data_list.withIndex()) {
                lineentry.add(BarEntry(calory.calory?.toFloat()!!, index))
        }
            val bardataset = BarDataSet(lineentry, "Calory Values")
            val data = BarData(xvalues, bardataset)
            data.setValueTextSize(20f)
            graph.data = data
            graph.setDescription("Week Calory Graph")
            graph.animateY(3000)
            graph.setDescriptionTextSize(30f)

        graph_back_button.setOnClickListener {
            graph_data_list = ArrayList<Graphdata>(7)
            xvalues = ArrayList<String>()
            temp_day_number = getDayNumberOld(parse_date)
            number = 0
            date_now = date_now.minusWeeks(1)
            parse_date = Date.from(date_now.atStartOfDay(ZoneId.systemDefault()).toInstant())
            date_last_week = Date.from(date_now?.minusWeeks(1)?.atStartOfDay(ZoneId.systemDefault())?.toInstant())
            date_plus_one_day = Date.from(date_now?.plusDays(1)?.atStartOfDay(ZoneId.systemDefault())?.toInstant())

            day = date_now.dayOfMonth
            month = date_now.monthValue
            year = date_now.year
            begin_date = "$day/$month/$year"
            end_date_tolocal = convertToLocalDate(date_last_week)
            day = end_date_tolocal?.dayOfMonth!!
            month = end_date_tolocal?.monthValue!!
            year = end_date_tolocal?.year!!
            end_date = "$day/$month/$year"
            graph_text.text = "$end_date - $begin_date"

            while(xvalues.size != 7) {
                if(temp_day_number != 7){
                    xvalues.add(numerToDays(temp_day_number + 1))
                    temp_day_number++
                }
                else{
                    xvalues.add(numerToDays(number + 1))
                    number++
                }
            }

            xvalues.forEach {
                graph_data_list.add(Graphdata(it, 0))
            }

            realm.executeTransaction {
                val dates = it.where<days_value>().findAll()
                if (!dates.isEmpty()) {
                    var sorted_dates = dates.sort("date")

                    Log.v("datasorted", sorted_dates.size.toString())
                    for (date in sorted_dates) {
                        if (date.date?.before(date_plus_one_day)!! && date.date?.after(date_last_week)!!) {
                            graph_data_list.forEach {
                                if(it.day_date == numerToDays(getDayNumberOld(date.date))){
                                    it.calory = date.actual_calory
                                }
                            }
                        }
                    }
                }
            }

            val lineentry = ArrayList<BarEntry>()
            for ((index, calory) in graph_data_list.withIndex()) {
                lineentry.add(BarEntry(calory.calory?.toFloat()!!, index))
            }
            val bardataset = BarDataSet(lineentry, "Calory Values")
            val data = BarData(xvalues, bardataset)
            data.setValueTextSize(20f)
            graph.data = data
            graph.setDescription("Week Calory Graph")
            graph.animateY(3000)
            graph.setDescriptionTextSize(30f)
            graph.refreshDrawableState()
        }

        graph_forward_button.setOnClickListener {
            graph_data_list = ArrayList<Graphdata>(7)
            xvalues = ArrayList<String>()
            temp_day_number = getDayNumberOld(parse_date)
            number = 0

            date_now = date_now.plusWeeks(1)
            parse_date = Date.from(date_now.atStartOfDay(ZoneId.systemDefault()).toInstant())
            date_last_week = Date.from(date_now?.minusWeeks(1)?.atStartOfDay(ZoneId.systemDefault())?.toInstant())
            date_plus_one_day = Date.from(date_now?.plusDays(1)?.atStartOfDay(ZoneId.systemDefault())?.toInstant())

            day = date_now.dayOfMonth
            month = date_now.monthValue
            year = date_now.year
            begin_date = "$day/$month/$year"
            end_date_tolocal = convertToLocalDate(date_last_week)
            day = end_date_tolocal?.dayOfMonth!!
            month = end_date_tolocal?.monthValue!!
            year = end_date_tolocal?.year!!
            end_date = "$day/$month/$year"
            graph_text.text = "$end_date - $begin_date"

            while(xvalues.size != 7) {
                if(temp_day_number != 7){
                    xvalues.add(numerToDays(temp_day_number + 1))
                    temp_day_number++
                }
                else{
                    xvalues.add(numerToDays(number + 1))
                    number++
                }
            }

            xvalues.forEach {
                graph_data_list.add(Graphdata(it, 0))
            }

            realm.executeTransaction {
                val dates = it.where<days_value>().findAll()
                if (!dates.isEmpty()) {
                    var sorted_dates = dates.sort("date")

                    Log.v("datasorted", sorted_dates.size.toString())
                    for (date in sorted_dates) {
                        if (date.date?.before(date_plus_one_day)!! && date.date?.after(date_last_week)!!) {
                            graph_data_list.forEach {
                                if(it.day_date == numerToDays(getDayNumberOld(date.date))){
                                    it.calory = date.actual_calory
                                }
                            }
                        }
                    }
                }
            }

            val lineentry = ArrayList<BarEntry>()
            for ((index, calory) in graph_data_list.withIndex()) {
                lineentry.add(BarEntry(calory.calory?.toFloat()!!, index))
            }
            val bardataset = BarDataSet(lineentry, "Calory Values")
            val data = BarData(xvalues, bardataset)
            data.setValueTextSize(20f)
            graph.data = data
            graph.setDescription("Week Calory Graph")
            graph.animateY(3000)
            graph.setDescriptionTextSize(30f)
            graph.refreshDrawableState()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


    fun getDayNumberOld(date: Date?): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.DAY_OF_WEEK]
    }

    fun numerToDays(number: Int):String{
        var name_of_day: String? = null
        when(number){
            1 -> {
                name_of_day = "Sun"
            }
            2 -> {
                name_of_day = "Mon"
            }
            3 -> {
                name_of_day = "Tue"
            }
            4 -> {
                name_of_day = "Wed"
            }
            5 -> {
                name_of_day = "Thu"
            }
            6 -> {
                name_of_day = "Fri"
            }
            7 -> {
                name_of_day = "Sat"
            }
        }
        return name_of_day!!
    }

    fun convertToLocalDate(dateToConvert: Date): LocalDate? {
        return Instant.ofEpochMilli(dateToConvert.time)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
    }
}
package com.example.calory_calculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.calory_calculator.MODELS.days_value
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
import java.time.DayOfWeek
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
        var parse_date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        var date_last_week = Date.from(LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        var date_plus_one_day = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        open class Graphdata(
                var day_date: String? = null,
                var calory: Long? = null
        )
        var graph_data_list = ArrayList<Graphdata>(7)
        val xvalues = ArrayList<String>()
        var temp_day_number = getDayNumberOld(parse_date)
        var number = 0
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

        Log.v("order_good", xvalues.toString())
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

        for (date in graph_data_list) {
            Log.v("pokza czydziala", date.calory.toString())
        }

        val lineentry = ArrayList<BarEntry>()
        for ((index, calory) in graph_data_list.withIndex()) {
                lineentry.add(BarEntry(calory.calory?.toFloat()!!, index))
        }
            val bardataset = BarDataSet(lineentry, "Calory")
            val data = BarData(xvalues, bardataset)
            graph.data = data
            graph.setDescription("Set Bar Chart Description")
            bardataset.color = resources.getColor(R.color.purple_500)
            graph.animateY(3000)

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
}
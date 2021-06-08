package com.example.calory_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

class Graph : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        var graph: BarChart = findViewById(R.id.graph_bar)
        //var items: ArrayList<BarEntry> = ArrayList()
        val xvalues = ArrayList<String>()
        xvalues.add("Monday")
        xvalues.add("Tuesday")
        xvalues.add("Wednesday")
        xvalues.add("Thursday")
        xvalues.add("Friday")
        xvalues.add("Saturday")
        xvalues.add("Sunday")

        val lineentry = ArrayList<BarEntry>()
        lineentry.add(BarEntry(1f, 0))
        lineentry.add(BarEntry(1f, 1))
        lineentry.add(BarEntry(1f, 2))
        lineentry.add(BarEntry(1f, 3))
        //lineentry.add(BarEntry(1f, 4))
       // lineentry.add(BarEntry(1f, 5))
        //lineentry.add(BarEntry(1f, 6))

        val bardataset = BarDataSet(lineentry, "first")

        val data = BarData(xvalues, bardataset)
        graph.data = data
        graph.setDescription("Set Bar Chart Description")
        bardataset.color = resources.getColor(R.color.purple_500)
        graph.animateY(5000)


    }
}
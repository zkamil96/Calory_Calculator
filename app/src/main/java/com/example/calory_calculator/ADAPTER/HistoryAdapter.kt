package com.example.calory_calculator.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.MODELS.history_value
import com.example.calory_calculator.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HistoryAdapter(
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>(){
    private var myList = emptyList<history_value>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_history_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name_history?.text = myList[position].name
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val instant: Instant = myList[position].date!!.toInstant()
        val localDate: LocalDateTime = instant.atZone(defaultZoneId).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = localDate.format(formatter)
        //holder.date_history?.text = myList[position].date.toString()
        holder.date_history?.text = formatted
/*        var temp = myList[position].date.toString()
        val formatter = DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter()
        formatter.parse(temp)
        holder.date_history?.text = temp*/
    }

    fun setData(newList: List<history_value>){
        myList = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var name_history: TextView = itemView.findViewById(R.id.recycle_view_history_name)
        var date_history: TextView = itemView.findViewById(R.id.recycle_view_history_date)
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }
    }

    interface OnItemClickListener{
        //fun onItemClick(id: Int, name:String)
    }

}
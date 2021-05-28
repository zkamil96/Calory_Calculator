package com.example.calory_calculator.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.MODELS.*
import com.example.calory_calculator.R

class FoodAdapter (
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.MyViewHolder>(){
    private var myList = emptyList<days_value_breakfast>()
    private var myList2 = emptyList<days_value_lunchtime>()
    private var myList3 = emptyList<days_value_snacks>()
    private var myList4 = emptyList<days_value_dinner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_food_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        var list_size = 0
        if(myList.isNotEmpty()){
            list_size = myList.size
        }else if(myList2.isNotEmpty()){
            list_size = myList2.size
        }
        else if(myList3.isNotEmpty()){
            list_size = myList3.size
        }
        else if(myList4.isNotEmpty()){
            list_size = myList4.size
        }
        return list_size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(myList.isNotEmpty()){
            holder.name_recycler?.text =  "Product name: \n" + myList[position].name
            holder.amount_recycler?.text = "Calories: " + myList[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList[position].proteins.toString()
        }else if(myList2.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList2[position].name
            holder.amount_recycler?.text = "Calories: " + myList2[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList2[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList2[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList2[position].proteins.toString()
        }else if(myList3.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList3[position].name
            holder.amount_recycler?.text = "Calories: " + myList3[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList3[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList3[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList3[position].proteins.toString()
        }else if(myList4.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList4[position].name
            holder.amount_recycler?.text = "Calories: " + myList4[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList4[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList4[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList4[position].proteins.toString()
        }


    }

    fun setData(newList: List<days_value_breakfast>){
        myList = newList
        notifyDataSetChanged()
    }
    fun setDataLunch(newList: List<days_value_lunchtime>){
        myList2 = newList
        notifyDataSetChanged()
    }
    fun setDatasnacks(newList: List<days_value_snacks>){
        myList3 = newList
        notifyDataSetChanged()
    }
    fun setDatadinner(newList: List<days_value_dinner>){
        myList4 = newList
        notifyDataSetChanged()
    }




    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var name_recycler: TextView = itemView.findViewById(R.id.name_recycler)
        var amount_recycler: TextView = itemView.findViewById(R.id.amount_recycler)
        var fats_recycler: TextView = itemView.findViewById(R.id.fats_recycler)
        var carbohydrates_recycler: TextView = itemView.findViewById(R.id.carbohydrates_recycler)
        var proteins_recycler: TextView = itemView.findViewById(R.id.proteins_recycler)

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
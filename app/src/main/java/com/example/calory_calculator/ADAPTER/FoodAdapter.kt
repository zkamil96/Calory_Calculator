package com.example.calory_calculator.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.MODELS.*
import com.example.calory_calculator.R
import com.example.calory_calculator.Variables
import com.example.calory_calculator.app
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import java.time.ZoneId
import java.util.*

class FoodAdapter (
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.MyViewHolder>(){
    val user = app.currentUser()
    val config = SyncConfiguration
            .Builder(user, app.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)
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
        var actual_date = Variables.choosen_date
        var parse_date = Date.from(actual_date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        if(myList.isNotEmpty()){
            holder.name_recycler?.text =  "Product name: \n" + myList[position].name
            holder.amount_recycler?.text = "Calories: " + myList[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList[position].proteins.toString()
            holder.button_recycler.setOnClickListener {
                realm.executeTransaction {
                    var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                    if(dataAboutProducts != null){
                        if(dataAboutProducts.breakfast.isNotEmpty()){
                            dataAboutProducts.breakfast.find { it.name == myList[holder.absoluteAdapterPosition].name }?.deleteFromRealm()
                        }
                    }
                }
                notifyItemRemoved(holder.absoluteAdapterPosition)
            }
        }else if(myList2.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList2[position].name
            holder.amount_recycler?.text = "Calories: " + myList2[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList2[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList2[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList2[position].proteins.toString()
            holder.button_recycler.setOnClickListener {
                realm.executeTransaction {
                    var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                    if(dataAboutProducts != null){
                        if(dataAboutProducts.lunchtime.isNotEmpty()){
                            dataAboutProducts.lunchtime.find { it.name == myList2[holder.absoluteAdapterPosition].name }?.deleteFromRealm()
                        }
                    }
                }
                notifyItemRemoved(holder.absoluteAdapterPosition)
            }
        }else if(myList3.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList3[position].name
            holder.amount_recycler?.text = "Calories: " + myList3[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList3[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList3[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList3[position].proteins.toString()
            holder.button_recycler.setOnClickListener {
                realm.executeTransaction {
                    var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                    if(dataAboutProducts != null){
                        if(dataAboutProducts.snacks.isNotEmpty()){
                            dataAboutProducts.snacks.find { it.name == myList3[holder.absoluteAdapterPosition].name }?.deleteFromRealm()
                        }
                    }
                }
                notifyItemRemoved(holder.absoluteAdapterPosition)
            }
        }else if(myList4.isNotEmpty()){
            holder.name_recycler?.text = "Product name: \n" + myList4[position].name
            holder.amount_recycler?.text = "Calories: " + myList4[position].calories.toString()
            holder.fats_recycler?.text = "Fats: " + myList4[position].fats.toString()
            holder.carbohydrates_recycler?.text = "Carbohydrates: " + myList4[position].carbohydrates.toString()
            holder.proteins_recycler?.text = "Proteins: " + myList4[position].proteins.toString()
            holder.button_recycler.setOnClickListener {
                realm.executeTransaction {
                    var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                    if(dataAboutProducts != null){
                        if(dataAboutProducts.dinner.isNotEmpty()){
                            dataAboutProducts.dinner.find { it.name == myList4[holder.absoluteAdapterPosition].name }?.deleteFromRealm()
                        }
                    }
                }
                notifyItemRemoved(holder.absoluteAdapterPosition)
            }
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
        var button_recycler: ImageButton = itemView.findViewById(R.id.recycler_button_delete)

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
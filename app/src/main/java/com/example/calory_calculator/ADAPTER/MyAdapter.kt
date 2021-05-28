package com.example.calory_calculator.ADAPTER

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.API.ApiService
import com.example.calory_calculator.Favorite_products
import com.example.calory_calculator.MODELS.Image_Class
import com.example.calory_calculator.MODELS.Prod
import com.example.calory_calculator.R
import com.example.calory_calculator.MODELS.Result
import com.example.calory_calculator.Search
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Console
import java.util.*

class MyAdapter(
        private val listener: OnItemClickListener
        ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    private var myList = emptyList<Result>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView?.text = myList[position].name
            var image_string: String = myList[position].image
            Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/$image_string?apiKey=1cfbb8a54c5e49cd9852c604d1effe70").into(holder.imageView)
    }

    fun setData(newList: List<Result>){
        //var list = newList.filter { it.id != 10149 }
        //myList = list
        myList = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var textView: TextView = itemView.findViewById(R.id.text_view_recycle)
        var imageView: ImageView = itemView.findViewById(R.id.image_recycle)
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            var item_name = myList[position].name
            var item_id = myList[position].id
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(item_id, item_name)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(id: Int, name:String)
    }

}


package com.example.calory_calculator.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.MODELS.favorite_list_value
import com.example.calory_calculator.R
import com.example.calory_calculator.Variables
import com.example.calory_calculator.app
import com.example.calory_calculator.realm
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration

class FavoriteListAdapter(
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavoriteListAdapter.MyViewHolder>(){
/*    val user = app.currentUser()
    val config = SyncConfiguration
            .Builder(user, app.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)*/
    private var myList = emptyList<favorite_list_value>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_favorite_product, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView?.text = myList[position].name
        holder.imageButton.setOnClickListener {
            realm?.executeTransaction {
                val product = it.where<favorite_list_value>().equalTo("_id", myList[position]._id).findFirst()
                product?.deleteFromRealm()
            }
            notifyItemRemoved(position)
        }
    }

    fun setData(newList: List<favorite_list_value>){
        myList = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var textView: TextView = itemView.findViewById(R.id.favorite_product_text)
        var imageButton: ImageButton = itemView.findViewById(R.id.delete_product_button)
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            var item_name = myList[position].name
            var item_id = myList[position].product_id
            if(position != RecyclerView.NO_POSITION) {
                listener.onFavItemClick(item_id!!, item_name!!)
            }
        }
    }

    interface OnItemClickListener{
        fun onFavItemClick(id: Long, name:String)
    }

}

package com.example.calory_calculator.API

import android.media.Image
import com.example.calory_calculator.MODELS.Nutriens_Info
import com.example.calory_calculator.MODELS.Prod
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("food/ingredients/search?apiKey=1cfbb8a54c5e49cd9852c604d1effe70")
    fun getProducts(@Query("query") search_value: String) : Call<Prod>
    @GET("food/ingredients/{id}/information?apiKey=1cfbb8a54c5e49cd9852c604d1effe70&unit=g")
    fun getInfoAboutProduct(@Path("id") id:Int, @Query("amount") search_amount: Int) : Call<Nutriens_Info>

    companion object{
        const val base_URL = "https://api.spoonacular.com"
    }
}
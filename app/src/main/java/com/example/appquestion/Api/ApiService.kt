package com.example.appquestion.Api

import com.example.appquestion.Model.Categories
import com.example.appquestion.Model.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api.php")
    fun getQuizData(
        @Query("amount") amount: Int?,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?
    ): Call<Model.QuizQuestion>

    @GET("api_category.php")
    fun getCategory():Call<Categories.trivia>
}
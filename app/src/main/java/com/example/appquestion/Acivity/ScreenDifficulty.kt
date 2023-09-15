package com.example.appquestion.Acivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.appquestion.Api.ApiService
import com.example.appquestion.Model.Categories
import com.example.appquestion.Model.Model
import com.example.appquestion.NetWork.RetrofitInstance
import com.example.appquestion.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_screen_difficulty.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScreenDifficulty : AppCompatActivity() {

    private var data: java.io.Serializable? = null;
    private lateinit var btnEasy: Button;
    private lateinit var btnMedium: Button;
    private lateinit var btnHard: Button;
    private lateinit var context: Context;

    private val DIFFICULTY = arrayListOf<String>("easy", "medium", "hard"); // Độ khó
    private var AMOUNT = 10; // Số câu hỏi Default = 10
    private var ID_CATEGORY: Int? = null; // Mã danh mục (có hoặc không)
    private var TYPE = arrayListOf<String>("multiple", "boolean") // kiểu đáp án


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_difficulty)
        Reflection()
        data = intent?.getSerializableExtra("Category")

        if (data != null) {
            ID_CATEGORY = (data as Categories.trivia_categories).id
        }


        btnEasy.setOnClickListener {
            startLoading(View.VISIBLE)
            CallApi(AMOUNT, ID_CATEGORY, DIFFICULTY[0], TYPE[0])
        }
        btnMedium.setOnClickListener {
            startLoading(View.VISIBLE)
            CallApi(AMOUNT, ID_CATEGORY, DIFFICULTY[1], TYPE[0])
        }
        btnHard.setOnClickListener {
            startLoading(View.VISIBLE)
            CallApi(AMOUNT, ID_CATEGORY, DIFFICULTY[2], TYPE[0])
        }


    }

    private fun Reflection() {
        context = this;
        btnEasy = activity_screen_difficulty_btn_easy
        btnMedium = activity_screen_difficulty_btn_medium
        btnHard = activity_screen_difficulty_btn_hard
    }

    private fun CallApi(amount: Int, category: Int?, difficulty: String, type: String) {
        val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
        val call = apiService.getQuizData(amount, category, difficulty, type)

        call.enqueue(object : Callback<Model.QuizQuestion> {
            override fun onResponse(
                call: Call<Model.QuizQuestion>, response: Response<Model.QuizQuestion>
            ) {
                if (response.isSuccessful) {
                    val data= response.body()
                    if (data != null) {
                        val quizQuestion = data as Model.QuizQuestion
                        startLoading(View.GONE)
                        val gson = Gson()
                        val quizQuestionJson = gson.toJson(quizQuestion)
                        var bundle = Bundle()
                        var intent = Intent(context, ScreenGame::class.java)
                        bundle.putString("QuizQuestionJson", quizQuestionJson)
                        intent.putExtras(bundle)
                        startActivity(intent)

                    } else {
                        // Xử lý trường hợp body trả về null
                        Toast.makeText(context, "Response body is null",Toast.LENGTH_LONG).show()
                        println("Response body is null")
                    }
                } else {
                    // Xử lý lỗi khi cuộc gọi API không thành công
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        try {
                            val errorResponse =
                                Gson().fromJson(
                                    errorBody.string(),
                                    ScreenMode.ErrorResponse::class.java
                                )
                            Toast.makeText(context, "${errorResponse.message}",Toast.LENGTH_LONG).show()
                            startLoading(View.GONE)
                            // Xử lý lỗi từ phản hồi JSON
                        } catch (e: Exception) {
                            // Xử lý lỗi khi không thể phân tích JSON
                            Toast.makeText(context, "${e.message}",Toast.LENGTH_LONG).show()
                            println("Error parsing error response JSON")
                            startLoading(View.GONE)

                        }
                    } else {
                        // Xử lý trường hợp errorBody là null
                        startLoading(View.GONE)
                        Toast.makeText(context, "Error body is null",Toast.LENGTH_LONG).show()
                        println("Error body is null")
                    }
                }
            }

            override fun onFailure(call: Call<Model.QuizQuestion>, t: Throwable) {
                // Xử lý lỗi khi có lỗi trong quá trình gọi API
                startLoading(View.GONE)
                Toast.makeText(context, "Error calling API: ${t.message}",Toast.LENGTH_LONG).show()
                println("Error calling API: ${t.message}")
            }
        })
    }

    private fun startLoading(isVisibility: Int) {
        activity_sreen_difficulty_progressBar.visibility = isVisibility
        activity_sreen_difficulty_blockingView.visibility = isVisibility
    }
}
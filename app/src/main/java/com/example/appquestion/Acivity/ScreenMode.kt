package com.example.appquestion.Acivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.appquestion.Api.ApiService
import com.example.appquestion.Model.Categories
import com.example.appquestion.Model.Model
import com.example.appquestion.NetWork.RetrofitInstance
import com.example.appquestion.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_screen_mode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScreenMode : AppCompatActivity() {

    val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_mode)
        SetUp()
        Event()
    }

    private fun SetUp() {
        activity_sreen_mode_progressBar.progress = 0
    }

    private fun Event() {
        activity_sreen_mode_btn_category.setOnClickListener {
            startLoading(View.VISIBLE)
            CallApiCategory()
        }

        activity_sreen_mode_btn_random.setOnClickListener {
            startActivity(Intent(context,ScreenDifficulty::class.java))
        }
    }

    private fun CallApiCategory() {
        val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
        val call = apiService.getCategory()

        call.enqueue(object : Callback<Categories.trivia> {
            override fun onResponse(
                call: Call<Categories.trivia>, response: Response<Categories.trivia>
            ) {
                if (response.isSuccessful) {
                    startLoading(View.GONE)
                    val trivia = response.body();
                    if (trivia != null) {
                        val intent = Intent(context, ScreenCategory::class.java);
                        val bundle = Bundle();
                        val categories = ArrayList(trivia.trivia_categories);
                        bundle.putSerializable("Categories", categories);
                        intent.putExtras(bundle);
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "Không lấy được dữ liệu",Toast.LENGTH_LONG);
                    }

                } else {
                    // Xử lý lỗi khi cuộc gọi API không thành công
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        try {
                            val errorResponse =
                                Gson().fromJson(errorBody.string(), ErrorResponse::class.java)
                            // Xử lý lỗi từ phản hồi JSON
                            handleErrorResponse(errorResponse)
                        } catch (e: Exception) {
                            // Xử lý lỗi khi không thể phân tích JSON
                            println("Error parsing error response JSON")
                        }
                    } else {
                        // Xử lý trường hợp errorBody là null
                        println("Error body is null")
                    }
                }
            }

            override fun onFailure(call: Call<Categories.trivia>, t: Throwable) {
                // Xử lý lỗi khi có lỗi trong quá trình gọi API
                println("Error calling API: ${t.message}")
            }

        })
    }

    data class ErrorResponse(
        val code: Int, val message: String
    )

    fun handleErrorResponse(errorResponse: ErrorResponse) {
        Toast.makeText(context, "Error Code: ${errorResponse.code}", Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Error Code: ${errorResponse.message}", Toast.LENGTH_LONG).show();
        println("Error Code: ${errorResponse.code}")
        println("Error Message: ${errorResponse.message}")
        // Xử lý lỗi dựa trên mã và thông báo lỗi
    }

    private fun startLoading(isVisibility: Int) {
        activity_sreen_mode_progressBar.visibility = isVisibility
        blockingView.visibility = isVisibility
    }
}
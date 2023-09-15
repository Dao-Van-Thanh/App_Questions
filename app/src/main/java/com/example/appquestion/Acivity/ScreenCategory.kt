package com.example.appquestion.Acivity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appquestion.Adapter.AdapterRcyViewCategory
import com.example.appquestion.Model.Categories
import com.example.appquestion.R
import kotlinx.android.synthetic.main.activity_screen_category.*

class ScreenCategory : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView;

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_category)

        var data =
            intent.getSerializableExtra("Categories") as? ArrayList<Categories.trivia_categories>
        if (data != null) {
            recyclerView = activity_screen_category_recyclerview;
            loadRecyclerView(data)
        }
    }

    private fun loadRecyclerView(data: ArrayList<Categories.trivia_categories>) {
        val layoutManager = GridLayoutManager(this, 2) // 2 cột
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1 // Đặt mỗi mục có kích thước của 1 cột
            }
        }
        recyclerView?.layoutManager = layoutManager
        val adapter = AdapterRcyViewCategory(this)
        adapter.senData(data)
        // Thay MyAdapter bằng tên của adapter của bạn
        recyclerView?.adapter = adapter
    }
}
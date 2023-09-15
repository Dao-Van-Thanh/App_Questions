package com.example.appquestion.Acivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appquestion.R
import kotlinx.android.synthetic.main.activity_screen_first.*

class ScreenFirst : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_first)
        event()
    }

    private fun event() {
        activity_screen_first_btn_start_game.setOnClickListener {
            startActivity(Intent(this,ScreenMode::class.java))
        }
    }
}
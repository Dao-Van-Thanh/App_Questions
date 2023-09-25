package com.example.appquestion.Acivity

import android.app.usage.UsageEvents.Event
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.appquestion.Model.Model
import com.example.appquestion.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_screen_difficulty.*
import kotlinx.android.synthetic.main.activity_screen_game.*

class ScreenGame : AppCompatActivity() {
    private lateinit var quizQuestion: Model.QuizQuestion;
    private lateinit var countDownTimer: CountDownTimer;
    private lateinit var context: Context;
    private lateinit var txtTime: TextView;
    private lateinit var txtQuestion: TextView;
    private lateinit var btnAnswer1: Button;
    private lateinit var btnAnswer2: Button;
    private lateinit var btnAnswer3: Button;
    private lateinit var btnAnswer4: Button;
    private lateinit var txtQuestionLocation: TextView;
    private lateinit var imageView:ImageView;
    private lateinit var listMemeTrue: ArrayList<Int>;
    private lateinit var listMemeFalse:ArrayList<Int>;

    private var NUMBER_OF_CORRECT_ANSWERS = 0;
    private var NUMBER_OF_INCORRECT_ANSWERS = 0;
    private var INDEX_QUESTION = 0
    private lateinit var CORRECT_ANSWER: String;
    private var colorY = "#00FF00"
    private var colorN = "#FF3333"
    private var colorDF = "#FFFF33"


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_game)

        Reflection()
        val quizQuestionJson = intent.getStringExtra("QuizQuestionJson")
        val gson = Gson()
        quizQuestion = gson.fromJson(quizQuestionJson, Model.QuizQuestion::class.java)
        when (quizQuestion.response_code) {
            0 -> {
                StartCountDownTimer()// bắt đầu đếm ngược
                GetTheCorrectAnswer(INDEX_QUESTION) // lấy đáp án đúng
                LoadQuestion(INDEX_QUESTION) // load câu hỏi
                EventChooseAnswer()// sự kiện người dùng chọn đáp án
            }
            1 -> txtQuestion.setText("No Results")
            2 -> txtQuestion.setText("Invalid Parameter")
            3 -> txtQuestion.setText("Token Not Found")
            4 -> txtQuestion.setText("Token Empty")
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun EventBtn(btn: Button) {
        val handler = Handler()
        blockingView.visibility = View.VISIBLE // chặn tương tác với màn hình
        if (INDEX_QUESTION < 9) {
            handler.postDelayed({
                btn.backgroundTintList = ChangeBackgroundTint(colorDF)
                GetTheCorrectAnswer(INDEX_QUESTION) // lấy đáp án đúng
                SetPositionTheQuestion(INDEX_QUESTION) // set vị trí câu hỏi VD: 4/10
                imageView.setImageResource(R.drawable.yooo)
                LoadQuestion(INDEX_QUESTION) // thay đổi câu hỏi
                blockingView.visibility = View.GONE // cho phép tương tác với màn hình
            }, 3000) //  3000 mili giây (3 giây) thực hiện sau 3 giây (3000)
            INDEX_QUESTION++
        } else {
            StopCountDownTimer();
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun EventChooseAnswer() {
        btnAnswer1.setOnClickListener {
            if (btnAnswer1.text == CORRECT_ANSWER) {
                NUMBER_OF_CORRECT_ANSWERS++
                btnAnswer1.backgroundTintList = ChangeBackgroundTint(colorY)
                imageView.setImageResource(listMemeTrue[INDEX_QUESTION])
                EventBtn(btnAnswer1)
            } else {
                NUMBER_OF_INCORRECT_ANSWERS++
                btnAnswer1.backgroundTintList = ChangeBackgroundTint(colorN)
                imageView.setImageResource(listMemeFalse[INDEX_QUESTION])
                EventBtn(btnAnswer1)
            }
        }
        btnAnswer2.setOnClickListener {

            if (btnAnswer2.text == CORRECT_ANSWER) {
                NUMBER_OF_CORRECT_ANSWERS++
                btnAnswer2.backgroundTintList = ChangeBackgroundTint(colorY)
                imageView.setImageResource(listMemeTrue[INDEX_QUESTION])
                EventBtn(btnAnswer2)
            } else {
                NUMBER_OF_INCORRECT_ANSWERS++
                btnAnswer2.backgroundTintList = ChangeBackgroundTint(colorN)
                imageView.setImageResource(listMemeFalse[INDEX_QUESTION])
                EventBtn(btnAnswer2)
            }
        }
        btnAnswer3.setOnClickListener {
            if (btnAnswer3.text == CORRECT_ANSWER) {
                NUMBER_OF_CORRECT_ANSWERS++
                btnAnswer3.backgroundTintList = ChangeBackgroundTint(colorY)
                imageView.setImageResource(listMemeTrue[INDEX_QUESTION])
                EventBtn(btnAnswer3)
            } else {
                NUMBER_OF_INCORRECT_ANSWERS++
                btnAnswer3.backgroundTintList = ChangeBackgroundTint(colorN)
                imageView.setImageResource(listMemeFalse[INDEX_QUESTION])
                EventBtn(btnAnswer3)
            }
        }
        btnAnswer4.setOnClickListener {
            if (btnAnswer4.text == CORRECT_ANSWER) {
                NUMBER_OF_CORRECT_ANSWERS++
                btnAnswer4.backgroundTintList = ChangeBackgroundTint(colorY)
                imageView.setImageResource(listMemeTrue[INDEX_QUESTION])
                EventBtn(btnAnswer4)
            } else {
                NUMBER_OF_INCORRECT_ANSWERS++
                btnAnswer4.backgroundTintList = ChangeBackgroundTint(colorN)
                imageView.setImageResource(listMemeFalse[INDEX_QUESTION])
                EventBtn(btnAnswer4)
            }
        }
    }

    private fun ChangeBackgroundTint(color: String): ColorStateList {
        val color = Color.parseColor(color)
        return ColorStateList.valueOf(color)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun LoadQuestion(i: Int) {
        val encodedString = quizQuestion.results.get(i).question
        val decodedString = Html.fromHtml(encodedString, Html.FROM_HTML_MODE_LEGACY).toString()
        txtQuestion.setText(decodedString);
        println(quizQuestion.results.get(i))
        var listAnswers = arrayListOf<String>(
            quizQuestion.results.get(i).correct_answer,
            quizQuestion.results.get(i).incorrect_answers[0],
            quizQuestion.results.get(i).incorrect_answers[1],
            quizQuestion.results.get(i).incorrect_answers[2]
        )
        listAnswers.shuffle();
        for (i in listAnswers.indices) {
            when {
                i == 0 -> btnAnswer1.setText(listAnswers.get(i))
                i == 1 -> btnAnswer2.setText(listAnswers.get(i))
                i == 2 -> btnAnswer3.setText(listAnswers.get(i))
                i == 3 -> btnAnswer4.setText(listAnswers.get(i))
            }
        }
    }


    private fun Reflection() {
        context = this
        txtTime = activity_screen_game_countdown_timer
        txtQuestionLocation = activity_screen_game_txt_question_location;
        txtQuestion = activity_screen_game_question
        btnAnswer1 = activity_screen_game_btn_answer1
        btnAnswer2 = activity_screen_game_btn_answer2
        btnAnswer3 = activity_screen_game_btn_answer3
        btnAnswer4 = activity_screen_game_btn_answer4
        imageView = activity_screen_game_img_view

        listMemeTrue = ArrayList<Int>();
        listMemeTrue.add(R.drawable.yea)
        listMemeTrue.add(R.drawable.yea1)
        listMemeTrue.add(R.drawable.yea2)
        listMemeTrue.add(R.drawable.yea2)
        listMemeTrue.add(R.drawable.yea3)
        listMemeTrue.add(R.drawable.yea4)
        listMemeTrue.add(R.drawable.yea4)
        listMemeTrue.add(R.drawable.yea5)
        listMemeTrue.add(R.drawable.yea6)
        listMemeTrue.add(R.drawable.yea7)

        listMemeFalse = ArrayList<Int>();
        listMemeFalse.add(R.drawable.no)
        listMemeFalse.add(R.drawable.no1)
        listMemeFalse.add(R.drawable.no2)
        listMemeFalse.add(R.drawable.no2)
        listMemeFalse.add(R.drawable.no3)
        listMemeFalse.add(R.drawable.no4)
        listMemeFalse.add(R.drawable.no5)
        listMemeFalse.add(R.drawable.no6)
        listMemeFalse.add(R.drawable.no8)
        listMemeFalse.add(R.drawable.no9)

        listMemeTrue.shuffle()
        listMemeFalse.shuffle()

    }

    private fun GetTheCorrectAnswer(i: Int) {
        CORRECT_ANSWER = quizQuestion.results.get(i).correct_answer;
    }

    private fun SetPositionTheQuestion(i: Int) {
        txtQuestionLocation.setText("${i + 1}/10")
    }

    private fun StartCountDownTimer() {
        val totalTimeInMillis = 10 * 60 * 1000 // 10 phút (10 * 60 * 1000 mili giây)
        countDownTimer = object : CountDownTimer(totalTimeInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Được gọi sau mỗi giây
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                txtTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Được gọi khi bộ đếm giờ kết thúc
                blockingView.visibility = View.VISIBLE
                txtTime.text = "00:00" // Hoặc thực hiện hành động khi đếm ngược kết thúc
            }
        }
        countDownTimer?.start() // Bắt đầu đếm ngược
    }

    private fun StopCountDownTimer() {
        countDownTimer?.cancel();
    }

}
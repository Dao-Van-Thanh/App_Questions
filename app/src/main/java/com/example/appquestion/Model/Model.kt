package com.example.appquestion.Model

import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.http.GET

class Model {
    data class QuizQuestion(
        var response_code: Int,
        var results: List<Question>
    ):java.io.Serializable

    data class Question(
        var category: String,
        var type: String,
        var difficulty: String,
        var question: String,
        var correct_answer: String,
        var incorrect_answers: List<String>
    ){
        fun decodeHtmlEntities() {
            // Giải mã mã HTML entities trong câu hỏi và câu trả lời
            question = decodeHtmlEntities(question)
            correct_answer = decodeHtmlEntities(correct_answer)
            incorrect_answers = incorrect_answers.map { decodeHtmlEntities(it) }
        }

        private fun decodeHtmlEntities(input: String): String {
            return Jsoup.parse(input).text()
        }
    }
}
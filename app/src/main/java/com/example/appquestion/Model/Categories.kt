package com.example.appquestion.Model

class Categories {
    data class trivia(
        var trivia_categories:List<trivia_categories>
    )
    data class trivia_categories(
        var id:Int,
        var name:String
    ):java.io.Serializable
}
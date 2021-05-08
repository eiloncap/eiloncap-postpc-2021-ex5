package exercise.android.reemh.todo_items

import java.io.Serializable

data class TodoItem(
    val todoItem: String = "",
    var isDone: Boolean = false
) : Serializable
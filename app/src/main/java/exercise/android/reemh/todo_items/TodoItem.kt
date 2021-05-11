package exercise.android.reemh.todo_items

import java.io.Serializable
import java.time.LocalDateTime

data class TodoItem(
    val description: String = "",
    var isDone: Boolean = false,
    var deleteMode: Boolean = false,
    val creationDateTime: LocalDateTime = LocalDateTime.now()
) : Serializable, Comparable<TodoItem> {

    override fun compareTo(other: TodoItem): Int {
        if (this === other) {
            return 0
        }
        val res = other.creationDateTime.compareTo(this.creationDateTime)
        return if (res == 0) -1 else res
    }
}
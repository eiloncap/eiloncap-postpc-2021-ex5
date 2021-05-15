package exercise.android.reemh.todo_items

import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class TodoItem(
    var description: String,
    var isDone: Boolean = false,
    var deleteMode: Boolean = false,
    private val creationDT: Date = Date()
) : Serializable, Comparable<TodoItem> {

    private var lastModified: Date = creationDT

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }

    override fun compareTo(other: TodoItem): Int {
        if (this === other) {
            return 0
        }
        val res = other.creationDT.compareTo(this.creationDT)
        return if (res == 0) -1 else res
    }

    fun getCreationDateTimeStr(delimiter: String = "\n"): String {
        val lclDateTime = getCreationDateTime()
        return DATE_FORMATTER.format(lclDateTime) + delimiter + TIME_FORMATTER.format(lclDateTime)
    }

    fun getModificationDateStr(): String {
        val lclDate = getModificationDateTime()
        return DATE_FORMATTER.format(lclDate)
    }

    fun getModificationTimeStr(): String {
        val lclTime = getModificationDateTime()
        return TIME_FORMATTER.format(lclTime)
    }

    fun markModified() {
        lastModified = Date()
    }

    fun update(item: TodoItem) {
        description = item.description
        isDone = item.isDone
        lastModified = item.lastModified
    }

    fun getModificationDateTime(): LocalDateTime {
        return lastModified.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun getCreationDateTime(): LocalDateTime {
        return creationDT.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}
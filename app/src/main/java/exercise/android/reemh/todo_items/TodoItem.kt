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

    // todo: convert to string individually?

    var id: String? = null
    private var lastModified: Date = creationDT

    companion object {
        private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }

    override fun compareTo(other: TodoItem): Int {
        if (this == other) {
            return 0
        }
        val res = other.getCreationDateTime().compareTo(this.getCreationDateTime())
        return if (res == 0) -1 else res
    }

    override fun equals(other: Any?): Boolean {
        return other is TodoItem && this.id == other.id
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

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
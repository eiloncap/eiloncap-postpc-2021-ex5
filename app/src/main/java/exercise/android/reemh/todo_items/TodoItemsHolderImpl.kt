package exercise.android.reemh.todo_items

import java.time.LocalDateTime
import java.util.*

class TodoItemsHolderImpl : TodoItemsHolder {

    private val doneItems = TreeSet<TodoItem>()
    private val inProgressItems = TreeSet<TodoItem>()

    override fun getCurrentItems(): List<TodoItem> {
        return inProgressItems.toList() + doneItems.toList()
    }

    override fun addNewInProgressItem(description: String) {
        inProgressItems.add(TodoItem(description = description))
    }

    override fun markItemDone(item: TodoItem) {
        item.isDone = true
        if (inProgressItems.contains(item)) {
            inProgressItems.remove(item)
            doneItems.add(item)
        }
    }

    override fun markItemInProgress(item: TodoItem) {
        item.isDone = false
        if (doneItems.contains(item)) {
            doneItems.remove(item)
            inProgressItems.add(item)
        }
    }

    override fun deleteItem(item: TodoItem) {
        if (doneItems.contains(item)) {
            doneItems.remove(item)
        } else if (inProgressItems.contains(item)) {
            inProgressItems.remove(item)
        }
    }
}


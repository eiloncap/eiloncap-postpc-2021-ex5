package exercise.android.reemh.todo_items

import java.util.*
import kotlin.collections.ArrayList

class TodoItemsHolderImpl : TodoItemsHolder {


//    private val doneItems = mutableListOf<TodoItem>()
//    private val inProgressItems = mutableListOf<TodoItem>()
    private val doneItems = TreeSet<TodoItem>()
    private val inProgressItems = TreeSet<TodoItem>()

    override fun getCurrentItems(): List<TodoItem> {
        return ArrayList(inProgressItems + doneItems)
    }

    override fun addNewInProgressItem(description: String) {
        this.inProgressItems.add(TodoItem(description = description, isDone = false))
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


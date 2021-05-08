package exercise.android.reemh.todo_items

class TodoItemsHolderImpl : TodoItemsHolder {
    override val currentItems = mutableListOf<TodoItem>()
        get() = field.toMutableList()

    override fun addNewInProgressItem(description: String) {
        currentItems += TodoItem(todoItem = description, isDone = false)
    }

    override fun markItemDone(item: TodoItem) {
        item.isDone = true
    }

    override fun markItemInProgress(item: TodoItem) {
        item.isDone = false
    }

    override fun deleteItem(item: TodoItem) {
        currentItems.remove(item)
    }
}
package exercise.android.reemh.todo_items

class TodoItemsHolderImpl : TodoItemsHolder {


    private val _currentItems = mutableListOf<TodoItem>()
    private var isDone = false

    override fun getCurrentItems(): List<TodoItem> {
        return ArrayList(_currentItems)
    }

    override fun addNewInProgressItem(description: String) {
        this._currentItems += TodoItem(todoItem = description, isDone = false)
    }

    override fun markItemDone(item: TodoItem) {
        item.isDone = true
    }

    override fun markItemInProgress(item: TodoItem) {
        item.isDone = false
    }

    override fun deleteItem(item: TodoItem) {
        _currentItems.remove(item)
    }
}


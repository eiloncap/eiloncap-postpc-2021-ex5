package exercise.android.reemh.todo_items

import androidx.lifecycle.LiveData
import java.io.Serializable

interface TodoItemsHolder : Serializable {

    val ld: LiveData<List<TodoItem>>

    /** Get a copy of the current items list  */
    fun getCurrentItems(): List<TodoItem>

    /** Get an item by its id  */
    fun getItem(id: String): TodoItem

    /**
     * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
     * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
     */
    fun addNewInProgressItem(description: String)

    /** mark the @param item as DONE  */
    fun markItemDone(item: TodoItem)

    /** mark the @param item as IN-PROGRESS  */
    fun markItemInProgress(item: TodoItem)

    /** delete the @param item  */
    fun editItemDescription(item: TodoItem, description: String)

    /** delete the @param item  */
    fun deleteItem(item: TodoItem)
}
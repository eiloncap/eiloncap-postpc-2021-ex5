package exercise.android.reemh.todo_items

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TodoItemsHolderImpl(context: Context) : TodoItemsHolder {

    companion object {
        private const val SP_IN_PROGRESS_TREE: String = "sp_in_progress"
        private const val SP_DONE_TREE: String = "sp_done"
    }

    private val inProgressItems: TreeSet<TodoItem> = TreeSet<TodoItem>()
    private val doneItems: TreeSet<TodoItem> = TreeSet<TodoItem>()
    private val inProgressSp: SharedPreferences =
        context.getSharedPreferences(SP_IN_PROGRESS_TREE, Context.MODE_PRIVATE)
    private val doneSp: SharedPreferences =
        context.getSharedPreferences(SP_DONE_TREE, Context.MODE_PRIVATE)
    private val mld: MutableLiveData<List<TodoItem>> = MutableLiveData()
    override val ld: LiveData<List<TodoItem>> = mld

    init {
        val gson = Gson()
        for (ser in inProgressSp.all) {
            inProgressItems.add(gson.fromJson(ser.value as String, TodoItem::class.java))
        }
        for (ser in doneSp.all) {
            doneItems.add(gson.fromJson(ser.value as String, TodoItem::class.java))
        }
        mld.value = ArrayList(getCurrentItems())
    }

    override fun getItem(id: String): TodoItem{
        for (item in inProgressItems + doneItems) {
            if (item.id == id) {
                return item
            }
        }
        throw Exception("no item with id $id")
    }

    override fun getCurrentItems(): ArrayList<TodoItem> {
        val toReturn = ArrayList(inProgressItems)
        toReturn.addAll(doneItems)
        return toReturn
    }

    override fun addNewInProgressItem(description: String) = informChanges {
        val item = TodoItem(description = description)
        item.id = UUID.randomUUID().toString()
        inProgressItems.add(item)
        inProgressSp.edit().putString(item.id, Gson().toJson(item)).apply()
    }

    override fun markItemDone(item: TodoItem) = informChanges {
        item.isDone = true
        if (inProgressItems.contains(item)) {
            inProgressItems.remove(item)
            doneItems.add(item)
        }
        inProgressSp.edit().remove(item.id).apply()
        doneSp.edit().putString(item.id, Gson().toJson(item)).apply()
    }

    override fun markItemInProgress(item: TodoItem) = informChanges {
        item.isDone = false
        if (doneItems.contains(item)) {
            doneItems.remove(item)
            inProgressItems.add(item)
        }
        doneSp.edit().remove(item.id).apply()
        inProgressSp.edit().putString(item.id, Gson().toJson(item)).apply()
    }

    override fun editItemDescription(item: TodoItem, description: String) = informChanges {
        item.description = description
        item.markModified()
        if (doneItems.contains(item)) {
            doneSp.edit().putString(item.id, Gson().toJson(item)).apply()
        } else if (inProgressItems.contains(item)) {
            inProgressSp.edit().putString(item.id, Gson().toJson(item)).apply()
        }
    }

    override fun deleteItem(item: TodoItem) = informChanges {
        if (doneItems.contains(item)) {
            doneItems.remove(item)
            doneSp.edit().remove(item.id).apply()
        } else if (inProgressItems.contains(item)) {
            inProgressItems.remove(item)
            inProgressSp.edit().remove(item.id).apply()
        }
    }

    private fun informChanges(f: () -> Unit) {
        f()
        mld.value = ArrayList(getCurrentItems())
    }
}
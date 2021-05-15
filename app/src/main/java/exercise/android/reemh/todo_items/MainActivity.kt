package exercise.android.reemh.todo_items

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SP_TODO_LIST: String = "spTodoListHolder"
        private const val HOLDER_STATE: String = "holder"
        private const val INPUT_STATE: String = "input"
    }

    var holder: TodoItemsHolder? = null
    private var onEditItem: TodoItem? = null
    private lateinit var broadcastReceiverForItemModification: BroadcastReceiver
    private lateinit var adapter: TodoItemAdapter
    private lateinit var recyclerTodoItemsList: RecyclerView
    private lateinit var editTextInsertTask: EditText
    private lateinit var buttonCreateTodoItem: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (holder == null) {
            holder = TodoItemsHolderImpl()

            val holderJson = getPreferences(Context.MODE_PRIVATE).getString(SP_TODO_LIST, null)
            holder = if (holderJson == null) {
                TodoItemsHolderImpl()
            } else {
                Gson().fromJson(holderJson, TodoItemsHolderImpl::class.java) as TodoItemsHolder
            }
        }

        recyclerTodoItemsList = findViewById(R.id.recyclerTodoItemsList)
        editTextInsertTask = findViewById(R.id.editTextInsertTask)
        buttonCreateTodoItem = findViewById(R.id.buttonCreateTodoItem)
        recyclerTodoItemsList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = TodoItemAdapter()
        adapter.onItemClickCallback = { item ->
            startActivity(
                Intent(this@MainActivity, TodoItemEditActivity::class.java)
                    .putExtra("item", item)
            )
            onEditItem = item
        }
        adapter.onItemCheckCallback = { item ->
            if (!item.isDone) {
                holder!!.markItemDone(item)
            } else {
                holder!!.markItemInProgress(item)
            }
            adapter.setTasks(holder!!.getCurrentItems())
        }
        adapter.onDeleteCallback = { item ->
            holder!!.deleteItem(item)
            adapter.setTasks(holder!!.getCurrentItems())
        }
        adapter.setTasks(holder!!.getCurrentItems())
        recyclerTodoItemsList.adapter = adapter

        buttonCreateTodoItem.setOnClickListener {
            if (editTextInsertTask.text.isNotEmpty()) {
                holder!!.addNewInProgressItem(editTextInsertTask.text.toString())
                editTextInsertTask.text.clear()
                adapter.setTasks(holder!!.getCurrentItems())
            }
        }

        // register a broadcast-receiver to handle item modification
        broadcastReceiverForItemModification = object : BroadcastReceiver() {
            override fun onReceive(context: Context, incomingIntent: Intent) {
                if (incomingIntent.action != "item_modified" || onEditItem == null) return
                onEditItem!!.update(incomingIntent.getSerializableExtra("returned_item") as TodoItem)
                adapter.notifyDataSetChanged()
            }
        }
        registerReceiver(broadcastReceiverForItemModification, IntentFilter("item_modified"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(HOLDER_STATE, holder)
        outState.putString(INPUT_STATE, editTextInsertTask.text.toString())

        val holderAsJson: String = Gson().toJson(holder)
        getPreferences(Context.MODE_PRIVATE).edit().putString(SP_TODO_LIST, holderAsJson).apply()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        holder = savedInstanceState.getSerializable(HOLDER_STATE) as TodoItemsHolder
        editTextInsertTask.setText(savedInstanceState.getString(INPUT_STATE) as String)
        adapter.setTasks(holder!!.getCurrentItems())

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiverForItemModification)
    }
}
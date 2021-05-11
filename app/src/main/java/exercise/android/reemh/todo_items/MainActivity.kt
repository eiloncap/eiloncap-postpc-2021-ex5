package exercise.android.reemh.todo_items

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    lateinit var holder: TodoItemsHolder
    private lateinit var adapter: TodoItemAdapter
    private lateinit var recyclerTodoItemsList: RecyclerView
    private lateinit var editTextInsertTask: EditText
    private lateinit var buttonCreateTodoItem: FloatingActionButton
    private val HOLDER_STATE: String = "holder"
    private val INPUT_STATE: String = "input"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (holder == null) {
//            holder = TodoItemsHolderImpl()
//        }

        // TODO: do something else to check that not initialized
        if (!this::holder.isInitialized) {
            holder = TodoItemsHolderImpl()
        }

        recyclerTodoItemsList = findViewById(R.id.recyclerTodoItemsList)
        editTextInsertTask = findViewById(R.id.editTextInsertTask)
        buttonCreateTodoItem = findViewById(R.id.buttonCreateTodoItem)
        recyclerTodoItemsList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = TodoItemAdapter()
        adapter.onItemClickCallback = { item, isChecked ->
            if (!item.isDone) {
                holder.markItemDone(item)
            } else {
                holder.markItemInProgress(item)
            }
            adapter.setTasks(holder.getCurrentItems())
        }
        adapter.onDeleteCallback = { item ->
            holder.deleteItem(item)
            adapter.setTasks(holder.getCurrentItems())
        }
        recyclerTodoItemsList.adapter = adapter

        buttonCreateTodoItem.setOnClickListener {
            if (editTextInsertTask.text.isNotEmpty()) {
                holder.addNewInProgressItem(editTextInsertTask.text.toString())
                editTextInsertTask.text.clear()
                adapter.setTasks(holder.getCurrentItems())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(HOLDER_STATE, holder)
        outState.putString(INPUT_STATE, editTextInsertTask.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        holder = savedInstanceState.getSerializable(HOLDER_STATE) as TodoItemsHolder
        editTextInsertTask.setText(savedInstanceState.getString(INPUT_STATE) as String)
        adapter.setTasks(holder.getCurrentItems())

    }
}

// TODO: 1) problem with tests after converting to kotlin
//       2) is TodoItemsHolder is really a holder?
//       2) getter doesn't work with copy

package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        private const val INPUT_STATE: String = "input_state"
    }

    var holder: TodoItemsHolder? = null
    private lateinit var adapter: TodoItemAdapter
    private lateinit var recyclerTodoItemsList: RecyclerView
    private lateinit var editTextInsertTask: EditText
    private lateinit var buttonCreateTodoItem: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (holder == null) {
            holder = TodoApp.instance.holder
        }
        recyclerTodoItemsList = findViewById(R.id.recyclerTodoItemsList)
        editTextInsertTask = findViewById(R.id.editTextInsertTask)
        buttonCreateTodoItem = findViewById(R.id.buttonCreateTodoItem)
        recyclerTodoItemsList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        initAdapter()
        holder?.ld!!.observe(this, { list -> adapter.setTasks(list) })

        buttonCreateTodoItem.setOnClickListener {
            if (editTextInsertTask.text.isNotEmpty()) {
                holder!!.addNewInProgressItem(editTextInsertTask.text.toString())
                editTextInsertTask.text.clear()
            }
        }
    }

    private fun initAdapter() {
        adapter = TodoItemAdapter()
        adapter.onItemClickCallback = { item ->
            startActivity(
                Intent(this@MainActivity, EditTodoItemActivity::class.java)
                    .putExtra("item_id", item.id)
            )
        }
        adapter.onItemCheckCallback = { item ->
            if (!item.isDone) holder?.markItemDone(item) else holder?.markItemInProgress(item)
        }
        adapter.onDeleteCallback = { item -> holder?.deleteItem(item) }
        adapter.setTasks(holder!!.getCurrentItems())
        recyclerTodoItemsList.adapter = adapter
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_STATE, editTextInsertTask.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextInsertTask.setText(savedInstanceState.getString(INPUT_STATE) as String)
        adapter.setTasks(holder!!.getCurrentItems())

    }
}
package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TodoItemAdapter :
    RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    var onItemClickCallback: ((TodoItem, Boolean) -> Unit)? = null
    var onDeleteCallback: ((TodoItem) -> Unit)? = null
    private val tasks: MutableList<TodoItem> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    fun setTasks(newTasksList: List<TodoItem>) {
        tasks.clear()
        tasks.addAll(newTasksList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = tasks[position]
        val view = holder.itemView
        val checkBox = holder.checkBox
        val deleteButton = holder.deleteButton
        val bgColor = if (position % 2 == 0)
            ContextCompat.getColor(view.context, R.color.white)
        else ContextCompat.getColor(view.context, R.color.blue_50)
        view.setBackgroundColor(bgColor)
        checkBox.setOnCheckedChangeListener(null)
        checkBox.text = todoItem.description
        checkBox.isChecked = todoItem.isDone
        if (todoItem.isDone) {
            checkBox.paintFlags =
                checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            checkBox.paintFlags =
                checkBox.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
        if (todoItem.deleteMode) {
            deleteButton.visibility = View.VISIBLE
        } else {
            deleteButton.visibility = View.GONE
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            val callback = onItemClickCallback ?: return@setOnCheckedChangeListener
            callback(todoItem, isChecked)
        }
        deleteButton.setOnClickListener {
            val callback = onDeleteCallback ?: return@setOnClickListener
            callback(todoItem)
        }
        checkBox.setOnLongClickListener(View.OnLongClickListener { v ->
            if (deleteButton.visibility == View.GONE) {
                deleteButton.alpha = 0f
                deleteButton.visibility = View.VISIBLE
                deleteButton.animate().alpha(1f).start()
                todoItem.deleteMode = true
            } else {
                deleteButton.animate().alpha(0f).start()
                deleteButton.visibility = View.GONE
                todoItem.deleteMode = false
            }
            return@OnLongClickListener true
        })
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
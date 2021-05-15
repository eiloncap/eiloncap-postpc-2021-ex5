package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class TodoItemAdapter :
    RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    var onItemCheckCallback: ((TodoItem) -> Unit)? = null
    var onItemClickCallback: ((TodoItem) -> Unit)? = null
    var onDeleteCallback: ((TodoItem) -> Unit)? = null
    private val tasks: MutableList<TodoItem> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val description: TextView = view.findViewById(R.id.description)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val textViewDateTime: TextView = view.findViewById(R.id.textViewDateTime)
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
        val description = holder.description
        val bgColor = if (position % 2 == 0)
            ContextCompat.getColor(view.context, R.color.white)
        else ContextCompat.getColor(view.context, R.color.blue_50)
        view.setBackgroundColor(bgColor)

        holder.textViewDateTime.text = todoItem.getCreationDateTimeStr()

        checkBox.setOnCheckedChangeListener(null)
        description.text = todoItem.description
        checkBox.isChecked = todoItem.isDone
        description.paintFlags =
            if (todoItem.isDone) description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else description.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
        deleteButton.visibility = if (todoItem.deleteMode) View.VISIBLE else View.GONE

        checkBox.setOnCheckedChangeListener { _, _ ->
            val callback = onItemCheckCallback ?: return@setOnCheckedChangeListener
            callback(todoItem)
        }
        deleteButton.setOnClickListener {
            val callback = onDeleteCallback ?: return@setOnClickListener
            callback(todoItem)
        }
        description.setOnClickListener {
            val callback = onItemClickCallback ?: return@setOnClickListener
            callback(todoItem)
        }
        description.setOnLongClickListener {
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
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
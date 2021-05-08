package exercise.android.reemh.todo_items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TodoItemAdapter(tasks: MutableList<TodoItem>) :
    RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    var onItemClickCallback: ((TodoItem, Boolean) -> Unit)? = null
    private val _tasks: MutableList<TodoItem> = tasks

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
//        val bg: ConstraintLayout = view.findViewById(R.id.bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = _tasks[position]
        val view = holder.itemView
        view.setBackgroundColor(
            if (position % 2 == 0)
                ContextCompat.getColor(view.context, R.color.white)
            else ContextCompat.getColor(view.context, R.color.blue_50)
        )
        holder.checkBox.text = todoItem.todoItem
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val callback = onItemClickCallback ?: return@setOnCheckedChangeListener
            callback(todoItem, isChecked)
            // TODO: change tasks orientation
        }
    }

    override fun getItemCount(): Int {
        return _tasks.size
    }
}
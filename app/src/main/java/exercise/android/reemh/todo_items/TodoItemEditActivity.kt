package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class TodoItemEditActivity : AppCompatActivity() {

    private lateinit var item: TodoItem
    private lateinit var creationDate: TextView
    private lateinit var modificationDate: TextView
    private lateinit var description: EditText
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)

        creationDate = findViewById(R.id.creationDate)
        modificationDate = findViewById(R.id.modificationDate)
        description = findViewById(R.id.descriptionEditText)
        checkBox = findViewById(R.id.checkBox)

        item = (if (savedInstanceState?.containsKey("item_in_edit") == true)
            savedInstanceState.getSerializable("item_in_edit")
        else intent.getSerializableExtra("item")) as TodoItem

        creationDate.text = item.getCreationDateTimeStr(delimiter = " at ")
        description.setText(item.description)
        checkBox.isChecked = item.isDone
        modificationDate.text = updateModificationDate()

        description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                item.description = description.text.toString()
                item.markModified()
                modificationDate.text = updateModificationDate()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isDone = isChecked
        }
    }

    fun updateModificationDate(): String {
        val modified: LocalDateTime = item.getModificationDateTime()
        val now: LocalDateTime = LocalDateTime.now()
        if (now.toLocalDate() != modified.toLocalDate()) {
            return "${item.getModificationDateStr()} at ${item.getModificationTimeStr()}"
        } else if (ChronoUnit.HOURS.between(modified, now) >= 1) {
            return "Today at ${item.getModificationTimeStr()}"
        }
        return "${ChronoUnit.MINUTES.between(modified, now)} minutes ago"
    }

    override fun onPause() {
        super.onPause()
        val broadcast = Intent()
            .setAction("item_modified")
            .putExtra("returned_item", item)
        this.sendBroadcast(broadcast)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("item_in_edit", item)
    }
}
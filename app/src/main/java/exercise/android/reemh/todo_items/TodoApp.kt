package exercise.android.reemh.todo_items

import android.app.Application


class TodoApp : Application() {

    companion object {
        lateinit var instance: TodoApp
            private set
    }

    var holder: TodoItemsHolder? = null
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this
        holder = TodoItemsHolderImpl(this)
    }
}
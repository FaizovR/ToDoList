package ru.faizovr.todo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.faizovr.todo.R
import ru.faizovr.todo.presentation.model.TaskDataView
import ru.faizovr.todo.presentation.viewholder.TaskViewHolder

class ToDoTaskAdapter(
        private val onEditButtonClickListener: (position: Int) -> Unit,
        private val onCheckBoxClickListener: (position: Int) -> Unit,
        private val onTaskClickListener: (position: Int) -> Unit
) :
        RecyclerView.Adapter<TaskViewHolder>() {

    private var taskList: List<TaskDataView> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_view_holder, parent, false)
        return TaskViewHolder(view)
    }

    fun updateList(newList: List<TaskDataView>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ListDiffUtilCallback(taskList, newList))
        taskList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int =
            taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int): Unit =
            holder.bind(taskList[position], onEditButtonClickListener, onCheckBoxClickListener, onTaskClickListener)
}
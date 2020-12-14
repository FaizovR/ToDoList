package ru.faizovr.todo.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_view_holder.view.*
import ru.faizovr.todo.presentation.model.TaskDataView

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private fun setMessage(message: String) {
        itemView.text_task.text = message
    }

    private fun setState(isChecked: Boolean) {
        itemView.checkbox_is_complete.isChecked = isChecked
    }

    private fun setPaintFlags(paintFlags: Int) {
        itemView.text_task.paintFlags = paintFlags
    }

    private fun setImage(imageId: Int) {
        itemView.button_edit_task.setImageResource(imageId)
    }

    private fun setOnClickListeners(
            onEditButtonClickListener: (position: Int) -> Unit,
            onCheckBoxClickListener: (position: Int) -> Unit,
            onTaskClickListener: (position: Int) -> Unit
    ) {
        itemView.button_edit_task.setOnClickListener {
            onEditButtonClickListener(adapterPosition)
        }
        itemView.checkbox_is_complete.setOnClickListener {
            onCheckBoxClickListener(adapterPosition)
        }
        itemView.setOnClickListener {
            onTaskClickListener(adapterPosition)
        }
    }

    fun bind(
            task: TaskDataView,
            onEditButtonClickListener: (position: Int) -> Unit,
            onCheckBoxClickListener: (position: Int) -> Unit,
            onTaskClickListener: (position: Int) -> Unit
    ) {
        setState(task.isCheckBoxActive)
        setPaintFlags(task.paintFlags)
        setMessage(task.message)
        setImage(task.editButtonImageId)
        setOnClickListeners(onEditButtonClickListener, onCheckBoxClickListener, onTaskClickListener)
    }
}

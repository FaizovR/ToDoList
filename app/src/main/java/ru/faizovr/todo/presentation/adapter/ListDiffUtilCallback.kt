package ru.faizovr.todo.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.faizovr.todo.presentation.model.TaskDataView

class ListDiffUtilCallback(private val oldList: List<TaskDataView>, private val newList: List<TaskDataView>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = when {
        oldList[oldItemPosition].isCheckBoxActive != newList[newItemPosition].isCheckBoxActive -> false
        oldList[oldItemPosition].message != newList[newItemPosition].message -> false
        oldList[oldItemPosition].editButtonImageId != newList[newItemPosition].editButtonImageId -> false
        oldList[oldItemPosition].paintFlags != newList[newItemPosition].paintFlags -> false
        else -> true
    }
}
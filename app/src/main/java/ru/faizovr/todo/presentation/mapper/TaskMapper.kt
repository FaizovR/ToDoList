package ru.faizovr.todo.presentation.mapper

import android.graphics.Paint
import ru.faizovr.todo.R
import ru.faizovr.todo.domain.model.Task
import ru.faizovr.todo.domain.model.TaskState
import ru.faizovr.todo.presentation.model.TaskDataView

class TaskMapper : EntityMapper<Task, TaskDataView> {
    override fun mapFromEntity(entity: Task): TaskDataView =
            TaskDataView(
                    isCheckBoxActive = setupCheckBox(entity),
                    paintFlags = setupPaintFlags(entity),
                    id = entity.id,
                    message = entity.message,
                    editButtonImageId = setupEditButtonImageId(entity)
            )

    private fun setupPaintFlags(entity: Task): Int =
            if (entity.taskState == TaskState.COMPLETE) {
                Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                0
            }

    private fun setupEditButtonImageId(entity: Task) =
            when (entity.taskState) {
                TaskState.DEFAULT -> R.drawable.ic_round_inactive_32
                TaskState.COMPLETE -> R.drawable.ic_round_inactive_32
                TaskState.EDIT -> R.drawable.ic_round_active_32
            }

    private fun setupCheckBox(entity: Task) =
            when (entity.taskState) {
                TaskState.DEFAULT -> false
                TaskState.COMPLETE -> true
                TaskState.EDIT -> false
            }
}
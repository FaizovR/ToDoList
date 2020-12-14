package ru.faizovr.todo.presentation.model

class TaskDataView(val isCheckBoxActive: Boolean,
                   val paintFlags: Int,
                   val id: Long,
                   val message: String,
                   val editButtonImageId: Int
)
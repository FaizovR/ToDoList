package ru.faizovr.todo.presentation.presenter

import ru.faizovr.todo.domain.model.Model
import ru.faizovr.todo.presentation.contract.TaskContract

class TaskPresenter(
        view: TaskContract.View,
        model: Model,
        taskId: Long?
) : TaskContract.Presenter {

    init {
        if (taskId != null) {
            val task = model.getTaskById(taskId)
            val message = task?.message
            if (message != null) {
                view.showTaskMessage(message)
            }
        }
    }
}
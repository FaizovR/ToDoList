package ru.faizovr.todo.presentation.presenter

import androidx.annotation.VisibleForTesting
import ru.faizovr.todo.domain.model.Model
import ru.faizovr.todo.domain.model.Task
import ru.faizovr.todo.domain.model.TaskState
import ru.faizovr.todo.presentation.InputState
import ru.faizovr.todo.presentation.contract.TaskListContract
import ru.faizovr.todo.presentation.mapper.TaskMapper
import ru.faizovr.todo.presentation.viewholder.TaskDataView

class TaskListPresenter(
        private val view: TaskListContract.View,
        private val model: Model,
        private val taskMapper: TaskMapper = TaskMapper()
) : TaskListContract.Presenter {

    private var addTextString: String = ""
    private var editTextString: String = ""
    private var inputState: InputState = InputState.ADD

    override fun init() {
        view.setFuncToMainButton()
        setupInputState()
        showContent()
    }

    private fun setupInputState() {
        val task = model.getMyList().find { it.taskState == TaskState.EDIT }
        if (task != null) {
            inputState = InputState.EDIT
        }
    }

    private fun deleteTask(position: Int) {
        if (model.getMyList().size > position) {
            model.deleteTask(position)
        }
    }

    override fun listItemMoved(fromPosition: Int, toPosition: Int) {
        model.swapTask(fromPosition, toPosition)
        showContent()
    }

    override fun listItemSwiped(position: Int) {
        if (inputState == InputState.EDIT && position == model.getEditableTaskPosition()) {
            inputState = InputState.ADD
            view.setToDoTaskInputText(addTextString)
        }
        deleteTask(position)
        showContent()
    }

    override fun onMainButtonClicked() {
        if (inputState == InputState.ADD) {
            model.addTask(addTextString)
            addTextString = ""
            view.clearEditText()
        } else {
            model.setTaskMessage(model.getEditableTaskPosition(), editTextString)
            model.setTaskState(model.getEditableTaskPosition(), TaskState.DEFAULT)
            view.setToDoTaskInputText(editTextString)
            inputState = InputState.ADD
        }
        showContent()
    }

    private fun setupButtonLogic() {
        val alpha: Float
        if (inputState == InputState.ADD) {
            view.setMainButtonClickable(addTextString.isNotEmpty())
            alpha = if (addTextString.isNotEmpty()) 1F else 0.5F
            view.setMainButtonAlpha(alpha)
        } else {
            view.setMainButtonClickable(editTextString.isNotEmpty())
            alpha = if (editTextString.isNotEmpty()) 1F else 0.5F
            view.setMainButtonAlpha(alpha)
        }
    }

    private fun changeButtonText() {
        if (inputState == InputState.ADD)
            view.setAddTextToMainButton()
        else
            view.setEditTextToMainButton()
    }

    override fun onEditTaskClickedForPosition(position: Int) {
        if (inputState == InputState.ADD) {
            inputState = InputState.EDIT
            model.setTaskState(position, TaskState.EDIT)
        } else {
            if (model.getTaskFromPosition(position)?.taskState == TaskState.EDIT) {
                inputState = InputState.ADD
                model.setTaskState(position, TaskState.DEFAULT)
            } else {
                model.setTaskState(model.getEditableTaskPosition(), TaskState.DEFAULT)
                model.setTaskState(position, TaskState.EDIT)
            }
        }
        showContent()
    }

    override fun onCheckBoxTaskClickedForPosition(position: Int) {
        val taskAtPosition: Task? = model.getTaskFromPosition(position)
        if (taskAtPosition != null) {
            when (taskAtPosition.taskState) {
                TaskState.DEFAULT -> {
                    model.setTaskState(position, TaskState.COMPLETE)
                }
                TaskState.EDIT -> {
                    inputState = InputState.ADD
                    model.setTaskState(position, TaskState.COMPLETE)
                }
                TaskState.COMPLETE -> {
                    model.setTaskState(position, TaskState.DEFAULT)
                }
            }
            showContent()
        }
    }

    override fun onTaskMessageInputTextChanged(message: String) {
        if (inputState == InputState.ADD) {
            addTextString = message
        } else {
            editTextString = message
        }
        setupButtonLogic()
    }

    override fun onSaveInstanceState() {
        model.saveDataToSharedPreference()
    }

    private fun showList(taskList: List<TaskDataView>) {
        view.setEmptyTextMessageVisibility(false)
        view.setListVisibility(true)
        view.updateList(taskList)
    }

    override fun onTaskClickedForPosition(position: Int) {
        val taskFromPosition = model.getTaskFromPosition(position)
        if (taskFromPosition != null) {
            val taskDataView = taskMapper.mapFromEntity(taskFromPosition)
            view.showTaskFragment(taskDataView)
        }
    }

    private fun showEmptyListText() {
        view.setEmptyTextMessageVisibility(true)
        view.setListVisibility(false)
    }

    private fun updateList() {
        val taskList: List<TaskDataView> = model.getCopyList()
                .map(taskMapper::mapFromEntity)
        if (taskList.isNotEmpty()) {
            showList(taskList)
        } else {
            showEmptyListText()
        }
    }

    private fun setupToDoTaskInputText() {
        if (inputState == InputState.ADD) {
            view.setToDoTaskInputText(addTextString)
        } else {
            view.setToDoTaskInputText(model.getEditableTaskMessage())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun showContent() {
        updateList()
        setupButtonLogic()
        setupToDoTaskInputText()
        changeButtonText()
    }
}
package ru.faizovr.todo.presentation.presenter

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import androidx.annotation.VisibleForTesting
import ru.faizovr.todo.R
import ru.faizovr.todo.domain.model.Model
import ru.faizovr.todo.domain.model.Task
import ru.faizovr.todo.domain.model.TaskState
import ru.faizovr.todo.presentation.InputState
import ru.faizovr.todo.presentation.contract.TaskListContract
import ru.faizovr.todo.presentation.mapper.TaskMapper
import ru.faizovr.todo.presentation.model.TaskDataView

class TaskListPresenter(
        private val view: TaskListContract.View,
        private val model: Model,
        private val resources: Resources,
        private val taskMapper: TaskMapper = TaskMapper()
) : TaskListContract.Presenter {

    private var addTextString: String = ""
    private var editTextString: String = ""
    private var inputState: InputState = InputState.ADD

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setAddTextString(message: String) {
        addTextString = message
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setEditTextString(message: String) {
        editTextString = message
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setInputState(inputState: InputState) {
        this.inputState = inputState
    }

    init {
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
        val isStringNotEmpty: Boolean = if (inputState == InputState.ADD) {
            addTextString.isNotEmpty()
        } else {
            editTextString.isNotEmpty()
        }
        val typedValue = TypedValue()
        if (isStringNotEmpty)
            resources.getValue(R.dimen.alpha_default, typedValue, true)
        else
            resources.getValue(R.dimen.alpha_half, typedValue, true)
        view.setMainButtonClickable(isStringNotEmpty)
        view.setMainButtonAlpha(typedValue.float)
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
            view.showTaskFragment(taskFromPosition.id)
        }
    }

    private fun showEmptyListText() {
        view.setEmptyTextMessageVisibility(true)
        view.setListVisibility(false)
    }

    private fun updateList() {
        val taskList: List<TaskDataView> = model.getList()
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

    private fun showContent() {
        updateList()
        setupButtonLogic()
        setupToDoTaskInputText()
        changeButtonText()
    }

}
package ru.faizovr.todo.presentation.contract

import ru.faizovr.todo.presentation.model.TaskDataView

interface TaskListContract {

    interface View {
        fun setFuncToMainButton()
        fun updateList(taskList: List<TaskDataView>)
        fun setMainButtonClickable(isClickable: Boolean)
        fun setMainButtonAlpha(alpha: Float)
        fun setListVisibility(isVisible: Boolean)
        fun setEmptyTextMessageVisibility(isVisible: Boolean)
        fun setToDoTaskInputText(message: String)
        fun setAddTextToMainButton()
        fun setEditTextToMainButton()
        fun clearEditText()
        fun showTaskFragment(id: Long)
    }

    interface Presenter {
        fun onTaskClickedForPosition(position: Int)
        fun onCheckBoxTaskClickedForPosition(position: Int)
        fun onEditTaskClickedForPosition(position: Int)
        fun listItemMoved(fromPosition: Int, toPosition: Int)
        fun listItemSwiped(position: Int)
        fun onMainButtonClicked()
        fun onTaskMessageInputTextChanged(message: String)
        fun onSaveInstanceState()
    }
}
package ru.faizovr.todo.presentation.presenter

import android.content.res.Resources
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import ru.faizovr.todo.domain.model.Model
import ru.faizovr.todo.domain.model.TaskState
import ru.faizovr.todo.presentation.InputState
import ru.faizovr.todo.presentation.contract.TaskListContract
import ru.faizovr.todo.presentation.mapper.TaskMapper

class PresenterTest {

    private val model: Model = mock()
    private val view: TaskListContract.View = mock()
    private val taskMapper: TaskMapper = mock()
    private val resources: Resources = mock()
    private val taskListPresenter = TaskListPresenter(view, model, resources, taskMapper)

    private fun setAddInputState() {
        taskListPresenter.setInputState(InputState.ADD)
    }

    private fun setEditInputState() {
        taskListPresenter.setInputState(InputState.EDIT)
    }

    private fun setMessageToAddTextString(message: String) {
        taskListPresenter.setAddTextString(message)
    }

    private fun setMessageToEditTextString(message: String) {
        taskListPresenter.setEditTextString(message)
    }

    @Test
    fun `onMainButtonClicked when inputState = Add, and verify addTask called`() {
        setAddInputState()
        setMessageToAddTextString(TEST_MESSAGE)
        taskListPresenter.onMainButtonClicked()
        verify(model).addTask(TEST_MESSAGE)
    }

    @Test
    fun `onMainButtonClicked when inputState = Add, and verify clearEditText called`() {
        setAddInputState()
        setMessageToAddTextString(TEST_MESSAGE)
        taskListPresenter.onMainButtonClicked()
        verify(view).clearEditText()
    }

    @Test
    fun `onMainButtonClicked when inputState = Edit, and verify setTaskMessage called`() {
        val pos = model.getEditableTaskPosition()

        setEditInputState()
        setMessageToEditTextString(TEST_MESSAGE)
        taskListPresenter.onMainButtonClicked()
        verify(model).setTaskMessage(pos, TEST_MESSAGE)
    }

    @Test
    fun `onMainButtonClicked when inputState = Edit, and verify setTaskState called`() {
        val pos = model.getEditableTaskPosition()

        setEditInputState()
        setMessageToEditTextString(TEST_MESSAGE)
        taskListPresenter.onMainButtonClicked()
        verify(model).setTaskState(pos, TaskState.DEFAULT)
    }

    @Test
    fun `onMainButtonClicked when inputState = Edit, and verify setToDoTaskInputText called`() {
        setEditInputState()
        setMessageToEditTextString(TEST_MESSAGE)
        taskListPresenter.onMainButtonClicked()
        verify(view).setToDoTaskInputText(TEST_MESSAGE)
    }

    companion object {
        private const val TEST_MESSAGE = "Test"
    }
}
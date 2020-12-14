package ru.faizovr.todo.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragment_to_do_list.*
import ru.faizovr.todo.R
import ru.faizovr.todo.ToDoApplication
import ru.faizovr.todo.presentation.activity.ToDoActivity
import ru.faizovr.todo.presentation.adapter.ToDoTaskAdapter
import ru.faizovr.todo.presentation.contract.TaskListContract
import ru.faizovr.todo.presentation.presenter.TaskListPresenter
import ru.faizovr.todo.presentation.textwatcher.MessageInputTextWatcher
import ru.faizovr.todo.presentation.touchhelper.TaskTouchHelper
import ru.faizovr.todo.presentation.model.TaskDataView

class TaskListFragment : Fragment(R.layout.fragment_to_do_list), TaskListContract.View {

    private var taskListPresenter: TaskListContract.Presenter? = null
    private lateinit var messageInputTextWatcher: MessageInputTextWatcher
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val onEditButtonClicked: (position: Int) -> Unit = { position: Int ->
        taskListPresenter?.onEditTaskClickedForPosition(position)
    }
    private val onCheckBoxClicked: (position: Int) -> Unit = { position: Int ->
        taskListPresenter?.onCheckBoxTaskClickedForPosition(position)
    }

    private val onTaskClicked: (position: Int) -> Unit = { position: Int ->
        taskListPresenter?.onTaskClickedForPosition(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToDoActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setupViews()
        setupPresenter()
        setupHelpers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        taskListPresenter?.onSaveInstanceState()
    }

    private fun setupPresenter() {
        val app: ToDoApplication = requireActivity().application as ToDoApplication?
                ?: throw IllegalStateException("Fragment $this not attached to an app.")
        taskListPresenter = TaskListPresenter(this, app.model, resources)
    }

    private fun setupViews() {
        lists_recycler_view.adapter = ToDoTaskAdapter(onEditButtonClicked, onCheckBoxClicked, onTaskClicked)
    }

    override fun showTaskFragment(id: Long) {
        val bundle = Bundle()
        bundle.putLong(TaskFragment.TASK_ID_KEY, id)
        val fragment: Fragment = TaskFragment()
        fragment.arguments = bundle

        fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_container, fragment)
                ?.addToBackStack(TaskFragment.FRAGMENT_TAG)
                ?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyHelpers()
    }

    private fun destroyHelpers() {
        val taskListPresenter1 = taskListPresenter
        if (taskListPresenter1 != null) {
            edit_text_add.removeTextChangedListener(messageInputTextWatcher)
            itemTouchHelper.attachToRecyclerView(null)
        }
    }

    private fun setupHelpers() {
        val taskListPresenter1 = taskListPresenter
        if (taskListPresenter1 != null) {
            messageInputTextWatcher = MessageInputTextWatcher(taskListPresenter1)
            edit_text_add.addTextChangedListener(messageInputTextWatcher)
            val taskTouchHelper = TaskTouchHelper(taskListPresenter1)
            itemTouchHelper = ItemTouchHelper(taskTouchHelper)
            itemTouchHelper.attachToRecyclerView(lists_recycler_view)
        }
    }

    override fun setFuncToMainButton() {
        button_main.setOnClickListener {
            taskListPresenter?.onMainButtonClicked()
        }
    }

    override fun clearEditText() {
        edit_text_add.text?.clear()
    }

    override fun setMainButtonClickable(isClickable: Boolean) {
        button_main.isClickable = isClickable
    }

    override fun setMainButtonAlpha(alpha: Float) {
        button_main.alpha = alpha
    }

    override fun setListVisibility(isVisible: Boolean) {
        lists_recycler_view.isVisible = isVisible
    }

    override fun setEmptyTextMessageVisibility(isVisible: Boolean) {
        text_empty.isVisible = isVisible
    }

    override fun setToDoTaskInputText(message: String) {
        edit_text_add.setText(message)
    }

    override fun setAddTextToMainButton() {
        button_main.setText(R.string.action_add_task)
    }

    override fun setEditTextToMainButton() {
        button_main.setText(R.string.action_edit_task)
    }

    override fun updateList(taskList: List<TaskDataView>) {
        val adapter: ToDoTaskAdapter = lists_recycler_view.adapter as ToDoTaskAdapter
        adapter.updateList(taskList)
    }

}
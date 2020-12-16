package ru.faizovr.todo.presentation.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.task_view_holder.*
import ru.faizovr.todo.R
import ru.faizovr.todo.ToDoApplication
import ru.faizovr.todo.presentation.contract.TaskContract
import ru.faizovr.todo.presentation.presenter.TaskPresenter


class TaskFragment : Fragment(R.layout.fragment_task), TaskContract.View {

    private var taskPresenter: TaskPresenter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupPresenter()
    }

    private fun setupPresenter() {
        val id = arguments?.getLong(TASK_ID_KEY)
        val app: ToDoApplication = requireActivity().application as ToDoApplication?
                ?: throw IllegalStateException("Fragment $this not attached to an app.")
        taskPresenter = TaskPresenter(this, app.model, id)
    }

    override fun showTaskMessage(message: String) {
        text_task_message.text = message

    }

    companion object {
        const val FRAGMENT_TAG = "TaskFragment"
        const val TASK_ID_KEY = "TASK_ID"
    }
}
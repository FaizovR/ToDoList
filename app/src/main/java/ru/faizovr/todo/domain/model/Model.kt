package ru.faizovr.todo.domain.model

import ru.faizovr.todo.data.Repository

class Model(private val repository: Repository) {

    private val taskList: MutableList<Task> = mutableListOf()
    private var nextTaskId: Long = 0
    private var editableTaskPosition: Int = -1

    init {
        loadDataFromSharedPreference()
    }

    private fun loadDataFromSharedPreference() {
        val taskList = repository.getListFromSharedPreference() as MutableList<Task>
        this.taskList.clear()
        this.taskList.addAll(taskList)
        nextTaskId = repository.getIdFromSharedPreference()
        editableTaskPosition = repository.getEditablePositionFromSharedPreference()
    }

    fun saveDataToSharedPreference() {
        repository.saveListToSharedPreference(taskList)
        repository.saveEditablePositionToSharedPreference(editableTaskPosition)
        repository.saveIdToSharedPreference(nextTaskId)
    }

    fun getMyList(): List<Task> =
            taskList

    fun addTask(message: String) {
        val newTask = Task(nextTaskId++, message)
        taskList.add(newTask)
    }

    fun getTaskById(id: Long): Task? =
            taskList.find { it.id == id }

    private fun swapEditableTask(fromPosition: Int, toPosition: Int) {
        if (taskList[fromPosition].taskState == TaskState.EDIT) {
            editableTaskPosition = toPosition
        } else if (taskList[toPosition].taskState == TaskState.EDIT) {
            editableTaskPosition = fromPosition
        }
    }

    fun swapTask(fromPosition: Int, toPosition: Int) {
        if (fromPosition in 0 until taskList.size && toPosition in 0 until taskList.size) {
            swapEditableTask(fromPosition, toPosition)
            val temp: Task = taskList[fromPosition]
            taskList[fromPosition] = taskList[toPosition]
            taskList[toPosition] = temp
        }
    }

    fun getEditableTaskPosition(): Int =
            editableTaskPosition

    fun getEditableTaskMessage(): String =
            if (getEditableTaskPosition() in 0 until taskList.size)
                taskList[getEditableTaskPosition()].message
            else
                ""

    fun setTaskState(position: Int, taskState: TaskState) {
        if (position in 0 until taskList.size) {
            taskList[position].taskState = taskState
            editableTaskPosition = if (taskState == TaskState.EDIT)
                position
            else
                -1
        }
    }

    fun deleteTask(position: Int) {
        if (editableTaskPosition == position)
            editableTaskPosition = -1
        if (position in 0 until taskList.size)
            taskList.removeAt(position)
    }

    fun setTaskMessage(position: Int, message: String) {
        if (position in 0 until taskList.size)
            taskList[position].message = message
    }

    fun getTaskFromPosition(position: Int): Task? =
            if (position in 0 until taskList.size)
                taskList[position]
            else
                null

    fun getList(): List<Task> =
            taskList
}


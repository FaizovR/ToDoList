package ru.faizovr.todo.data


import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.faizovr.todo.domain.model.Model
import ru.faizovr.todo.domain.model.Task
import ru.faizovr.todo.domain.model.TaskState

class ModelTest {

    companion object {
        private const val TEST_MESSAGE = "Read Book"
    }

    private val model = Model(mock())

    @Before
    fun before() {
        model.addTask("test 1")
        model.addTask("test 2")
        model.addTask("test 3")
    }

    @Test
    fun `addTask new item added then assert size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val expectedSize = initialSize + 1

        model.addTask(TEST_MESSAGE)
        Assert.assertEquals(myList.size, expectedSize)
    }

    @Test
    fun `addTask new item added then assert message`() {
        val myList: List<Task> = model.getMyList()
        val expectedMessage = TEST_MESSAGE

        model.addTask(TEST_MESSAGE)
        val lastItem: Task = myList.last()
        Assert.assertEquals(lastItem.message, expectedMessage)
    }

    @Test
    fun `deleteTask item deleted then assert size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val expectedSize = initialSize - 1

        model.deleteTask(0)
        Assert.assertEquals(myList.size, expectedSize)
    }


    @Test
    fun `deleteTask item deleted then assert item`() {
        val myList: List<Task> = model.getMyList()
        val deletedItem = myList[0]

        model.deleteTask(0)
        Assert.assertEquals(myList.contains(deletedItem), false)
    }

    @Test
    fun `deleteTask with invalid params then assert size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val invalidElementPosition = myList.size

        model.deleteTask(invalidElementPosition)
        Assert.assertEquals(myList.size, initialSize)
    }


    @Test
    fun `deleteTask with invalid params then assert list`() {
        val myList: List<Task> = model.getMyList()
        val expectedList: List<Task> = model.getMyList()
        val invalidElementPosition = myList.size

        model.deleteTask(invalidElementPosition)
        Assert.assertEquals(myList, expectedList)
    }

    @Test
    fun `deleteTask delete editable task, and check editable position then assert editable position`() {
        val initialEditablePosition = 0
        val expectedEditablePosition = -1
        model.setTaskState(0, TaskState.EDIT)
        model.deleteTask(0)
        val actualEditablePosition = model.getEditableTaskPosition()

        Assert.assertNotEquals(initialEditablePosition, actualEditablePosition)
        Assert.assertEquals(actualEditablePosition, expectedEditablePosition)
    }

    @Test
    fun `deleteTask delete editable task, and check editable position then assert size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val expectedSize = initialSize - 1
        model.setTaskState(0, TaskState.EDIT)
        model.deleteTask(0)

        Assert.assertEquals(myList.size, expectedSize)
    }

    @Test
    fun `deleteTask delete editable task, and check editable position then assert list contain item`() {
        val myList: List<Task> = model.getMyList()
        val deletedItem = myList[0]
        model.setTaskState(0, TaskState.EDIT)
        model.deleteTask(0)

        Assert.assertEquals(myList.contains(deletedItem), false)
    }

    @Test
    fun `swapTask with two valid params then assert item position`() {
        val myList: List<Task> = model.getMyList()
        val firstItemPosition = 0
        val secondItemPosition = 1
        val expectedItemAtFirstPosition = myList[secondItemPosition]
        val expectedItemAtSecondPosition = myList[firstItemPosition]

        model.swapTask(firstItemPosition, secondItemPosition)
        Assert.assertEquals(myList[firstItemPosition], expectedItemAtFirstPosition)
        Assert.assertEquals(myList[secondItemPosition], expectedItemAtSecondPosition)
    }

    @Test
    fun `swapTask with two valid params then assert list size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val firstItemPosition = 0
        val secondItemPosition = 1

        model.swapTask(firstItemPosition, secondItemPosition)
        Assert.assertEquals(myList.size, initialSize)
    }

    @Test
    fun `swapTask with first invalid param and second valid param then assert item position`() {
        val myList: List<Task> = model.getMyList()
        val invalidItemPosition = myList.size
        val firstItemPosition = 0
        val secondItemPosition = 1
        val expectedItemAtFirstPosition = myList[firstItemPosition]
        val expectedItemAtSecondPosition = myList[secondItemPosition]

        model.swapTask(invalidItemPosition, secondItemPosition)

        Assert.assertEquals(myList[firstItemPosition], expectedItemAtFirstPosition)
        Assert.assertEquals(myList[secondItemPosition], expectedItemAtSecondPosition)
    }

    @Test
    fun `swapTask with first invalid param and second valid param then assert list size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val invalidItemPosition = myList.size
        val secondItemPosition = 1

        model.swapTask(invalidItemPosition, secondItemPosition)

        Assert.assertEquals(myList.size, initialSize)
    }


    @Test
    fun `swapTask with first valid param and second invalid param then assert itemPosition`() {
        val myList: List<Task> = model.getMyList()
        val invalidItemPosition = myList.size
        val firstItemPosition = 0
        val secondItemPosition = 1
        val expectedItemAtFirstPosition = myList[firstItemPosition]
        val expectedItemAtSecondPosition = myList[secondItemPosition]

        model.swapTask(firstItemPosition, invalidItemPosition)

        Assert.assertEquals(myList[firstItemPosition], expectedItemAtFirstPosition)
        Assert.assertEquals(myList[secondItemPosition], expectedItemAtSecondPosition)
    }

    @Test
    fun `swapTask with first valid param and second invalid param then assert list size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val invalidItemPosition = myList.size
        val firstItemPosition = 0

        model.swapTask(firstItemPosition, invalidItemPosition)

        Assert.assertEquals(myList.size, initialSize)
    }

    @Test
    fun `swapTask with both invalid params then assert item position`() {
        val myList: List<Task> = model.getMyList()
        val firstInvalidItemPosition = myList.size
        val secondInvalidPosition = -1
        val firstItemPosition = 0
        val secondItemPosition = 1
        val expectedItemAtFirstPosition = myList[firstItemPosition]
        val expectedItemAtSecondPosition = myList[secondItemPosition]

        model.swapTask(firstInvalidItemPosition, secondInvalidPosition)

        Assert.assertEquals(myList[firstItemPosition], expectedItemAtFirstPosition)
        Assert.assertEquals(myList[secondItemPosition], expectedItemAtSecondPosition)
    }

    @Test
    fun `swapTask with both invalid params then assert list size`() {
        val myList: List<Task> = model.getMyList()
        val initialSize = myList.size
        val firstInvalidItemPosition = myList.size
        val secondInvalidPosition = -1

        model.swapTask(firstInvalidItemPosition, secondInvalidPosition)

        Assert.assertEquals(myList.size, initialSize)
    }

    @Test
    fun `getEditableTaskPosition when list contain editable element then assert editable task position`() {
        val expectedTaskPosition = 0
        model.setTaskState(0, TaskState.EDIT)
        val editableTaskPosition = model.getEditableTaskPosition()

        Assert.assertEquals(editableTaskPosition, expectedTaskPosition)
    }

    @Test
    fun `getEditableTaskPosition when list doesn't contain editable element then assert editable task position`() {
        val expectedTaskPosition = -1
        model.setTaskState(0, TaskState.DEFAULT)
        val editableTaskPosition = model.getEditableTaskPosition()

        Assert.assertEquals(editableTaskPosition, expectedTaskPosition)
    }

    @Test
    fun `getEditableTaskMessage when list contain editable message then asset editable task message`() {
        val myList: List<Task> = model.getMyList()
        val expectedTaskMessage = myList[0].message
        model.setTaskState(0, TaskState.EDIT)
        val editableTaskMessage = model.getEditableTaskMessage()

        Assert.assertEquals(editableTaskMessage, expectedTaskMessage)
    }

    @Test
    fun `getEditableTaskMessage when list doesn't contain editable element then assert editable task message`() {
        val expectedTaskMessage = ""
        model.setTaskState(0, TaskState.DEFAULT)
        val editableTaskMessage = model.getEditableTaskMessage()

        Assert.assertEquals(editableTaskMessage, expectedTaskMessage)
    }

    private fun setTaskStatePrepareAndAssert(from: TaskState, to: TaskState, expectedTaskState: TaskState) {
        val myList: List<Task> = model.getMyList()
        val testingTaskPosition = 0
        val task = myList[testingTaskPosition]
        task.taskState = from

        model.setTaskState(testingTaskPosition, to)

        val actualTaskState = task.taskState
        Assert.assertEquals(actualTaskState, expectedTaskState)
    }

    @Test
    fun `setTaskState from EDIT to EDIT then assert TaskState EDIT`() {
        setTaskStatePrepareAndAssert(TaskState.EDIT, TaskState.EDIT, TaskState.EDIT)
    }

    @Test
    fun `setTaskState from EDIT to COMPLETE then assert TaskState EDIT`() {
        setTaskStatePrepareAndAssert(TaskState.EDIT, TaskState.COMPLETE, TaskState.COMPLETE)
    }

    @Test
    fun `setTaskState from EDIT to DEFAULT then assert TaskState DEFAULT`() {
        setTaskStatePrepareAndAssert(TaskState.EDIT, TaskState.DEFAULT, TaskState.DEFAULT)
    }

    @Test
    fun `setTaskState from DEFAULT to DEFAULT then assert TaskState DEFAULT`() {
        setTaskStatePrepareAndAssert(TaskState.DEFAULT, TaskState.DEFAULT, TaskState.DEFAULT)
    }

    @Test
    fun `setTaskState from DEFAULT to EDIT then assert TaskState EDIT`() {
        setTaskStatePrepareAndAssert(TaskState.DEFAULT, TaskState.EDIT, TaskState.EDIT)
    }

    @Test
    fun `setTaskState from DEFAULT to COMPLETE then assert TaskState COMPLETE`() {
        setTaskStatePrepareAndAssert(TaskState.DEFAULT, TaskState.COMPLETE, TaskState.COMPLETE)
    }

    @Test
    fun `setTaskState from COMPLETE to COMPLETE then assert TaskState COMPLETE`() {
        setTaskStatePrepareAndAssert(TaskState.COMPLETE, TaskState.COMPLETE, TaskState.COMPLETE)
    }

    @Test
    fun `setTaskState from COMPLETE to DEFAULT then assert TaskState DEFAULT`() {
        setTaskStatePrepareAndAssert(TaskState.COMPLETE, TaskState.DEFAULT, TaskState.DEFAULT)
    }

    @Test
    fun `setTaskState from COMPLETE to EDIT then assert TaskState EDIT`() {
        setTaskStatePrepareAndAssert(TaskState.COMPLETE, TaskState.EDIT, TaskState.EDIT)
    }

    @Test
    fun `testSetTaskState from SOME to EDIT then assert that editable position changed`() {
        val initialTaskState = TaskState.DEFAULT
        val taskPosition = 0
        model.setTaskState(taskPosition, initialTaskState)
        val initialEditablePosition = model.getEditableTaskPosition()
        val editableTaskState = TaskState.EDIT
        model.setTaskState(taskPosition, editableTaskState)

        val actualEditablePosition = model.getEditableTaskPosition()

        Assert.assertNotEquals(initialEditablePosition, actualEditablePosition)
        Assert.assertEquals(taskPosition, actualEditablePosition)
    }

    @Test
    fun `testSetTaskState from EDIT to SOME then assert that editable position changed`() {
        val expectedEditablePosition = -1
        val taskPosition = 0
        val initialTaskState = TaskState.EDIT
        model.setTaskState(taskPosition, initialTaskState)
        val initialEditablePosition = model.getEditableTaskPosition()

        Assert.assertEquals(taskPosition, initialEditablePosition)

        val expectedTaskState = TaskState.DEFAULT
        model.setTaskState(taskPosition, expectedTaskState)

        val actualEditablePosition = model.getEditableTaskPosition()

        Assert.assertNotEquals(initialEditablePosition, actualEditablePosition)
        Assert.assertEquals(actualEditablePosition, expectedEditablePosition)
    }

    @Test
    fun `setTaskMessage with valid params then assert message Changed`() {
        val myList = model.getMyList()
        val initialTask = myList.first()
        val initialMessage = initialTask.message
        val expectedMessage = "Message updated"
        val taskPosition = 0

        model.setTaskMessage(taskPosition, expectedMessage)
        val actualMessage = model.getMyList().first().message

        Assert.assertNotEquals(initialMessage, actualMessage)
        Assert.assertEquals(actualMessage, expectedMessage)
    }

    @Test
    fun `getTaskFromPosition with valid param then assert testing task = actual task`() {
        val myList = model.getMyList()
        val testingTask = myList.first()
        val testingPosition = 0

        val actualTask = model.getTaskFromPosition(testingPosition)
        Assert.assertEquals(actualTask, testingTask)
    }

    @Test
    fun `getTaskFromPosition with invalid param then assert testing task = actual task = null`() {
        val testingTask = null
        val testingPosition = -1

        val actualTask = model.getTaskFromPosition(testingPosition)
        Assert.assertEquals(actualTask, testingTask)
    }
}
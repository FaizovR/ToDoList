package ru.faizovr.todo.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.faizovr.todo.domain.model.Task

class RepositoryImpl(private val sharedPreferences: SharedPreferences,
                     private val gson: Gson)
    : Repository {

    override fun getListFromSharedPreference(): List<Task> {
        val jsonString = sharedPreferences.getString(PREFS_TASK_LIST_KEY, "")
        if (jsonString?.isEmpty() == true)
            return mutableListOf()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson<MutableList<Task>>(jsonString, type)
    }

    override fun getEditablePositionFromSharedPreference(): Int =
            sharedPreferences.getInt(PREFS_EDITABLE_POSITION_KEY, -1)

    override fun getIdFromSharedPreference(): Long =
            sharedPreferences.getLong(PREFS_ID_KEY, 0)

    override fun saveListToSharedPreference(taskList: List<Task>) {
        val jsonString: String = gson.toJson(taskList)
        sharedPreferences.edit().apply {
            putString(PREFS_TASK_LIST_KEY, jsonString)
            apply()
        }
    }

    override fun saveEditablePositionToSharedPreference(editablePosition: Int) {
        sharedPreferences.edit().apply {
            putInt(PREFS_EDITABLE_POSITION_KEY, editablePosition)
            apply()
        }
    }


    override fun saveIdToSharedPreference(id: Long) {
        sharedPreferences.edit().apply {
            putLong(PREFS_ID_KEY, id)
            apply()
        }
    }

    companion object {
        private const val PREFS_TASK_LIST_KEY = "PREFS_TASK_LIST_KEY"
        private const val PREFS_ID_KEY = "PREFS_ID_KEY"
        private const val PREFS_EDITABLE_POSITION_KEY = "PREFS_EDITABLE_POSITION_KEY"
    }
}
package ru.faizovr.todo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.faizovr.todo.data.RepositoryImpl
import ru.faizovr.todo.domain.model.Model

class ToDoApplication : Application() {

    lateinit var model: Model
        private set

    override fun onCreate() {
        super.onCreate()
        setupModel()
    }

    private fun setupModel() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val repository = RepositoryImpl(sharedPreferences, Gson())
        model = Model(repository)
    }

    companion object {
        private const val PREFERENCES = "app_preferences"
    }
}
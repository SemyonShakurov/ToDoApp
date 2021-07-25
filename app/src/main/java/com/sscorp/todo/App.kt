package com.sscorp.todo

import android.app.Application
import android.content.Context
import com.sscorp.todo.database.DatabaseModule
import com.sscorp.todo.fragments.EditTasksFragment
import com.sscorp.todo.fragments.TasksFragment
import com.sscorp.todo.network.NetworkModule
import dagger.Component
import javax.inject.Singleton

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun getApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun inject(fragment: TasksFragment)
    fun inject(fragment: EditTasksFragment)
}
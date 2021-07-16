package com.sscorp.todo

import android.app.Application
import android.content.Context

class App : Application() {

    companion object{
        private lateinit var appContext: Context

        fun setContext(context: Context) {
            appContext = context
        }

        fun getAppContext() = appContext
    }
}
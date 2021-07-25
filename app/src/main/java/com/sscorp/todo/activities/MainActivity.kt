package com.sscorp.todo.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.sscorp.todo.R
import com.sscorp.todo.fragments.TasksFragmentDirections
import com.sscorp.todo.models.Task
import com.sscorp.todo.notifications.NotificationReceiver
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNotification()
    }

    fun openEditNoteFragment(task: Task? = null) {
        val action = TasksFragmentDirections.actionTasksFragmentToEditTasksFragment(task)
        findViewById<FragmentContainerView>(R.id.nav_host_fragment).findNavController()
            .navigate(action)
    }

    private fun setNotification() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.time < Date())
            calendar.add(Calendar.DAY_OF_MONTH, 1)

        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as? AlarmManager

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
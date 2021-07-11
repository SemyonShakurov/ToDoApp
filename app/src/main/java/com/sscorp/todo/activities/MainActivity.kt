package com.sscorp.todo.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sscorp.todo.App
import com.sscorp.todo.R
import com.sscorp.todo.fragments.EditNotesFragment
import com.sscorp.todo.fragments.NotesFragment
import com.sscorp.todo.models.Note
import com.sscorp.todo.notifications.NotificationReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.setContext(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, NotesFragment())
                .commit()
        }
        setNotification()
    }

    fun openEditNoteFragment(note: Note? = null) {
        val fragment = if (note == null)
            EditNotesFragment()
        else
            EditNotesFragment.newInstance(note)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, fragment)
            .commit()
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
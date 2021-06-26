package com.sscorp.todo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.adapters.NotesAdapter
import com.sscorp.todo.adapters.SwipeCallback
import com.sscorp.todo.models.Note
import com.sscorp.todo.utils.NotesLoader

class MainActivity : AppCompatActivity() {

    private val notes: MutableList<Note> = getSortedNotes()

    private lateinit var notesAdapter: NotesAdapter

    private var currentVisibility = Visibility.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContentToListOfNotes()
        setSupportActionBar(findViewById(R.id.toolbar))
        updateCompletedTasks()

        findViewById<ImageView>(R.id.imageViewVisibility).setOnClickListener {
            onVisibilityChange(it as ImageView)
        }
    }

    private fun setContentToListOfNotes() {
        val list = findViewById<RecyclerView>(R.id.list_notes)
        notesAdapter = NotesAdapter(this)
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(notesAdapter))
        itemTouchHelper.attachToRecyclerView(list)
        notesAdapter.submitList(notes.filter { !it.isDone })
        list.adapter = notesAdapter
    }

    fun updateCompletedTasks() {
        val count = notes.filter { it.isDone }.size
        findViewById<TextView>(R.id.textViewCount).text =
            String.format(getString(R.string.count_done), count)
    }

    private fun getSortedNotes(): MutableList<Note> {
        val notes = NotesLoader.getNotes()
        return notes.sortedDescending().toMutableList()
    }

    private fun onVisibilityChange(view: ImageView) {

    }

    fun deleteNote(position: Int) {
        var counter = -1
        for ((ind, note) in notes.withIndex()) {
            if (currentVisibility == Visibility.NORMAL && note.isDone)
                continue
            counter++
            if (counter == position) {
                notes.removeAt(ind)
                if (currentVisibility == Visibility.SHOWN_ALL)
                    notesAdapter.submitList(notes.toMutableList())
                else
                    notesAdapter.submitList(notes.filter { !it.isDone })
                break
            }
        }
    }

    enum class Visibility {
        NORMAL, SHOWN_ALL
    }
}
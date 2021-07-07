package com.sscorp.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity
import com.sscorp.todo.adapters.NotesAdapter
import com.sscorp.todo.adapters.SwipeCallback
import com.sscorp.todo.models.Note
import com.sscorp.todo.presenter.NotesPresenter

class NotesFragment : Fragment() {

    private lateinit var presenter: NotesPresenter

    private lateinit var notesAdapter: NotesAdapter

    private lateinit var textViewCount: TextView
    private lateinit var visibilityIcon: ImageView
    private lateinit var notesList: RecyclerView
    private lateinit var addNoteButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        textViewCount = view.findViewById(R.id.textViewCount)
        visibilityIcon = view.findViewById(R.id.imageViewVisibility)
        notesList = view.findViewById(R.id.list_notes)
        addNoteButton = view.findViewById(R.id.btn_add_note)
        presenter = NotesPresenter(this)
        configListOfNotes()
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
        visibilityIcon.setOnClickListener {
            presenter.changeVisibility()
        }
        view.findViewById<Toolbar>(R.id.toolbar)
            .setOnClickListener {
                notesList.smoothScrollToPosition(0)
            }
        addNoteButton.setOnClickListener {
            (context as MainActivity).openEditNoteFragment()
        }
        presenter.viewIsReady()
    }

    private fun configListOfNotes() {
        notesAdapter = NotesAdapter(requireContext(), this, ::onCheckBoxClicked)
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(notesAdapter))
        itemTouchHelper.attachToRecyclerView(notesList)
        notesList.adapter = notesAdapter
    }

    fun showNotes(notes: List<Note>) {
        notesAdapter.submitList(notes)
    }

    fun updateCountOfCompleted(count: Int) {
        textViewCount.text = String.format(getString(R.string.count_done), count)
    }

    private fun onCheckBoxClicked(note: Note, position: Int) {
        presenter.checkBoxClick(note, position)
    }

    fun deleteNote(note: Note) {
        presenter.deleteNote(note)
    }

    fun updateListItemView(position: Int) {
        notesAdapter.notifyItemChanged(position)
    }

    fun checkNote(note: Note, position: Int) {
        presenter.checkNote(note, position)
    }

    fun showVisibilityOnIcon() {
        visibilityIcon.setImageResource(R.drawable.ic_visibility)
    }

    fun showVisibilityOffIcon() {
        visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
    }
}
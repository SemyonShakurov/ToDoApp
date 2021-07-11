package com.sscorp.todo.presenter

import com.sscorp.todo.fragments.EditNotesFragment
import com.sscorp.todo.models.Note
import com.sscorp.todo.models.NotesModel

class EditNotePresenter(
    private val view: EditNotesFragment,
) {

    fun addNote(note: Note) {
        NotesModel.addNote(note) {
            view.activity?.onBackPressed()
        }
    }

    fun deleteNote(note: Note) {
        NotesModel.deleteNote(note) {
            view.activity?.onBackPressed()
        }
    }

    fun updateNote(oldNoteData: Note, newNoteDate: Note) {
        NotesModel.updateNote(oldNoteData, newNoteDate) {
            view.activity?.onBackPressed()
        }
    }
}
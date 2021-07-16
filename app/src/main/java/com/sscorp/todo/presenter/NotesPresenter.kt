package com.sscorp.todo.presenter

import com.sscorp.todo.fragments.NotesFragment
import com.sscorp.todo.models.Note
import com.sscorp.todo.models.NotesModel

class NotesPresenter(
    private val view: NotesFragment,
) {

    private var currentVisibility = Visibility.NORMAL

    fun changeVisibility() {
        currentVisibility = if (currentVisibility == Visibility.NORMAL) {
            view.showVisibilityOffIcon()
            Visibility.ALL
        } else {
            view.showVisibilityOnIcon()
            Visibility.NORMAL
        }

        val sortedNotes = getFilteredAndSortedNotes(NotesModel.notes)
        view.showNotes(sortedNotes)
    }

    fun viewIsReady() {
        loadNotes()
    }

    fun deleteNote(note: Note) {
        NotesModel.deleteNote(note) {
            view.updateCountOfCompleted(NotesModel.notes.filter { it.isDone }.size)
            val sortedNotes = getFilteredAndSortedNotes(NotesModel.notes)
            view.showNotes(sortedNotes)
        }
    }

    fun checkBoxClick(note: Note, position: Int) {
        note.isDone = !note.isDone
        NotesModel.updateNoteDataInDb(note)
        view.updateCountOfCompleted(NotesModel.notes.filter { it.isDone }.size)
        view.updateListItemView(position)
    }

    fun checkNote(note: Note, position: Int) {
        note.isDone = true
        NotesModel.updateNoteDataInDb(note)
        view.updateCountOfCompleted(NotesModel.notes.filter { it.isDone }.size)
        view.updateListItemView(position)
    }

    private fun loadNotes() {
        NotesModel.loadNotes { list ->
            val filteredList = list.filter { !it.isDone }.sortedDescending()
            view.showNotes(filteredList)
            view.updateCountOfCompleted(list.size - filteredList.size)
        }
    }

    private fun getFilteredAndSortedNotes(notes: List<Note>): List<Note> {
        return if (currentVisibility == Visibility.ALL)
            notes.sortedDescending()
        else
            notes.filter { !it.isDone }.sortedDescending()
    }

    enum class Visibility {
        NORMAL, ALL
    }
}
package com.sscorp.todo.models

import java.util.*

object NotesModel {

    private var mutableNotes: MutableList<Note>? = null
    val notes: List<Note>
        get() = mutableNotes!!

    fun loadNotes(onLoad: (list: List<Note>) -> Unit) {
        if (mutableNotes == null)
            mutableNotes = mutableListOf(
                Note("Сходить в магазин"),
                Note("Написать ToDo приложение"),
                Note("Встретиться с друзьями", expirationDate = Date()),
                Note("Сдать сессию"),
                Note("Сходить в тренажерку", Importance.HIGH),
                Note("Покормить пса"),
                Note(
                    "Купить что-то, где-то, зачем-то, " +
                            "но зачем не очень понятно, " +
                            "но точно чтобы показать как обрезается…", isDone = true
                ),
                Note(
                    "Купить что-то, где-то, зачем-то, " +
                            "но зачем не очень понятно, " +
                            "но точно чтобы показать как обрезается…", Importance.HIGH
                ),
                Note(
                    "Купить что-то, где-то, зачем-то, " +
                            "но зачем не очень понятно, " +
                            "но точно чтобы показать как обрезается…", Importance.LOW
                ),
                Note(
                    "Купить что-то, где-то, зачем-то, " +
                            "но зачем не очень понятно, " +
                            "но точно чтобы показать как обрезается…"
                )
            )
        onLoad(mutableNotes!!)
    }

    fun deleteNote(note: Note) {
        val pos = findNotePos(note)
        mutableNotes!!.removeAt(pos)
    }

    fun addNote(note: Note) {
        mutableNotes!!.add(note)
    }

    fun updateNote(oldNote: Note, newNote: Note) {
        val pos = findNotePos(oldNote)
        mutableNotes!![pos] = newNote
    }

    private fun findNotePos(note: Note): Int {
        var pos: Int? = null
        for ((ind, _note) in mutableNotes!!.withIndex()) {
            if (note === _note) {
                pos = ind
                break
            }
        }
        if (pos == null)
            throw IllegalArgumentException()
        return pos
    }
}
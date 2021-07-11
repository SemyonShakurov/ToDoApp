package com.sscorp.todo.models

import com.sscorp.todo.database.DatabaseStorage
import com.sscorp.todo.database.NoteEntity
import com.sscorp.todo.network.NetworkModule
import com.sscorp.todo.network.NotesChangesBody
import com.sscorp.todo.network.TaskResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

object NotesModel {

    private var mutableNotes: MutableList<Note> = mutableListOf()
    val notes: List<Note>
        get() = mutableNotes

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val noteDao = DatabaseStorage.instance.noteDao()

    private val api = NetworkModule.noteApiService

    fun loadNotes(onLoad: (list: List<Note>) -> Unit) {
        ioScope.launch {
            var savedNotes: List<Note>
            try {
                val changes = getChanges()
                if (changes != null) {
                    savedNotes = responsesToNotesList(api.syncNotes(changes))
                    clearChanges()
                }
                else
                    savedNotes = responsesToNotesList(api.getNotes())
            } catch (ex: Exception) {
                savedNotes = entitiesToNotesList(noteDao.all())
            }
            mutableNotes = savedNotes.toMutableList()
            withContext(Dispatchers.Main) {
                onLoad(mutableNotes)
            }
        }
    }

    fun deleteNote(note: Note, onDelete: () -> Unit) {
        ioScope.launch {
            launch {
                val noteEntity = noteToNoteEntity(note)
                try {
                    api.deleteNote("tasks/${note.id!!}")
                    noteDao.delete(noteEntity)
                } catch (ex: Exception) {
                    noteEntity.syncState = "deleted"
                    noteDao.update(noteEntity)
                }
            }
            val pos = findNotePos(note)
            mutableNotes.removeAt(pos)
            withContext(Dispatchers.Main) {
                onDelete()
            }
        }
    }

    fun addNote(note: Note, onAdd: () -> Unit) {
        ioScope.launch {
            val noteEntity = noteToNoteEntity(note)
            val id = noteDao.insert(noteEntity)
            noteEntity.id = id
            note.id = id
            mutableNotes.add(note)
            launch {
                try {
                    api.saveNote(noteToTaskResponse(note))
                } catch (ex: Exception) {
                    noteEntity.syncState = "other"
                    noteDao.update(noteEntity)
                }
            }
            withContext(Dispatchers.Main) {
                onAdd()
            }
        }
    }

    fun updateNote(oldNote: Note, newNote: Note, onUpdate: () -> Unit) {
        ioScope.launch {
            val pos = findNotePos(oldNote)
            mutableNotes[pos] = newNote
            launch {
                val noteEntity = noteToNoteEntity(newNote, oldNote.id)
                try {
                    api.updateNote("tasks/${oldNote.id}", noteToTaskResponse(newNote))
                } catch (ex: Exception) {
                    noteEntity.syncState = "other"
                }
                noteDao.update(noteEntity)
            }
            withContext(Dispatchers.Main) {
                onUpdate()
            }
        }
    }

    private fun findNotePos(note: Note): Int {
        var pos: Int? = null
        for ((ind, _note) in mutableNotes.withIndex()) {
            if (note === _note) {
                pos = ind
                break
            }
        }
        if (pos == null)
            throw IllegalArgumentException()
        return pos
    }

    private fun noteEntityToNote(noteEntity: NoteEntity): Note {
        val text = noteEntity.text
        val priority = when (noteEntity.priority) {
            "basic" -> Importance.NO
            "low" -> Importance.LOW
            "important" -> Importance.HIGH
            else -> throw IllegalArgumentException()
        }
        val date = if (noteEntity.deadline != null)
            Date(noteEntity.deadline)
        else
            null
        val isDone = noteEntity.done
        val id = noteEntity.id
        return Note(text, priority, date, isDone, id)
    }

    private fun noteToNoteEntity(note: Note, id: Long? = null): NoteEntity {
        val text = note.description
        val priority = when (note.importance) {
            Importance.NO -> "basic"
            Importance.LOW -> "low"
            Importance.HIGH -> "important"
        }
        val date = note.expirationDate?.time
        val isDone = note.isDone
        return NoteEntity(id ?: note.id, text, priority, isDone, date)
    }

    private fun noteToTaskResponse(note: Note): TaskResponse {
        val idStr = note.id!!.toString()
        val text = note.description
        val priority = when (note.importance) {
            Importance.NO -> "basic"
            Importance.LOW -> "low"
            Importance.HIGH -> "important"
        }
        val done = note.isDone
        val deadline = note.expirationDate?.time ?: 0L

        return TaskResponse(idStr, text, priority, done, deadline)
    }

    private fun taskResponseToNote(response: TaskResponse): Note {
        val text = response.text
        val priority = when (response.priority) {
            "basic" -> Importance.NO
            "low" -> Importance.LOW
            "important" -> Importance.HIGH
            else -> throw IllegalArgumentException()
        }
        val date = when(response.deadline) {
            0L -> null
            else -> Date(response.deadline)
        }
        val isDone = response.done
        val id = response.id.toLong()
        return Note(text, priority, date, isDone, id)
    }

    fun updateNoteDataInDb(note: Note) {
        ioScope.launch {
            val noteEntity = noteToNoteEntity(note)
            try {
                api.updateNote("tasks/${note.id}", noteToTaskResponse(note))
            } catch (ex: Exception) {
                noteEntity.syncState = "other"
            }
            noteDao.update(noteEntity)
        }
    }

    private fun responsesToNotesList(responses: List<TaskResponse>): List<Note> {
        val notes: MutableList<Note> = mutableListOf()
        for (response in responses)
            notes.add(taskResponseToNote(response))
        return notes
    }

    private fun entitiesToNotesList(entities: List<NoteEntity>): List<Note> {
        val notes: MutableList<Note> = mutableListOf()
        for (entity in entities) {
            if (entity.syncState == "deleted")
                continue
            notes.add(noteEntityToNote(entity))
        }
        return notes
    }

    private suspend fun getChanges(): NotesChangesBody? {
        val deleted = noteDao.getDeleted()
        val other = noteDao.getOther()
        if (deleted.isEmpty() && other.isEmpty())
            return null
        val list = mutableListOf<TaskResponse>()
        for (item in other) {
            val note = noteEntityToNote(item)
            list.add(noteToTaskResponse(note))
        }
        return NotesChangesBody(deleted, list)
    }

    private suspend fun clearChanges() {
        noteDao.clearDeleteStates()
        noteDao.clearOthers()
    }
}
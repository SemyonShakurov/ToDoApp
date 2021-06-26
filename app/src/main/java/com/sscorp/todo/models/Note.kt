package com.sscorp.todo.models

import java.util.Date

data class Note(
    val description: String,
    val importance: Importance = Importance.NO,
    val expirationDate: Date? = null,
    var isDone: Boolean = false
) : Comparable<Note> {

    override fun compareTo(other: Note): Int = when {
        expirationDate == other.expirationDate ->
            importance.compareTo(other.importance)
        expirationDate == null -> -1
        other.expirationDate == null -> 1
        else -> expirationDate.compareTo(other.expirationDate)
    }
}

enum class Importance {
    NO, LOW, HIGH
}
package com.sscorp.todo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Note(
    val description: String,
    val importance: Importance = Importance.NO,
    val expirationDate: Date? = null,
    var isDone: Boolean = false
) : Comparable<Note>, Parcelable {

    override fun compareTo(other: Note): Int = when {
        expirationDate == other.expirationDate ->
            importance.compareTo(other.importance)
        expirationDate == null -> -1
        other.expirationDate == null -> 1
        else -> other.expirationDate.compareTo(expirationDate)
    }
}

enum class Importance {
    NO, LOW, HIGH
}
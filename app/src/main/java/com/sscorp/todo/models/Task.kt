package com.sscorp.todo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Task(
    var id: Int? = null,
    val text: String,
    val priority: Priority,
    var done: Boolean,
    val deadline: Date?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
): Parcelable, Comparable<Task> {

    override fun compareTo(other: Task): Int = when {
        deadline == other.deadline ->
            priority.compareTo(other.priority)
        deadline == null -> -1
        other.deadline == null -> 1
        else -> other.deadline.compareTo(deadline)
    }
}

enum class Priority {
    LOW, BASIC, IMPORTANT
}
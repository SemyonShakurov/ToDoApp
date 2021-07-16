package com.sscorp.todo.network

import java.io.Serializable

data class NotesChangesBody(
    val deleted: List<Int>,
    val other: List<TaskResponse>
): Serializable
package com.sscorp.todo.network

import java.io.Serializable

data class TaskResponse(
    val id: String,
    val text: String,
    val priority: String,
    val done: Boolean,
    val deadline: Long,
    val created_at: Long = 0,
    val updated_at: Long = 0
): Serializable
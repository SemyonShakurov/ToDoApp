package com.sscorp.todo.network

import java.io.Serializable

class TasksChangesBody(
    val deleted: List<Int>,
    val other: List<TaskResponse>
): Serializable
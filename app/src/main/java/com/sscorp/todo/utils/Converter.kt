package com.sscorp.todo.utils

import com.sscorp.todo.database.TaskEntity
import com.sscorp.todo.models.Priority
import com.sscorp.todo.models.Task
import com.sscorp.todo.network.TaskResponse
import java.util.*

object Converter {

    fun taskResponseToTask(taskResponse: TaskResponse): Task {
        val id = taskResponse.id.toInt()
        val text = taskResponse.text
        val priority = when (taskResponse.priority) {
            "low" -> Priority.LOW
            "important" -> Priority.IMPORTANT
            else -> Priority.BASIC
        }
        val done = taskResponse.done
        val deadline = when (taskResponse.deadline) {
            0L -> null
            else -> Date(taskResponse.deadline)
        }
        val createdAt = Date(taskResponse.created_at)
        val updatedAt = Date(taskResponse.updated_at)

        return Task(id, text, priority, done, deadline, createdAt, updatedAt)
    }

    fun taskEntityToTask(taskEntity: TaskEntity): Task {
        val id = taskEntity.id
        val text = taskEntity.text
        val priority = when (taskEntity.priority) {
            "low" -> Priority.LOW
            "important" -> Priority.IMPORTANT
            else -> Priority.BASIC
        }
        val done = taskEntity.done
        val deadline = if (taskEntity.deadline == null)
            null else Date(taskEntity.deadline)
        val createdAt = Date(taskEntity.createdAt)
        val updatedAt = Date(taskEntity.updatedAt)


        return Task(id, text, priority, done, deadline, createdAt, updatedAt)
    }

    fun taskToTaskResponse(task: Task): TaskResponse {
        val id = task.id.toString()
        val text = task.text
        val priority = when (task.priority) {
            Priority.BASIC -> "basic"
            Priority.LOW -> "low"
            Priority.IMPORTANT -> "important"
        }
        val done = task.done
        val deadline = task.deadline?.time ?: 0L
        val createdAt = task.createdAt.time
        val updatedAt = task.updatedAt.time

        return TaskResponse(id, text, priority, done, deadline, createdAt, updatedAt)
    }

    fun taskResponsesToTasks(taskResponses: List<TaskResponse>): List<Task> {
        val tasks: MutableList<Task> = mutableListOf()
        for (response in taskResponses)
            tasks.add(taskResponseToTask(response))
        return tasks
    }

    fun taskEntitiesToTasks(taskEntities: List<TaskEntity>): List<Task> {
        val tasks: MutableList<Task> = mutableListOf()
        for (entity in taskEntities) {
            if (entity.syncState == "deleted")
                continue
            tasks.add(taskEntityToTask(entity))
        }
        return tasks
    }

    fun taskToTaskEntity(task: Task, id: Int? = null): TaskEntity {
        val text = task.text
        val priority = when (task.priority) {
            Priority.BASIC -> "basic"
            Priority.LOW-> "low"
            Priority.IMPORTANT -> "important"
        }
        val date = task.deadline?.time
        val isDone = task.done
        val createdAt = task.createdAt.time
        val updatedAt = task.updatedAt.time
        return TaskEntity(id ?: task.id, text, priority, isDone, date, createdAt, updatedAt)
    }
}
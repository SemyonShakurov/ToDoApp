package com.sscorp.todo.repositories

import com.sscorp.todo.database.TasksDao
import com.sscorp.todo.models.Task
import com.sscorp.todo.network.TaskResponse
import com.sscorp.todo.network.TasksApiService
import com.sscorp.todo.network.TasksChangesBody
import com.sscorp.todo.utils.Converter
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val apiService: TasksApiService,
    private val tasksDao: TasksDao
) {

    suspend fun getTasks(): List<Task> {
        var savedNotes: List<Task>

        try {
            val changes = getChanges()
            if (changes != null) {
                savedNotes =
                    Converter.taskResponsesToTasks(apiService.syncTasks(changes))
                clearChanges()
            } else
                savedNotes = Converter.taskResponsesToTasks(apiService.getTasks())
        } catch (ex: Exception) {
            savedNotes = Converter.taskEntitiesToTasks(tasksDao.loadAll())
        }
        return savedNotes
    }

    private suspend fun getChanges(): TasksChangesBody? {
        val deleted = tasksDao.getDeleted()
        val other = tasksDao.getOther()
        if (deleted.isEmpty() && other.isEmpty())
            return null
        val list = mutableListOf<TaskResponse>()
        for (item in other) {
            val task = Converter.taskEntityToTask(item)
            list.add(Converter.taskToTaskResponse(task))
        }
        return TasksChangesBody(deleted, list)
    }

    private suspend fun clearChanges() {
        tasksDao.clearDeleteStates()
        tasksDao.clearOthers()
    }

    suspend fun loadAllTasksFromDb(): List<Task> {
        val tasks = tasksDao.loadAll()
        return Converter.taskEntitiesToTasks(tasks).sortedDescending()
    }

    suspend fun loadDoneTasksFromDb(): List<Task> {
        val tasks = tasksDao.loadAll().filter { it.done }
        return Converter.taskEntitiesToTasks(tasks).sortedDescending()
    }

    suspend fun updateData(task: Task) {
        val taskEntity = Converter.taskToTaskEntity(task)
        try {
            apiService.updateTask(task.id!!.toString(), Converter.taskToTaskResponse(task))
        } catch (ex: Exception) {
            taskEntity.syncState = "other"
        }
        tasksDao.update(taskEntity)
    }

    suspend fun deleteTask(task: Task) {
        val taskEntity = Converter.taskToTaskEntity(task)
        try {
            apiService.deleteTask(task.id!!.toString())
            tasksDao.delete(taskEntity)
        } catch (ex: Exception) {
            taskEntity.syncState = "deleted"
            tasksDao.update(taskEntity)
        }
    }

    suspend fun addTask(task: Task): Task {
        val taskEntity = Converter.taskToTaskEntity(task)
        val id = tasksDao.save(taskEntity)
        taskEntity.id = id.toInt()
        task.id = id.toInt()
        try {
            apiService.saveTask(Converter.taskToTaskResponse(task))
        } catch (ex: Exception) {
            taskEntity.syncState = "other"
            tasksDao.update(taskEntity)
        }
        return task
    }

    suspend fun updateTask(oldTask: Task, newTask: Task) {
        val taskEntity =  Converter.taskToTaskEntity(newTask, oldTask.id)
        try {
            apiService.updateTask(oldTask.id.toString(), Converter.taskToTaskResponse(newTask))
        } catch (ex: Exception) {
            taskEntity.syncState = "other"
        }
        tasksDao.update(taskEntity)
    }
}
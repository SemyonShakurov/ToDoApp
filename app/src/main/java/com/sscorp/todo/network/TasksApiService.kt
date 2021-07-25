package com.sscorp.todo.network

import retrofit2.http.*

interface TasksApiService {

    @GET("tasks/")
    suspend fun getTasks(): List<TaskResponse>

    @POST("tasks/")
    suspend fun saveTask(@Body task: TaskResponse): TaskResponse

    @PUT("tasks/{task_id}")
    suspend fun updateTask(@Path("task_id") taskId: String, @Body note: TaskResponse): TaskResponse

    @DELETE("tasks/{task_id}")
    suspend fun deleteTask(@Path("task_id") taskId: String): TaskResponse

    @PUT("tasks/")
    suspend fun syncTasks(@Body changes: TasksChangesBody): List<TaskResponse>
}
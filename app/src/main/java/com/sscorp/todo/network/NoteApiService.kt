package com.sscorp.todo.network

import retrofit2.http.*

interface NoteApiService {

    @GET("tasks/")
    suspend fun getNotes(): List<TaskResponse>

    @POST("tasks/")
    suspend fun saveNote(@Body note: TaskResponse): TaskResponse

    @PUT
    suspend fun updateNote(@Url url: String, @Body note: TaskResponse): TaskResponse

    @DELETE
    suspend fun deleteNote(@Url url: String): TaskResponse

    @PUT("tasks/")
    suspend fun syncNotes(@Body changes: NotesChangesBody): List<TaskResponse>
}
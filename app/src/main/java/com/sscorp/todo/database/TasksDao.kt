package com.sscorp.todo.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface TasksDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(task: TaskEntity): Long

    @Query("select * from tasks")
    suspend fun loadAll(): List<TaskEntity>

    @Query("select id from tasks where syncState='deleted'")
    suspend fun getDeleted(): List<Int>

    @Query("select * from tasks where syncState='other'")
    suspend fun getOther(): List<TaskEntity>

    @Query("delete from tasks where syncState='deleted'")
    suspend fun clearDeleteStates()

    @Query("update tasks set syncState = 'synced' where syncState='other'")
    suspend fun clearOthers()

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)
}
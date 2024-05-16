package com.example.taskmanagementsystem.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
    @Query("SELECT * FROM TaskTable ORDER BY taskId DESC")
    //suspend fun getAllTasks(): List<Task>
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM TaskTable WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)
}


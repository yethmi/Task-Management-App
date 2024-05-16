package com.example.taskmanagementsystem.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
    @Query("SELECT * FROM TaskTable ORDER BY taskId DESC")
    suspend fun getAllTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)  // Corrected from insertTasks to insertTask

    @Update
    suspend fun updateTask(task: Task)  // Corrected from updateTasks to updateTask

    @Query("DELETE FROM TaskTable WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)
}


package com.example.taskmanagementsystem

import android.app.Application
import com.example.taskmanagementsystem.data.TaskDatabase
import com.example.taskmanagementsystem.data.TaskRepository

class TaskApplication : Application() {
    val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}
//import android.app.Application
//import com.example.taskmanagementsystem.data.TaskDatabase
//import com.example.taskmanagementsystem.data.TaskRepository
//
//class TaskApplication : Application() {
//    lateinit var repository: TaskRepository
//        private set
//
//    override fun onCreate() {
//        super.onCreate()
//        initializeRepository()
//    }
//
//    private fun initializeRepository() {
//        val database = TaskDatabase.getDatabase(this)
//        repository = TaskRepository(database.taskDao())
//    }
//}

package com.example.taskmanagementsystem.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.allTasks
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

}



//    suspend fun getAllTasks(): List<Task> {
//        return repository.getAllTasks()
//    }
//val allTasks: LiveData<List<Task>> = repository.getAllTasks().asLiveData()
//val allTasks = repository.allTasks

// Method to force a refresh of the tasks
//    fun refreshTasks() {
//        viewModelScope.launch {
//            allTasks.value = repository.getAllTasks()
//        }
//    }
//val allTasks: LiveData<List<Task>> = repository.getAllTasks().asLiveData()

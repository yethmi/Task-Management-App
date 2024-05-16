package com.example.taskmanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagementsystem.data.Task
import com.example.taskmanagementsystem.data.TaskAdapter
import com.example.taskmanagementsystem.data.TaskDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskPriorityEditText: EditText
    private lateinit var taskDeadlineEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskNameEditText = findViewById(R.id.task_name)
        taskDescriptionEditText = findViewById(R.id.task_des)
        taskPriorityEditText = findViewById(R.id.task_priority)
        taskDeadlineEditText = findViewById(R.id.task_deadline)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = TaskAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.add).setOnClickListener {
            addTask()
        }
        findViewById<Button>(R.id.view).setOnClickListener {
            displayTasks()
        }
        findViewById<Button>(R.id.edit).setOnClickListener {
            updateTask()
        }
        findViewById<Button>(R.id.delete).setOnClickListener {
            deleteTask()
        }
    }

    private fun addTask() {
        val taskName = taskNameEditText.text.toString()
        val taskDescription = taskDescriptionEditText.text.toString()
        val taskPriority = taskPriorityEditText.text.toString().toIntOrNull() ?: 0
        val taskDeadline = taskDeadlineEditText.text.toString()

        if (taskName.isNotEmpty() && taskDescription.isNotEmpty() && taskPriority > 0 && taskDeadline.isNotEmpty()) {
            val task = Task(0, taskName, taskDescription, taskPriority, taskDeadline)
            lifecycleScope.launch {
                TaskDatabase.getInstance(applicationContext).taskDao().insertTask(task)
                showToast("Task added successfully")
                clearInputFields()
            }
        } else {
            showToast("Please fill all the fields")
        }
    }

    private fun displayTasks() {
        lifecycleScope.launch {
            val tasks = TaskDatabase.getInstance(applicationContext).taskDao().getAllTasks()
            adapter.updateTasks(tasks)
        }
    }

    private fun updateTask() {
        // The update logic should ideally also check for the task's existence
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialoge, null)
        dialogBuilder.setView(dialogView)

        val edtTaskId = dialogView.findViewById<EditText>(R.id.updateTaskId)
        val edtTaskName = dialogView.findViewById<EditText>(R.id.updateTaskName)
        val edtTaskDescription = dialogView.findViewById<EditText>(R.id.updateTaskDescription)
        val edtTaskPriority = dialogView.findViewById<EditText>(R.id.updateTaskPriority)
        val edtTaskDeadline = dialogView.findViewById<EditText>(R.id.updateTaskDeadline)

        dialogBuilder.setTitle("Update Task")
        dialogBuilder.setPositiveButton("Update") { _, _ ->
            val taskId = edtTaskId.text.toString().toIntOrNull()
            val taskName = edtTaskName.text.toString()
            val taskDescription = edtTaskDescription.text.toString()
            val taskPriority = edtTaskPriority.text.toString().toIntOrNull() ?: 0
            val taskDeadline = edtTaskDeadline.text.toString()

            if (taskId != null && taskName.isNotEmpty() && taskDescription.isNotEmpty() && taskPriority > 0 && taskDeadline.isNotEmpty()) {
                val task = Task(taskId, taskName, taskDescription, taskPriority, taskDeadline)
                lifecycleScope.launch {
                    TaskDatabase.getInstance(applicationContext).taskDao().updateTask(task)
                    showToast("Task updated successfully")
                }
            } else {
                showToast("Please fill all the fields correctly")
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        dialogBuilder.show()
    }

    private fun deleteTask() {
        // Similar to update, should check if the task actually exists
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.delete_dialoge, null)
        dialogBuilder.setView(dialogView)

        val edtTaskId = dialogView.findViewById<EditText>(R.id.deleteTaskId)

        dialogBuilder.setTitle("Delete Task")
        dialogBuilder.setPositiveButton("Delete") { _, _ ->
            val taskId = edtTaskId.text.toString().toIntOrNull()
            if (taskId != null) {
                lifecycleScope.launch {
                    TaskDatabase.getInstance(applicationContext).taskDao().deleteTask(taskId)
                    showToast("Task deleted successfully")
                }
            } else {
                showToast("Invalid Task ID")
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        dialogBuilder.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        taskNameEditText.text.clear()
        taskDescriptionEditText.text.clear()
        taskPriorityEditText.text.clear()
        taskDeadlineEditText.text.clear()
    }
}

package com.example.taskmanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagementsystem.data.Task
import com.example.taskmanagementsystem.data.TaskAdapter
import com.example.taskmanagementsystem.data.TaskViewModel
import com.example.taskmanagementsystem.data.TaskViewModelFactory

class MainActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskPriorityEditText: EditText
    private lateinit var taskDeadlineEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskNameEditText = findViewById(R.id.task_name)
        taskDescriptionEditText = findViewById(R.id.task_des)
        taskPriorityEditText = findViewById(R.id.task_priority)
        taskDeadlineEditText = findViewById(R.id.task_deadline)
        recyclerView = findViewById(R.id.recyclerView)

        setupRecyclerView()

        findViewById<Button>(R.id.add).setOnClickListener {
            addTask()
        }

        // Observe tasks from ViewModel
        taskViewModel.allTasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(emptyList(), this::deleteTaskFromAdapter, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun addTask() {
        val taskName = taskNameEditText.text.toString()
        val taskDescription = taskDescriptionEditText.text.toString()
        val taskPriority = taskPriorityEditText.text.toString().toIntOrNull() ?: 0
        val taskDeadline = taskDeadlineEditText.text.toString()

        if (taskName.isNotEmpty() && taskDescription.isNotEmpty() && taskPriority > 0 && taskDeadline.isNotEmpty()) {
            val task = Task(0, taskName, taskDescription, taskPriority, taskDeadline)
            taskViewModel.insertTask(task)
            showToast("Task added successfully")
            clearInputFields()
        } else {
            showToast("Please fill all the fields")
        }
    }

    @SuppressLint("InflateParams")
    private fun showUpdateDialog(task: Task) {
        val dialogView = layoutInflater.inflate(R.layout.update_dialoge, null)
        val edtTaskId = dialogView.findViewById<EditText>(R.id.updateTaskId)
        val edtTaskName = dialogView.findViewById<EditText>(R.id.updateTaskName)
        val edtTaskDescription = dialogView.findViewById<EditText>(R.id.updateTaskDescription)
        val edtTaskPriority = dialogView.findViewById<EditText>(R.id.updateTaskPriority)
        val edtTaskDeadline = dialogView.findViewById<EditText>(R.id.updateTaskDeadline)

        // Pre-fill the dialog with the task details
        edtTaskId.setText(task.taskId.toString())
        edtTaskName.setText(task.taskName)
        edtTaskDescription.setText(task.taskDes)
        edtTaskPriority.setText(task.priority.toString())
        edtTaskDeadline.setText(task.deadLine)

        val dialogBuilder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setTitle("Update Task")
            setPositiveButton("Update") { _, _ ->
                updateTask(task.copy(
                    taskId = edtTaskId.text.toString().toInt(),
                    taskName = edtTaskName.text.toString(),
                    taskDes = edtTaskDescription.text.toString(),
                    priority = edtTaskPriority.text.toString().toInt(),
                    deadLine = edtTaskDeadline.text.toString()
                ))
            }
            setNegativeButton("Cancel", null)
        }
        dialogBuilder.show()
    }

    private fun updateTask(task: Task) {
        taskViewModel.updateTask(task)
        showToast("Task updated successfully")
    }

    private fun deleteTaskFromAdapter(task: Task) {
        taskViewModel.deleteTask(task.taskId)
        showToast("Task deleted successfully")
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

    override fun onItemClick(task: Task) {
        showUpdateDialog(task)
    }
}

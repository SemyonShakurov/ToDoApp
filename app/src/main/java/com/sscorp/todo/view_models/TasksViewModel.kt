package com.sscorp.todo.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sscorp.todo.models.Task
import com.sscorp.todo.repositories.TasksRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    init {
        viewModelScope.launch {
            _tasks.value = tasksRepository.getTasks()
        }
    }

    fun loadAllTasksFromDb() {
        viewModelScope.launch {
            _tasks.value = tasksRepository.loadAllTasksFromDb()
        }
    }

    fun loadDoneTasksFromDb() {
        viewModelScope.launch {
            _tasks.value = tasksRepository.loadDoneTasksFromDb()
        }
    }

    fun setTaskDone(task: Task) {
        task.done = !task.done
        viewModelScope.launch {
            tasksRepository.updateData(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            launch {
                tasksRepository.deleteTask(task)
            }
            val pos = findTaskPos(task)
            val newList = _tasks.value!!.toMutableList()
            newList.removeAt(pos)
            _tasks.value = newList
        }
    }

    private fun findTaskPos(task: Task): Int {
        var pos: Int? = null
        for ((ind, _task) in _tasks.value!!.withIndex()) {
            if (task === _task) {
                pos = ind
                break
            }
        }
        if (pos == null)
            throw IllegalArgumentException()
        return pos
    }

    fun taskCheck(task: Task) {
        task.done = true
        viewModelScope.launch {
            tasksRepository.updateData(task)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val newTask = tasksRepository.addTask(task)
            val newList = _tasks.value!!.toMutableList()
            newList.add(newTask)
            _tasks.value = newList
        }
    }

    fun updateTask(oldTask: Task, newTask: Task) {
        viewModelScope.launch {
            launch {
                tasksRepository.updateTask(oldTask, newTask)
            }
            val pos = findTaskPos(oldTask)
            val newList = _tasks.value!!.toMutableList()
            newList[pos] = newTask
            _tasks.value = newList
        }
    }
}
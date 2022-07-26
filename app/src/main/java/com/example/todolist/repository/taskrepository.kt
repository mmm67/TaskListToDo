package com.example.todolist.repository

import com.example.todolist.util.Util
import com.example.todolist.database.TaskDao
import com.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun removeTask(task: Task) = taskDao.removeTask(task)

    fun getTasks(searchQuery:String, sortType: Util.sortType): Flow<List<Task>> = taskDao.getTasks(searchQuery, sortType)

}
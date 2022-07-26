package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.util.Util
import com.example.todolist.model.Task
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val taskRepo: TaskRepository) : ViewModel() {

    private val eventChannel = Channel<Util.CreateAndEditTaskEvent>()
    val eventsFlow = eventChannel.receiveAsFlow()

    fun createNewTask(task: Task) =
        viewModelScope.launch {
            taskRepo.insertTask(task)
            eventChannel.send(Util.CreateAndEditTaskEvent.BackToPriorScreen(Util.Constants.taskAdded))
        }


}
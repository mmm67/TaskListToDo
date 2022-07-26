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
class UpdateTaskViewModel @Inject constructor(private val taskRepo: TaskRepository) : ViewModel() {

    private val eventChannel = Channel<Util.CreateAndEditTaskEvent>()
    val eventsFlow = eventChannel.receiveAsFlow()

    fun editTask(task: Task) =
        viewModelScope.launch {
            taskRepo.updateTask(task)
            eventChannel.send(Util.CreateAndEditTaskEvent.BackToPriorScreen(Util.Constants.taskEdited))
        }


}
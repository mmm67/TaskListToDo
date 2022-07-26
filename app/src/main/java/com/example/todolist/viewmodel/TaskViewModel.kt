package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.util.Util
import com.example.todolist.datastore.DataStorePreferenceManager
import com.example.todolist.model.Task
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepo: TaskRepository,
    private val dataStorePreference: DataStorePreferenceManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val eventChannel = Channel<Event>()
    val eventsFlow = eventChannel.receiveAsFlow()


    // Send query to database as soon  received a search query or other condition as possible
    private val _taskFlow = combine(searchQuery, dataStorePreference.sortTypeFlow) { query, type ->
        Pair(query, type)
    }.flatMapLatest {

        taskRepo.getTasks(it.first, Util.sortType.valueOf(it.second))

    }

    fun updateOrderType(type: Util.sortType) = viewModelScope.launch {
        dataStorePreference.setSortType(type.name)
    }

    fun updateCompleteStatus(task: Task) {

        viewModelScope.launch {
            taskRepo.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepo.removeTask(task)
            eventChannel.send(Event.ShowSnackBar(task))
        }
    }

    fun undoDeletedTask(task: Task) {
        viewModelScope.launch {
            taskRepo.insertTask(task)
        }
    }

    fun onAddTaskButtonClicked() = viewModelScope.launch {
        eventChannel.send(Event.ShowCreateTaskFragment)
    }




    val taskList = _taskFlow.asLiveData()

    sealed class Event {
        data class ShowSnackBar(val task: Task) : Event()
        object ShowCreateTaskFragment : Event()
    }
}
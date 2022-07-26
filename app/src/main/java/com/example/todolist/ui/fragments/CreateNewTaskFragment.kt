package com.example.todolist

import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentCreateNewTaskBinding
import com.example.todolist.model.Task
import com.example.todolist.util.Util
import com.example.todolist.viewmodel.CreateTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CreateNewTaskFragment : Fragment(R.layout.fragment_create_new_task) {


    private val addTaskViewModel: CreateTaskViewModel by viewModels() // delegate initialization to ViewModel
    private lateinit var ondate: OnDateSetListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)

        binding.apply {
            addDeadline.setOnClickListener {
                ondate =
                    OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        this.addDeadline.setText(
                            dayOfMonth.toString() + "-" + (monthOfYear + 1).toString()
                                    + "-" + year.toString()
                        )
                    }
                parentFragmentManager?.let {manager->
                    Util.DatePickerFragment().showDatePicker(manager, ondate)
                }
            }
            btnCreateTask.setOnClickListener {
                addTaskViewModel.createNewTask(
                    Task(
                        0,
                        name.text.toString().toLowerCase(),
                        important.isChecked,
                        false,
                        addDeadline.text.toString()
                    )
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addTaskViewModel.eventsFlow.collect{event->
                when(event){
                    is Util.CreateAndEditTaskEvent.BackToPriorScreen ->{
                        setFragmentResult(
                            "result",
                            bundleOf("result" to event.message)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }



}
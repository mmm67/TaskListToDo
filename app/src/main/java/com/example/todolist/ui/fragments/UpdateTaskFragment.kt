package com.example.todolist.ui.fragments

import android.app.DatePickerDialog
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
import androidx.navigation.fragment.navArgs
import com.example.todolist.util.Util
import com.example.todolist.R
import com.example.todolist.databinding.FragmentUpdateTaskBinding
import com.example.todolist.viewmodel.UpdateTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UpdateTaskFragment : Fragment(R.layout.fragment_update_task) {


    private val updateTaskViewModel: UpdateTaskViewModel by viewModels() // delegate initialization to ViewModel
    private val args: UpdateTaskFragmentArgs by navArgs()
    private lateinit var ondate: DatePickerDialog.OnDateSetListener
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)

        
        binding.editName.setText( args.task.name)
        binding.editDeadline.setText(args.task.deadLine)
        binding.editImportant.isChecked =args.task.importance


        binding.apply {
            editDeadline.setOnClickListener {
                ondate =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        this.editDeadline.setText(
                            dayOfMonth.toString() + "-" + (monthOfYear + 1).toString()
                                    + "-" + year.toString()
                        )
                    }
                parentFragmentManager?.let { manager->

                    Util.DatePickerFragment().showDatePicker(manager, ondate)
                }
            }

            btnEditTask.setOnClickListener {
                updateTaskViewModel.editTask(
                    args.task.copy(
                        name = editName.text.toString(),
                        importance = editImportant.isChecked,
                        deadLine = editDeadline.text.toString()
                    )
                )
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            updateTaskViewModel.eventsFlow.collect{event->
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
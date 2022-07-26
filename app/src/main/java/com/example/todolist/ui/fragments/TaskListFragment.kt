package com.example.todolist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.adapters.TaskListAdapter
import com.example.todolist.util.Util
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.model.Task
import com.example.todolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TaskListFragment : Fragment(R.layout.fragment_task_list), MyOnCLickListener {

    private val taskViewModel: TaskViewModel by viewModels() // delegate initialization to ViewModel
    private lateinit var taskAdapter: TaskListAdapter

    //enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskListBinding.inflate(inflater, container, false)
        taskAdapter = TaskListAdapter(this@TaskListFragment)
        var swipeToDeleteCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direct: Int) {
                taskViewModel.deleteTask(taskAdapter.currentItem(viewHolder.adapterPosition))
            }
        }
        binding.apply {
            ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(taskListRcv)
            taskListRcv.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)  // Because the recyclerview has fixed width & height
            }
            btnAddTask.setOnClickListener{
                closeSearchView()
               taskViewModel.onAddTaskButtonClicked()
            }
            return binding.root
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskViewModel.taskList.observe(viewLifecycleOwner) { task_list ->
            taskAdapter.updateTaskList(task_list)
        }
        setFragmentResultListener("result") { _, bundle ->
            val result = bundle.getString("result")
            Snackbar.make(view, result.toString(),
                Snackbar.LENGTH_SHORT).show()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            taskViewModel.eventsFlow.collect{event->
                when(event){
                    is TaskViewModel.Event.ShowSnackBar->{
                         Snackbar.make(view, "task removed",
                            Snackbar.LENGTH_LONG).setAction("undo"){
                                taskViewModel.undoDeletedTask(event.task)
                        }.show()
                    }
                    is TaskViewModel.Event.ShowCreateTaskFragment ->{
                        findNavController().navigate(R.id.action_taskListFragment_to_createNewTaskFragment2)
                    }
                }
            }
        }

    }

    private lateinit var searchView: SearchView
    //inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val menuItem = menu.findItem(R.id.search_task)
         searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    taskViewModel.searchQuery.value = newText.orEmpty()
                    System.out.println("Value=   "+taskViewModel.searchQuery.value)
                    return true
                }

            }
        )
    }

    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_deadline -> {
                taskViewModel.updateOrderType(Util.sortType.SORT_BY_DAEDLINE)
                true
            }
            R.id.sort_by_name -> {
                taskViewModel.updateOrderType(Util.sortType.SORT_BY_NAME)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateCompleteStatus(task: Task) {
        taskViewModel.updateCompleteStatus(task)
    }

    override fun taskSelectedToEdit(task: Task) {
        val action = TaskListFragmentDirections.actionTaskListFragmentToUpdateTaskFragment2(task)
        findNavController().navigate(action)
    }

    override fun closeSearchView() {

        if (!searchView.isIconified()) {
            searchView.setIconified(true)

        }
    }

}

interface MyOnCLickListener {
    fun updateCompleteStatus(task: Task)
    fun taskSelectedToEdit(task:Task)
    fun closeSearchView()
}
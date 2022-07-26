package com.androiddevs.mvvmnewsapp.adapters

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.SingleTaskBinding
import com.example.todolist.model.Task
import com.example.todolist.ui.fragments.MyOnCLickListener


class TaskListAdapter(private val listener: MyOnCLickListener) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: SingleTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {

                isCompletedOrNot.apply {
                    isChecked = task.completion
                    setOnClickListener {
                        listener.updateCompleteStatus(task.copy(completion = isCompletedOrNot.isChecked))
                    }
                }
                if (task.deadLine.isEmpty())
                    deadline.text = ""
                else
                    deadline.text = task.deadLine
                if (task.importance && !isCompletedOrNot.isChecked) {
                    val content = SpannableString(task.name)
                    content.setSpan(
                        UnderlineSpan(),
                        0,
                        content.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    taskName.text = content
                } else
                    taskName.text = task.name

            }
        }
    }

    // Define an anonymous callback and get instance of it and then store it in the variable diffUtilCallBack"
    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    // To calculate difference of two lists on background thread because lists may be so large
    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = SingleTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    fun currentItem(index: Int): Task {
        return asyncDiffer.currentList[index]
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(asyncDiffer.currentList[position])
        holder.apply {
            itemView.setOnClickListener {
                listener.closeSearchView()
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.taskSelectedToEdit(asyncDiffer.currentList.get(adapterPosition))
                holder.bind(asyncDiffer.currentList[adapterPosition])
            }
        }

    }

    fun updateTaskList(newTaskList: List<Task?>?) {
        if (newTaskList != null)
            asyncDiffer.submitList(newTaskList)

    }
}








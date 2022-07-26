package com.example.todolist.util

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

object Util {

    object Constants {
        const val databaseName: String = "task_database"
        const val tableName: String = "task_table"
        const val USER_PREFERENCES_NAME = "task_preferences"
        const val taskAdded = "Task is added successfully"
        const val taskEdited = "Task is Edited successfully"
    }

    enum class sortType{
        SORT_BY_NAME,
        SORT_BY_DAEDLINE
    }
    sealed class CreateAndEditTaskEvent {
        data class BackToPriorScreen(val message:String) : CreateAndEditTaskEvent()
    }


    class DatePickerFragment : DialogFragment() {
        var ondateSet: DatePickerDialog.OnDateSetListener? = null
        private var year = 0
        private var month = 0
        private var day = 0

        private fun setCallBack(ondate: DatePickerDialog.OnDateSetListener?) {
            ondateSet = ondate
        }

        companion object GetDate{
            fun getDate(): String{
                val calender = Calendar.getInstance()

                val year = calender[Calendar.YEAR].toString()
                val month = calender[Calendar.MONTH].toString()
                val day = calender[Calendar.DAY_OF_MONTH].toString()

                return "$day-$month-$year"
            }
        }
        @SuppressLint("NewApi")
        override fun setArguments(args: Bundle?) {
            super.setArguments(args)
            year = args!!.getInt("year")
            month = args.getInt("month")
            day = args.getInt("day")
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return DatePickerDialog(requireActivity(), ondateSet, year, month, day)
        }

        fun showDatePicker(manager: FragmentManager, ondate: DatePickerDialog.OnDateSetListener?) {

            /**
             * Set Up Current Date Into dialog
             */
            val calender = Calendar.getInstance()
            val args = Bundle()
            args.putInt("year", calender[Calendar.YEAR])
            args.putInt("month", calender[Calendar.MONTH])
            args.putInt("day", calender[Calendar.DAY_OF_MONTH])
            this.arguments = args
            /**
             * Set Call back to capture selected date
             */
            setCallBack(ondate)
            show(manager, "Date Picker")
        }
    }
}
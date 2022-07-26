package com.example.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.model.Task
import com.example.todolist.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [Task::class], version = 1)
abstract class TaskDataBase() : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao


    // To pre-populate the database with dummy data
    class TaskCallBack @Inject constructor(
        private val taskDao: Provider<TaskDao>
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                populateDatabase()
            }
        }

        private suspend fun populateDatabase() {

              taskDao.get().insertTask(Task(0, "call mom",  true,
                  completion = false, deadLine = Util.DatePickerFragment.getDate()
              ))
            taskDao.get().insertTask(Task(0, "do homework",  true,
                completion = false, deadLine = Util.DatePickerFragment.getDate()
            ))
            taskDao.get().insertTask(Task(0, "go shopping",  false,
                completion = false, deadLine = Util.DatePickerFragment.getDate()
            ))
        }
    }
}
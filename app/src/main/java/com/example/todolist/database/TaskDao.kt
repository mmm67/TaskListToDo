package com.example.todolist.database

import androidx.room.*
import com.example.todolist.util.Util
import com.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update()
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun removeTask(task: Task)


    fun getTasks(searchQuery: String, sortType: Util.sortType): Flow<List<Task>> {
        return when (sortType) {
          Util.sortType.SORT_BY_NAME -> getTasksByName(searchQuery)
            Util.sortType.SORT_BY_DAEDLINE -> getTasksByDeadline(searchQuery)
            else -> getTasksByName(searchQuery)

        }
    }

    @Query("SELECT * FROM " + Util.Constants.tableName + " WHERE name LIKE '%' ||:searchQuery || '%' ORDER BY name asc")
     fun getTasksByName(searchQuery: String): Flow<List<Task>>

    @Query("SELECT * FROM " + Util.Constants.tableName + " WHERE name LIKE '%' ||:searchQuery || '%' ORDER BY deadline asc")
    fun getTasksByDeadline(searchQuery: String): Flow<List<Task>>
}
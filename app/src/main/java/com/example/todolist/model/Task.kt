package com.example.todolist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.util.Util
import kotlinx.android.parcel.Parcelize

@Entity(tableName = Util.Constants.tableName)
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)  val pk: Int,
    @ColumnInfo(name= "name")  val name: String ="",
    @ColumnInfo(name= "importance")  val importance: Boolean = false,
    @ColumnInfo(name= "completion")  var completion: Boolean= false,
    @ColumnInfo(name= "deadLine")   val deadLine: String = ""
):Parcelable

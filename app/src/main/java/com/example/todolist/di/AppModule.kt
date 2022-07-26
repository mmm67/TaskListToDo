package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.database.TaskDataBase
import com.example.todolist.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // tell hilt to create one instance from database
    @Provides
    fun provideTaskDatabase(
        @ApplicationContext app: Context,
        callBack: TaskDataBase.TaskCallBack
    ) = Room.databaseBuilder(
        app,
        TaskDataBase::class.java,
        Util.Constants.databaseName
    ).fallbackToDestructiveMigration()
        .addCallback(callBack)
        .build()

    @Provides
    fun provideTaskDao(db: TaskDataBase) = db.getTaskDao()
}
package com.example.todolist.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore(Util.Constants.USER_PREFERENCES_NAME)

@Singleton
class DataStorePreferenceManager @Inject constructor(@ApplicationContext appContext: Context) {


    private val taskDataStore = appContext.dataStore

    suspend fun setSortType(_sortType: String) = taskDataStore.edit { preference ->
            preference[sortType] = _sortType
        }


    val sortTypeFlow: Flow<String> = taskDataStore.data.map { preferences ->
        preferences[sortType] ?: Util.sortType.SORT_BY_NAME.name
    }

    private companion object{
        val sortType = stringPreferencesKey("sort_type")
    }



}
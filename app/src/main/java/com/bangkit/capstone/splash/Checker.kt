package com.bangkit.capstone.splash

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class PassChecker{
    DONE, UNDONE
}

class Checker(context: Context) {
    private val mDataStore: DataStore<Preferences> = context.dataStore

    val isDoneFlow: Flow<PassChecker> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { check ->
            when(check[IS_DONE]?: false) {
                true -> PassChecker.DONE
                false -> PassChecker.UNDONE
            }
        }

    suspend fun setDone(passChecker: PassChecker) {
        mDataStore.edit { check ->
            check[IS_DONE] = when(passChecker) {
                PassChecker.UNDONE -> false
                PassChecker.DONE -> true
            }
        }
    }


    companion object {
        val IS_DONE = booleanPreferencesKey("is_done")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("check")
    }
}
package com.joinhub.khataapp.Utilites

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


val Context.dataStore by preferencesDataStore("Settings")

class ThemeDataStore ( context:Context ){

    private val dataStore = context.dataStore

    object PreferencesKeys{

        val name =intPreferencesKey("theme")
    }


    val readOutFromDataStore: Flow<String> = dataStore.data
        .catch {
            if(this is IOException){
                emit(emptyPreferences())

            }

        }.map{preference->

            (preference[PreferencesKeys.name]?:"1").toString()

        }

    suspend fun saveToDataStore(name: Int)
    {

        dataStore.edit { preference->
            preference[PreferencesKeys.name]=name
        }
    }


}
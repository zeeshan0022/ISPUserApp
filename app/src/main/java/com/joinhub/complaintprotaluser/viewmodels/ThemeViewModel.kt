package com.joinhub.complaintprotaluser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.joinhub.khataapp.Utilites.ThemeDataStore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel (application: Application) :  AndroidViewModel(application) {

    private val dataStoreSetting = ThemeDataStore(application)
  val readFromDataStore= dataStoreSetting.readOutFromDataStore.asLiveData()

    fun saveToDataStore(name: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSetting.saveToDataStore(name)
        }
    }
}

package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlankViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _data = MutableLiveData<String>("Initial data")
    val data: LiveData<String> = _data

    fun updateData(newData: String) {
        _data.value = newData
    }
}
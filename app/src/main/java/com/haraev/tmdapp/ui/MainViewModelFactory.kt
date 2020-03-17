package com.haraev.tmdapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haraev.core.data.SessionLocalDataSource
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != MainViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return MainViewModel(sessionLocalDataSource) as T
    }
}
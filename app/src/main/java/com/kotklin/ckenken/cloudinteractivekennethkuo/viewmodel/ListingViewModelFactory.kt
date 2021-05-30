package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListingViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListingViewModel::class.java)) {
            return ListingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
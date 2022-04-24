package com.example.projet_coditty_goubin.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projet_coditty_goubin.database.UserDao
import com.example.projet_coditty_goubin.viewModel.AccueilViewModel

class AccueilViewModelFactory (
    private val dataSource: UserDao,
    private val application: Application,
    private val userID: Long = 0L // userID
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccueilViewModel::class.java)) {
            return AccueilViewModel(dataSource, application,userID) as T //
            userID
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
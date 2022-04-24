package com.example.projet_coditty_goubin.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projet_coditty_goubin.database.CardDao
import com.example.projet_coditty_goubin.database.ExplicationDao
import com.example.projet_coditty_goubin.database.UserDao
import com.example.projet_coditty_goubin.model.User
import com.example.projet_coditty_goubin.viewModel.GameViewModel

class GameViewModelFactory (
    private val dataSource: CardDao,
    private val dataSource2: UserDao,
    private val dataSource3: ExplicationDao,
    private val user: User,
    private val application: Application,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(dataSource, dataSource2, dataSource3, user, application) as T //
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.projet_coditty_goubin.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projet_coditty_goubin.api.MyApi
import com.example.projet_coditty_goubin.database.UserDao
import com.example.projet_coditty_goubin.event.Event
import com.example.projet_coditty_goubin.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListUserViewModel(
    val database: UserDao,
    application: Application,
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _response = MutableLiveData<List<User>>()

    val response: LiveData<List<User>>
        get() = _response

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        Log.i("ListLocalisViewModel", "created")
        initializeUsers()
    }

    private fun initializeUsers() {
        uiScope.launch {
            var getPropertiesDeferred = MyApi.retrofitService.getUsers()
            try {
                var listResult = getPropertiesDeferred.await()
                _response.value = listResult

                for (i in 0..listResult.size) {
                    var user = User()
                    user.id = _response.value?.get(i)!!.id
                    user.pseudo = _response.value?.get(i)!!.pseudo
                    user.score = _response.value?.get(i)!!.score
                    user.genre = _response.value?.get(i)!!.genre
                    if (database.get(user.id) == null) {
                        database.insert(user)
                    }
                    _users.value = database.getUsers()

                }
            } catch (e: Exception) {
                if (database.getUsers().isEmpty()) statusMessage.value =
                    Event("Vous n'êtes pas connecté à l'API !")
                _response.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ListUserViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
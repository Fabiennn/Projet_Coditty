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
import kotlinx.coroutines.*

class AccueilViewModel(
    val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    private val _changeGender = MutableLiveData<String>()
    val changeGender: LiveData<String>
        get() = _changeGender

    private val _navigateToAccueil = MutableLiveData<User>()
    val navigateToAccueil: LiveData<User>
        get() = _navigateToAccueil


    init {
        Log.i("AccueilViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUserFromDatabase()
        }
    }

    private suspend fun getUserFromDatabase(): User? {
        return withContext(Dispatchers.IO) {
            var user = database.get(userID) // userID
            if (user == null) {
                user = User()
            }
            user
        }
    }

    fun onGender(gender: String) {
        _user.value?.genre = gender
        _changeGender.value = gender
    }

    fun validate() {
        uiScope.launch {
            val user = user.value ?: return@launch
            if (user.pseudo.isNullOrEmpty()) {
                statusMessage.value = Event("Vous devez indiquer un pseudo")
                return@launch
            }
            if (user.genre.isNullOrEmpty()) {
                statusMessage.value = Event("Vous devez selectionner un genre")
                return@launch
            }
            var getPropertiesDeferred =
                user.let { MyApi.retrofitService.createUser(it) }
            try {
                var userCreated = getPropertiesDeferred.await()
                //database.insert(userCreated)
                _navigateToAccueil.value = userCreated
            } catch (e: Exception) {
                if (database.getUsers().isEmpty()) statusMessage.value =
                    Event("Vous n'êtes pas connecté à l'API, ou cette dernière est en train de se lancer")
                print(e.message)
            }
        }
    }

    fun doneNavigating() {
        _navigateToAccueil.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("AccueilViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
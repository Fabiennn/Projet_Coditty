package com.example.projet_coditty_goubin.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projet_coditty_goubin.api.MyApi
import com.example.projet_coditty_goubin.database.CardDao
import com.example.projet_coditty_goubin.database.ExplicationDao
import com.example.projet_coditty_goubin.database.UserDao
import com.example.projet_coditty_goubin.model.Card
import com.example.projet_coditty_goubin.model.Explication
import com.example.projet_coditty_goubin.model.User
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.lang.Exception
import kotlin.math.roundToInt

class GameViewModel (
    val database: CardDao,
    val databaseUser: UserDao,
    val databaseExplication: ExplicationDao,
    val user: User,
    application: Application,
) : AndroidViewModel(application) {

    private val listCardId = arrayListOf<Long>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card>
        get() = _card

    private val _isReady = MutableLiveData<String>()
    val isReady: LiveData<String>
        get() = _isReady

    private var explicationId = 1L

    private val _explication = MutableLiveData<Explication>()
    val explication: LiveData<Explication>
        get() = _explication


    private var giveExplication = true

    private var valueFonte = 3F
    private var valueDeath = 0
    private var valueTemperature = 20F
    private var valueHealth = 50F
    private var score = 0



    init {
        Log.i("GameViewModel", "created")
        initializeCard()
        initializeExplication()
    }

    private fun initializeCard() {
        uiScope.launch {
            var getPropertiesDeferred = MyApi.retrofitService.getCards()
            try {
                var listResult = getPropertiesDeferred.await()
                for (i in 0..listResult.size) {
                    database.insert(listResult[i])
                }
            } catch (e : Exception) {

            }
        }

    }

    fun getNextCard(): Card? {
        if (listCardId.size == 10) {
            _card.value!!.description = "C'est la fin du jeu ! J'espère que ça vous aura plu et que vous aurez appris des choses"
        } else {
            _card.value = database.getRandomNotIn(listCardId)
        }
        return _card.value
    }

    fun getExplication() : String? {
        if (listCardId.size == 10) {
            return "C'est la fin du jeu ! J'espère que ça vous aura plu et que vous aurez appris des choses"
        }
        listCardId.add(_card.value!!.id)
        return _card.value!!.explication
    }

    fun initializeExplication() {
        var explication1 = Explication(explicationId, "Vous etes le roi du royaume, une terrible crise environnementale s'est abattue sur le pays, seul vous êtes en mesure de sauver le monde")
        var explication2 = Explication(2L, "Vous allez avoir tout un tas de décisions à prendre (10 au total) parmis de nombreuses possibles. Vos décisions affecteront 4 critères représentés juste au dessus.")
        var explication3 = Explication(3L, "Vous etes le roi du royaume")
        var explication4 = Explication(4L, "Vous etes le roi du royfghaume")
        databaseExplication.insert(explication1)
        databaseExplication.insert(explication2)
        databaseExplication.insert(explication3)
        databaseExplication.insert(explication4)
    }


    fun getNextExplication() : Explication? {
        if (explicationId == 4L) {
            getNextCard()
            return null
        } else {
            var explicationToReturn = databaseExplication.get(explicationId)
            explicationId = explicationId + 1L
            return explicationToReturn
        }
    }

    fun getGiveExplication() : Boolean {
        return giveExplication
    }

    fun setGiveExplication(value : Boolean) {
        giveExplication = value
    }

    fun getFonte(): Float {
        return valueFonte
    }

    fun getHealth(): Float {
        return valueHealth
    }

    fun getTemperature(): Float {
        return valueTemperature
    }

    fun getDeath(): Int {
        return valueDeath
    }

    fun getScore(): Int {
        return user.score
    }

    fun setFonte(value: Float) {
        valueFonte = value
    }

    fun setDeath(value: Int) {
        valueDeath = value
    }

    fun setTemperature(value: Float) {
        valueTemperature = value
    }

    fun setHealth(value: Float) {
        valueHealth = value
    }

    fun getListSize() : Int {
        return listCardId.size
    }

    fun deleteData() {
        uiScope.launch {
            database.deleteData()
            databaseExplication.deleteData()
            databaseUser.deleteData()
        }
    }

    fun applyScore(case : Boolean) {
        if (case == true) {
            // apply yes
            valueFonte += card.value!!.yesFonte
            valueHealth += card.value!!.yesHealth
            valueTemperature -= card.value!!.yesTemperature
            valueDeath += card.value!!.yesDeath
        } else {
            // apply false
            valueFonte += card.value!!.noFonte
            valueHealth -= card.value!!.noHealth
            valueTemperature += card.value!!.noTemperature
            valueDeath += card.value!!.noDeath
        }
        user.score = (valueHealth + valueFonte + valueTemperature - valueDeath/1000).roundToInt()
        uiScope.launch {
            var getPropertiesDeferred =
                user?.let { MyApi.retrofitService.createUser(it) }
            try {
                getPropertiesDeferred?.await()
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }



}
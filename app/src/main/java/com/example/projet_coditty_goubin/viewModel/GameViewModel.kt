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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameViewModel(
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
            } catch (e: Exception) {

            }
        }

    }

    fun getNextCard(): Card? {
        if (listCardId.size == 10) {
            _card.value!!.description =
                "C'est la fin du jeu ! J'espère que ça vous aura plu et que vous aurez appris des choses"
        } else {
            _card.value = database.getRandomNotIn(listCardId)
        }
        return _card.value
    }

    fun getExplication(): String? {
        if (listCardId.size == 10) {
            return "C'est la fin du jeu ! J'espère que ça vous aura plu et que vous aurez appris des choses"
        }
        listCardId.add(_card.value!!.id)
        return _card.value!!.explication
    }

    fun initializeExplication() {
        var explication1 = Explication(
            explicationId,
            "Vous êtes le roi du royaume, une terrible crise environnementale s'est abattue sur le pays, seul vous êtes en mesure de pouvoir sauver le monde."
        )
        var explication2 = Explication(
            2L,
            "Vous allez avoir tout un tas de décisions à prendre, 10 au total. Vos décisions affecteront 4 critères représentés juste au dessus. Dans l'ordre il y a la température de la terre, le pourcentage de fonte des glaces, la santé génrale de la Terre, et le nombre de morts"
        )
        var explication3 = Explication(
            3L,
            "Chaque question sera suivi d'une explication sur la situation actuelle, dans le but de faire connaître des informations importantes sur notre monde"
        )
        var explication4 = Explication(
            4L,
            "Vous êtes prêt ? Alors c'est parti, amusez-vous et bonne chance surtout !"
        )
        databaseExplication.insert(explication1)
        databaseExplication.insert(explication2)
        databaseExplication.insert(explication3)
        databaseExplication.insert(explication4)
    }


    fun getNextExplication(): Explication? {
        if (explicationId == 5L) {
            getNextCard()
            return null
        } else {
            var explicationToReturn = databaseExplication.get(explicationId)
            explicationId = explicationId + 1L
            return explicationToReturn
        }
    }

    fun getGiveExplication(): Boolean {
        return giveExplication
    }

    fun setGiveExplication(value: Boolean) {
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

    fun getListSize(): Int {
        return listCardId.size
    }

    fun deleteData() {
        uiScope.launch {
            database.deleteData()
            databaseExplication.deleteData()
            databaseUser.deleteData()
        }
    }

    fun applyScore(case: Boolean) {
        if (case == true) {
            // apply case yes
            valueFonte += card.value!!.yesFonte
            valueHealth += card.value!!.yesHealth
            valueTemperature -= card.value!!.yesTemperature
            valueDeath += card.value!!.yesDeath
        } else {
            // apply case no
            valueFonte += card.value!!.noFonte
            valueHealth -= card.value!!.noHealth
            valueTemperature += card.value!!.noTemperature
            valueDeath += card.value!!.noDeath
        }
        user.score = (valueHealth + valueFonte + valueTemperature - valueDeath / 1000).roundToInt() // calculate score
        uiScope.launch {
            var getPropertiesDeferred =
                user.let { MyApi.retrofitService.createUser(it) }
            try {
                getPropertiesDeferred.await()
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }


}
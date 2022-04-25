package com.example.projet_coditty_goubin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projet_coditty_goubin.R
import com.example.projet_coditty_goubin.database.DatabaseCard
import com.example.projet_coditty_goubin.database.DatabaseExplication
import com.example.projet_coditty_goubin.database.DatabaseUser
import com.example.projet_coditty_goubin.databinding.FragmentGameBinding
import com.example.projet_coditty_goubin.model.User
import com.example.projet_coditty_goubin.viewModel.GameViewModel
import com.example.projet_coditty_goubin.viewModelFactory.GameViewModelFactory

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val args = GameFragmentArgs.fromBundle(requireArguments())

        val dataSource = DatabaseUser.getInstance(application).userDao
        val dataSource2 = DatabaseCard.getInstance(application).cardDao
        val dataSource3 = DatabaseExplication.getInstance(application).explicationDao
        val viewModelFactory =
            GameViewModelFactory(dataSource2, dataSource, dataSource3, args.user, application)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game,
            container, false
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.viewModel = viewModel

        binding.apply {
            btNo.text = getString(R.string.non)
            btYes.text = getString(R.string.oui)
            btNo.visibility = View.GONE
            btYes.visibility = View.GONE
            btValider.text = getString(R.string.validate)
            user.text = "Souverain : " + "\n" + args.user.pseudo
            cardText.text = getString(R.string.initialiser)
            binding.score.visibility = View.GONE
            binding.score.text = getString(R.string.score) + "0"
            binding.btQuitter.visibility = View.GONE
            binding.btQuitter.text = getString(R.string.quitter)

            binding.textDeath.text = viewModel!!.getDeath().toString()
            binding.textTemperature.text = viewModel!!.getTemperature().toString() + "°C"
            binding.textHealth.text = viewModel!!.getHealth().toString() + "%"
            binding.textFonte.text = viewModel!!.getFonte().toString() + "%"
        }
        binding.lifecycleOwner = this

        viewModel.card.observe(viewLifecycleOwner, Observer {
            binding.btValider.visibility = View.GONE
            binding.btYes.visibility = View.VISIBLE
            binding.btNo.visibility = View.VISIBLE
            binding.cardText.text = it!!.description
            binding.score.visibility = View.VISIBLE
        })

        binding.btQuitter.setOnClickListener {
            viewModel.deleteData()
            this.findNavController()
                .navigate(GameFragmentDirections.actionGameFragmentToAccueilFragment())
        }

        binding.btValider.setOnClickListener {
            boucleExplication()
        }

        binding.btYes.setOnClickListener {
            boucleJeuYes()
        }

        binding.btNo.setOnClickListener {
            boucleJeuNo()


        }

        return binding.root

    }


    private fun boucleJeuYes() {
        if (viewModel.getGiveExplication()) {
            viewModel.applyScore(true)
            if (viewModel.getListSize() != 10) updateScore()
            else afficherQuitter()
            nextExplication()
        } else nextCard()
    }


    private fun boucleJeuNo() {
        if (viewModel.getGiveExplication()) {
            viewModel.applyScore(false)
            if (viewModel.getListSize() != 10) updateScore()
            else afficherQuitter()
            nextExplication()
        } else nextCard()
    }

    private fun nextExplication() {
        var nextExplication = viewModel.getExplication()
        binding.cardText.text = nextExplication
        viewModel.setGiveExplication(false)
    }

    private fun nextCard() {
        var nextCard = viewModel.getNextCard()
        binding.cardText.text = nextCard!!.description
        viewModel.setGiveExplication(true)
    }

    private fun boucleExplication() {
        var explication = viewModel.getNextExplication()
        if (explication != null) binding.cardText.text = explication.description
    }

    private fun updateScore() {
        binding.textDeath.text = viewModel.getDeath().toString()
        binding.textTemperature.text = viewModel.getTemperature().toString() + "°C"
        binding.textHealth.text = viewModel.getHealth().toString() + "%"
        binding.textFonte.text = viewModel.getFonte().toString() + "%"
        binding.score.text = getString(R.string.score) + viewModel.getScore().toString()
    }

    private fun afficherQuitter() {
        binding.btNo.visibility = View.GONE
        binding.btYes.visibility = View.GONE
        binding.btQuitter.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        @BindingAdapter("setImage")
        fun ImageView.setUserImage(item: User) {
            if (item.genre == "Homme") setImageResource(R.mipmap.roi)
            else setImageResource(R.mipmap.reine)
        }

        @JvmStatic
        @BindingAdapter("pseudo")
        fun TextView.setPseudo(item: User) {
            text = item.pseudo
        }

        @JvmStatic
        @BindingAdapter("score")
        fun TextView.setScore(item: User) {
            text = item.score.toString()
        }


    }
}
package com.example.projet_coditty_goubin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projet_coditty_goubin.R
import com.example.projet_coditty_goubin.database.DatabaseUser
import com.example.projet_coditty_goubin.databinding.FragmentAccueilBinding
import com.example.projet_coditty_goubin.viewModel.AccueilViewModel
import com.example.projet_coditty_goubin.viewModelFactory.AccueilViewModelFactory

class AccueilFragment : Fragment() {
    private lateinit var binding: FragmentAccueilBinding
    private lateinit var viewModel: AccueilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseUser.getInstance(application).userDao
        val viewModelFactory = AccueilViewModelFactory(dataSource, application)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_accueil,
            container, false
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(AccueilViewModel::class.java)
        binding.viewModel = viewModel

        binding.apply {
            tvBienvenue.text = getString(R.string.bienvenue)
            tiPseudo.hint = getString(R.string.pseudo)
            btPlay.text = getString(R.string.play)
            btBestScore.text = getString(R.string.bestScore)
        }
        binding.lifecycleOwner = this

        viewModel.changeGender.observe(viewLifecycleOwner, Observer {
            setPictureGender(viewModel.changeGender.value!!)

        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.btBestScore.setOnClickListener {
            this.findNavController().navigate(
                AccueilFragmentDirections.actionAccueilFragmentToListUserFragment()
            )
        }

        viewModel.navigateToAccueil.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                this.findNavController().navigate(
                    AccueilFragmentDirections
                        .actionAccueilFragmentToGameFragment(user)
                )
                viewModel.doneNavigating()
            }
        })

        return binding.root

    }

    fun setPictureGender(gender: String) {
        if (gender == "Homme") binding.personnage.setImageResource(R.mipmap.roi)
        else binding.personnage.setImageResource(R.mipmap.reine)
    }
}
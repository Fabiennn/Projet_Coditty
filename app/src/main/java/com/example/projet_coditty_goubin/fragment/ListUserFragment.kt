package com.example.projet_coditty_goubin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projet_coditty_goubin.R
import com.example.projet_coditty_goubin.adapter.UserAdapter
import com.example.projet_coditty_goubin.adapter.UserListener
import com.example.projet_coditty_goubin.database.DatabaseUser
import com.example.projet_coditty_goubin.databinding.FragmentListUserBinding
import com.example.projet_coditty_goubin.viewModel.ListUserViewModel
import com.example.projet_coditty_goubin.viewModelFactory.ListUserViewModelFactory


class ListUserFragment : Fragment() {
    private lateinit var binding: FragmentListUserBinding
    private lateinit var viewModel: ListUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseUser.getInstance(application).userDao
        val viewModelFactory = ListUserViewModelFactory(dataSource, application)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_user,
            container, false
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ListUserViewModel::class.java)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        val adapter = UserAdapter(UserListener { localisationId ->

        })

        binding.list.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root

    }
}
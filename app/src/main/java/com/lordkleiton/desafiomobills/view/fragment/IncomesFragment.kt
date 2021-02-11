package com.lordkleiton.desafiomobills.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentIncomesBinding
import com.lordkleiton.desafiomobills.view.recyclerview.IncomesListAdapter
import com.lordkleiton.desafiomobills.viewmodel.IncomesViewModel

class IncomesFragment : Fragment(R.layout.fragment_incomes) {
    private lateinit var binding: FragmentIncomesBinding
    private lateinit var adapter: IncomesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomesBinding.bind(view)

        setupAdapter()

        val vm = ViewModelProvider(this).get(IncomesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setupAdapter() {
        adapter = IncomesListAdapter()

        binding.incomesRv.adapter = adapter
    }
}
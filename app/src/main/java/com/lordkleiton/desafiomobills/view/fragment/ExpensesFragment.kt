package com.lordkleiton.desafiomobills.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentExpensesBinding
import com.lordkleiton.desafiomobills.view.recyclerview.ExpensesListAdapter
import com.lordkleiton.desafiomobills.viewmodel.ExpensesViewModel

class ExpensesFragment : Fragment(R.layout.fragment_expenses) {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var adapter: ExpensesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpensesBinding.bind(view)

        setupAdapter()

        val vm = ViewModelProvider(this).get(ExpensesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setupAdapter() {
        adapter = ExpensesListAdapter()

        binding.expensesRv.adapter = adapter
    }
}
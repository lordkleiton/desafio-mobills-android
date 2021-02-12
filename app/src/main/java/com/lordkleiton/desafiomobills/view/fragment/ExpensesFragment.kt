package com.lordkleiton.desafiomobills.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentExpensesBinding
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_BOOL
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_DESC
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_MODE
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_VALUE
import com.lordkleiton.desafiomobills.util.AppConst.RESULT_EXPENSE
import com.lordkleiton.desafiomobills.view.FormActivity
import com.lordkleiton.desafiomobills.view.recyclerview.ExpensesListAdapter
import com.lordkleiton.desafiomobills.viewmodel.ExpensesViewModel

class ExpensesFragment : Fragment(R.layout.fragment_expenses) {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var adapter: ExpensesListAdapter
    private lateinit var vm: ExpensesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpensesBinding.bind(view)

        setupAdapter()

        setupObserver()

        setupFab()
    }

    private fun setupAdapter() {
        adapter = ExpensesListAdapter()

        binding.expensesRv.adapter = adapter
    }

    private fun setupObserver() {
        vm = ViewModelProvider(this).get(ExpensesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setupFab() {
        binding.expensesFab.setOnClickListener {
            val intent = Intent(activity, FormActivity::class.java).apply {
                putExtra(EXTRA_MODE, RESULT_EXPENSE)
            }

            startActivityForResult(intent, RESULT_EXPENSE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == RESULT_EXPENSE && data != null) {
            val value = data.getLongExtra(EXTRA_VALUE, -1L)
            val desc = data.getStringExtra(EXTRA_DESC)!!
            val bool = data.getBooleanExtra(EXTRA_BOOL, false)
            val expense = Despesa(value, desc, pago = bool)

            vm.save(expense).observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
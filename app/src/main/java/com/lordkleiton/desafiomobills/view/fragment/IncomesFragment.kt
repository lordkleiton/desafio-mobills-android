package com.lordkleiton.desafiomobills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentIncomesBinding
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.AppConst
import com.lordkleiton.desafiomobills.view.FormActivity
import com.lordkleiton.desafiomobills.view.recyclerview.IncomesListAdapter
import com.lordkleiton.desafiomobills.viewmodel.IncomesViewModel

class IncomesFragment : Fragment(R.layout.fragment_incomes) {
    private lateinit var binding: FragmentIncomesBinding
    private lateinit var adapter: IncomesListAdapter
    private lateinit var vm: IncomesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomesBinding.bind(view)

        setupAdapter()

        setupAdapter()

        setupObserver()

        setupFab()
    }

    private fun setupAdapter() {
        adapter = IncomesListAdapter()

        binding.incomesRv.adapter = adapter
    }

    private fun setupObserver() {
        vm = ViewModelProvider(this).get(IncomesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setupFab() {
        binding.incomesFab.setOnClickListener {
            val intent = Intent(activity, FormActivity::class.java).apply {
                putExtra(AppConst.EXTRA_MODE, AppConst.RESULT_EXPENSE)
            }

            startActivityForResult(intent, AppConst.RESULT_EXPENSE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == AppConst.RESULT_EXPENSE && data != null) {
            val value = data.getLongExtra(AppConst.EXTRA_VALUE, -1L)
            val desc = data.getStringExtra(AppConst.EXTRA_DESC)!!
            val bool = data.getBooleanExtra(AppConst.EXTRA_BOOL, false)
            val income = Receita(value, desc, recebido = bool)

            vm.save(income).observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
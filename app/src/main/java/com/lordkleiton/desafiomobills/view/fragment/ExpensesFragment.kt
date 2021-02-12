package com.lordkleiton.desafiomobills.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentExpensesBinding
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_MODE
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_BOOL
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_DESC
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_ID
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_VALUE
import com.lordkleiton.desafiomobills.util.AppConst.MODE_EDIT
import com.lordkleiton.desafiomobills.util.AppConst.MODE_NEW
import com.lordkleiton.desafiomobills.view.FormActivity
import com.lordkleiton.desafiomobills.view.recyclerview.ExpensesListAdapter
import com.lordkleiton.desafiomobills.view.recyclerview.listener.ExpenseActionListener
import com.lordkleiton.desafiomobills.viewmodel.ExpensesViewModel

class ExpensesFragment : Fragment(R.layout.fragment_expenses) {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var adapter: ExpensesListAdapter
    private lateinit var vm: ExpensesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpensesBinding.bind(view)

        setupAdapter()

        setupVM()

        setupRv()

        setupFab()
    }

    private fun setupAdapter() {
        adapter = ExpensesListAdapter(object : ExpenseActionListener {
            override fun onDelete(id: String) {
                vm.delete(id).observe(viewLifecycleOwner, {
                    adapter.submitList(it.toList())
                })
            }

            override fun onEdit(current: Pair<String, Despesa>) {
                customStart(MODE_EDIT, current)
            }
        })

        binding.expensesRv.adapter = adapter
    }

    private fun setupVM() {
        vm = ViewModelProvider(this).get(ExpensesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it.toList())
        })
    }

    private fun setupRv() {
        binding.expensesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (dy > 0) binding.expensesFab.hide()

                    if (dy < 0) binding.expensesFab.show()
                }
            }
        })
    }

    private fun setupFab() {
        binding.expensesFab.setOnClickListener {
            customStart(MODE_NEW)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            val value = data.getLongExtra(EXTRA_VALUE, -1L)
            val desc = data.getStringExtra(EXTRA_DESC)!!
            val bool = data.getBooleanExtra(EXTRA_BOOL, false)
            val id = data.getStringExtra(EXTRA_ID) ?: ""
            val expense = Despesa(value, desc, pago = bool)
            val liveData = when (requestCode) {
                MODE_NEW -> vm.save(expense)
                else -> vm.update(id, expense)
            }

            liveData.observe(viewLifecycleOwner, {
                adapter.submitList(it.toList())
            })
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun customStart(mode: Int, data: Pair<String, Despesa>? = null) {
        val intent = Intent(activity, FormActivity::class.java).apply {
            putExtra(CURRENT_MODE, mode)

            data?.apply {
                putExtra(EXTRA_ID, first)

                second.apply {
                    putExtra(EXTRA_VALUE, valor)
                    putExtra(EXTRA_DESC, descricao)
                    putExtra(EXTRA_BOOL, pago)
                }
            }
        }

        startActivityForResult(intent, mode)
    }
}
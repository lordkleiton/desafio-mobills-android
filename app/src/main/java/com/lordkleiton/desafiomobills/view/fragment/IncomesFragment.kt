package com.lordkleiton.desafiomobills.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentIncomesBinding
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.AppConst
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_MODE
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_TYPE
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_TYPE_INCOMES
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_BOOL
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_DESC
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_ID
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_TIMESTAMP
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_VALUE
import com.lordkleiton.desafiomobills.util.AppConst.MODE_NEW
import com.lordkleiton.desafiomobills.view.FormActivity
import com.lordkleiton.desafiomobills.view.recyclerview.IncomesListAdapter
import com.lordkleiton.desafiomobills.view.recyclerview.listener.IncomesActionListener
import com.lordkleiton.desafiomobills.viewmodel.IncomesViewModel
import java.util.*

class IncomesFragment : Fragment(R.layout.fragment_incomes) {
    private lateinit var binding: FragmentIncomesBinding
    private lateinit var adapter: IncomesListAdapter
    private lateinit var vm: IncomesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomesBinding.bind(view)

        setupAdapter()

        setupVM()

        setupRv()

        setupFab()
    }

    private fun setupAdapter() {
        adapter = IncomesListAdapter(object : IncomesActionListener {
            override fun onDelete(id: String) {
                vm.delete(id).observe(viewLifecycleOwner, {
                    adapter.submitList(it.toList())
                })
            }

            override fun onEdit(current: Pair<String, Receita>) {
                customStart(AppConst.MODE_EDIT, current)
            }
        })

        binding.incomesRv.adapter = adapter
    }

    private fun setupVM() {
        vm = ViewModelProvider(this).get(IncomesViewModel::class.java)

        vm.find().observe(viewLifecycleOwner, {
            adapter.submitList(it.toList())
        })
    }

    private fun setupRv() {
        binding.incomesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (dy > 0) binding.incomesFab.hide()

                    if (dy < 0) binding.incomesFab.show()
                }
            }
        })
    }

    private fun setupFab() {
        binding.incomesFab.setOnClickListener {
            customStart(MODE_NEW)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            val value = data.getLongExtra(EXTRA_VALUE, -1L)
            val desc = data.getStringExtra(EXTRA_DESC)!!
            val bool = data.getBooleanExtra(EXTRA_BOOL, false)
            val id = data.getStringExtra(EXTRA_ID) ?: ""
            val date = data.getLongExtra(EXTRA_TIMESTAMP, 1)
            val income = Receita(value, desc, Timestamp(Date(date)), bool)
            val liveData = when (requestCode) {
                MODE_NEW -> vm.save(income)
                else -> vm.update(id, income)
            }

            liveData.observe(viewLifecycleOwner, {
                adapter.submitList(it.toList())
            })
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun customStart(mode: Int, data: Pair<String, Receita>? = null) {
        val intent = Intent(activity, FormActivity::class.java).apply {
            putExtra(CURRENT_MODE, mode)
            putExtra(CURRENT_TYPE, CURRENT_TYPE_INCOMES)

            data?.apply {
                putExtra(EXTRA_ID, first)

                second.apply {
                    putExtra(EXTRA_VALUE, valor)
                    putExtra(EXTRA_DESC, descricao)
                    putExtra(EXTRA_BOOL, recebido)
                    putExtra(EXTRA_TIMESTAMP, this.data.toDate().time)
                }
            }
        }

        startActivityForResult(intent, mode)
    }
}
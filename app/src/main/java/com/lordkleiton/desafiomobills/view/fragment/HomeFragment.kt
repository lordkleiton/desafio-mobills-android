package com.lordkleiton.desafiomobills.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.FragmentHomeBinding
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.AppConst.PLACEHOLDER
import com.lordkleiton.desafiomobills.util.byHundred
import com.lordkleiton.desafiomobills.util.toCurrency
import com.lordkleiton.desafiomobills.viewmodel.ExpensesViewModel
import com.lordkleiton.desafiomobills.viewmodel.IncomesViewModel
import java.time.ZoneId
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val userName = Firebase.auth.currentUser!!.displayName!!
    private var hour = 0
    private lateinit var vmIncomes: IncomesViewModel
    private lateinit var vmExpenses: ExpensesViewModel

    private var expensesVisible = false
    private var incomesVisible = false

    private lateinit var incomes: List<Receita>
    private lateinit var expenses: List<Despesa>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setupVM()

        setupHour()

        setupViews()

        observe()
    }

    private fun observe() {
        vmIncomes.find().observe(viewLifecycleOwner, {
            incomes = it.toList().map { el -> el.second }

            setupIncomesCard()
        })

        vmExpenses.find().observe(viewLifecycleOwner, {
            expenses = it.toList().map { el -> el.second }

            setupExpensesCard()
        })
    }

    private fun setupExpensesCard() {
        expensesVisible = true

        binding.expenseCard.visibility = View.VISIBLE

        binding.expenseCardTitle.text = resources.getText(R.string.qty_expenses).toString().replace(
            PLACEHOLDER, expenses.size.toString()
        )
        binding.expenseCardPending.text =
            resources.getText(R.string.card_pending).toString().replace(
                PLACEHOLDER, expenses.filter { !it.pago }.size.toString()
            )
        binding.expenseCardNotPending.text =
            resources.getText(R.string.card_received).toString().replace(
                PLACEHOLDER, expenses.filter { it.pago }.size.toString()
            )

        setupResumeCard()
    }

    private fun setupIncomesCard() {
        incomesVisible = true

        binding.incomeCard.visibility = View.VISIBLE

        binding.incomeCardTitle.text = resources.getText(R.string.qty_incomes).toString().replace(
            PLACEHOLDER, incomes.size.toString()
        )
        binding.incomeCardPending.text =
            resources.getText(R.string.card_pending).toString().replace(
                PLACEHOLDER, incomes.filter { !it.recebido }.size.toString()
            )
        binding.incomeCardNotPending.text =
            resources.getText(R.string.card_received).toString().replace(
                PLACEHOLDER, incomes.filter { it.recebido }.size.toString()
            )

        setupResumeCard()
    }

    private fun setupResumeCard() {
        if (expensesVisible && incomesVisible) {
            val expensesValue = expenses.fold(0L) { acc, item ->
                var res = acc

                if (!item.pago) res += item.valor

                res
            }
            val incomesValue = incomes.fold(0L) { acc, item ->
                var res = acc

                if (item.recebido) res += item.valor

                res
            }
            val result = incomesValue - expensesValue

            binding.cardResume.visibility = View.VISIBLE

            binding.cardResumeTitle.text =
                resources.getText(R.string.txt_balance).toString().replace(
                    PLACEHOLDER, result.byHundred().toCurrency()
                )
        }
    }

    private fun setupVM() {
        vmIncomes = ViewModelProvider(this).get(IncomesViewModel::class.java)

        vmExpenses = ViewModelProvider(this).get(ExpensesViewModel::class.java)
    }

    private fun setupHour() {
        hour = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).hour
    }

    private fun setupViews() {
        val textId = when (hour) {
            in 6..11 -> R.string.msg_morning
            in 12..18 -> R.string.msg_afternoon
            else -> R.string.msg_night
        }

        binding.homeSubtitle.text =
            resources.getText(textId).toString().replace(PLACEHOLDER, userName.split(" ")[0])
    }
}
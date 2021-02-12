package com.lordkleiton.desafiomobills.view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.model.Despesa
import com.lordkleiton.desafiomobills.util.AppConst.DESCRIPTION_MAX
import com.lordkleiton.desafiomobills.util.toCurrency
import java.text.SimpleDateFormat

class ExpensesListAdapter :
    ListAdapter<Pair<String, Despesa>, ExpensesListAdapter.DespesaViewHolder>(DespesaViewHolder) {

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val item = currentList[position]

        return holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_generic_item, parent, false)

        return DespesaViewHolder(view)
    }

    class DespesaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val value: TextView
        private val description: TextView
        private val date: TextView
        private val pending: TextView

        init {
            view.apply {
                value = findViewById(R.id.list_item_value)
                description = findViewById(R.id.list_item_description)
                date = findViewById(R.id.list_item_date)
                pending = findViewById(R.id.list_item_pending)
            }
        }

        fun bind(data: Pair<String, Despesa>) {
            data.second.apply {
                val max = DESCRIPTION_MAX
                val auxDate = SimpleDateFormat.getDateInstance().format(this.data.toDate())
                val auxDesc = when (descricao.length) {
                    in 0..max -> descricao
                    else -> "${descricao.substring(0, max)}..."
                }
                val color = when (pago) {
                    false -> R.color.design_default_color_error
                    else -> android.R.color.holo_green_dark
                }
                val auxText = when (pago) {
                    false -> R.string.item_default_pending
                    else -> R.string.item_paid
                }

                value.text = (valor / 100).toCurrency()
                description.text = auxDesc
                date.text = auxDate
                pending.apply {
                    setTextColor(ContextCompat.getColor(view.context, color))

                    text = resources.getText(auxText)
                }
            }
        }

        companion object : DiffUtil.ItemCallback<Pair<String, Despesa>>() {
            override fun areContentsTheSame(
                oldItem: Pair<String, Despesa>,
                newItem: Pair<String, Despesa>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: Pair<String, Despesa>,
                newItem: Pair<String, Despesa>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.lordkleiton.desafiomobills.view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.AppConst.DESCRIPTION_MAX
import com.lordkleiton.desafiomobills.util.byHundred
import com.lordkleiton.desafiomobills.util.formatDate
import com.lordkleiton.desafiomobills.util.toCurrency
import com.lordkleiton.desafiomobills.view.recyclerview.listener.IncomesActionListener

class IncomesListAdapter(private val listener: IncomesActionListener) :
    ListAdapter<Pair<String, Receita>, IncomesListAdapter.ReceitaViewHolder>(ReceitaViewHolder) {

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {
        val item = currentList[position]

        return holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_generic_item, parent, false)

        return ReceitaViewHolder(view, listener)
    }

    class ReceitaViewHolder(private val view: View, listener: IncomesActionListener) :
        RecyclerView.ViewHolder(view) {
        private val value: TextView
        private val description: TextView
        private val date: TextView
        private val pending: TextView
        private val edit: ImageButton
        private val delete: ImageButton

        private lateinit var income: Pair<String, Receita>

        init {
            view.apply {
                value = findViewById(R.id.list_item_value)
                description = findViewById(R.id.list_item_description)
                date = findViewById(R.id.list_item_date)
                pending = findViewById(R.id.list_item_pending)
                edit = findViewById(R.id.list_item_edit)
                delete = findViewById(R.id.list_item_delete)

                edit.setOnClickListener {
                    listener.onEdit(income)
                }

                delete.setOnClickListener {
                    listener.onDelete(income.first)
                }
            }
        }

        fun bind(data: Pair<String, Receita>) {
            income = data

            data.second.apply {
                val max = DESCRIPTION_MAX
                val auxDate = this.data.toDate().formatDate()
                val auxDesc = when (descricao.length) {
                    in 0..max -> descricao
                    else -> "${descricao.substring(0, max)}..."
                }
                val color = when (recebido) {
                    false -> R.color.design_default_color_error
                    else -> android.R.color.holo_green_dark
                }
                val auxText = when (recebido) {
                    false -> R.string.item_default_pending
                    else -> R.string.item_received
                }

                value.text = valor.byHundred().toCurrency()
                description.text = auxDesc
                date.text = auxDate
                pending.apply {
                    setTextColor(ContextCompat.getColor(view.context, color))

                    text = resources.getText(auxText)
                }
            }
        }

        companion object : DiffUtil.ItemCallback<Pair<String, Receita>>() {
            override fun areContentsTheSame(
                oldItem: Pair<String, Receita>,
                newItem: Pair<String, Receita>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: Pair<String, Receita>,
                newItem: Pair<String, Receita>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
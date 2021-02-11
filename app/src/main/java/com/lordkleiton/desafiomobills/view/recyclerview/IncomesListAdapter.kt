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
import com.lordkleiton.desafiomobills.model.Receita
import com.lordkleiton.desafiomobills.util.toCurrency
import java.text.SimpleDateFormat

class IncomesListAdapter :
    ListAdapter<Receita, IncomesListAdapter.ReceitaViewHolder>(ReceitaViewHolder) {

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {
        val item = currentList[position]

        return holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_generic_item, parent, false)

        return ReceitaViewHolder(view)
    }

    class ReceitaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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

        fun bind(data: Receita) {
            data.apply {
                val max = 30
                val auxDate = SimpleDateFormat.getDateInstance().format(this.data.toDate())
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

                value.text = (valor / 100).toCurrency()
                description.text = auxDesc
                date.text = auxDate
                pending.apply {
                    setTextColor(ContextCompat.getColor(view.context, color))

                    text = resources.getText(auxText)

                }
            }
        }

        companion object : DiffUtil.ItemCallback<Receita>() {
            override fun areContentsTheSame(oldItem: Receita, newItem: Receita): Boolean {
                return oldItem.run {
                    recebido == newItem.recebido && valor == newItem.valor && data == newItem.data && descricao == newItem.descricao
                }
            }

            override fun areItemsTheSame(oldItem: Receita, newItem: Receita): Boolean {
                return oldItem == newItem
            }
        }
    }
}
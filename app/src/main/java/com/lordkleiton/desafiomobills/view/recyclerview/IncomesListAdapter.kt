package com.lordkleiton.desafiomobills.view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.model.Receita

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

    class ReceitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
                value.text = valor.toString()
                description.text = descricao
                date.text = data.toString()
                pending.text = recebido.toString()
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
package com.lordkleiton.desafiomobills.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.ActivityFormBinding
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_MODE
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_TYPE
import com.lordkleiton.desafiomobills.util.AppConst.CURRENT_TYPE_EXPENSES
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_BOOL
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_DESC
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_ID
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_MODE_NEW
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_VALUE
import com.lordkleiton.desafiomobills.util.AppConst.MODE_EDIT
import com.lordkleiton.desafiomobills.util.AppConst.MODE_NEW

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private var mode = -1
    private val errorMsg = "Campo obrigatório"
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMode()

        setupFab()
    }

    private fun setupMode() {
        mode = intent.getIntExtra(CURRENT_MODE, -1)

        if (mode == MODE_EDIT) fillForm()

        setupViews()
    }

    private fun setupViews() {
        val type = intent.getStringExtra(CURRENT_TYPE)!!
        val title = when (mode) {
            MODE_NEW -> when (type) {
                CURRENT_TYPE_EXPENSES -> R.string.new_expense
                else -> R.string.new_income
            }
            else -> when (type) {
                CURRENT_TYPE_EXPENSES -> R.string.edit_expense
                else -> R.string.edit_income
            }
        }
        val toggleLabel = when (type) {
            CURRENT_TYPE_EXPENSES -> R.string.item_paid
            else -> R.string.item_received
        }

        binding.formTitle.text = resources.getText(title)
        binding.fieldSelectText.text = resources.getText(toggleLabel)
    }

    private fun fillForm() {
        val value = intent.getLongExtra(EXTRA_VALUE, -1L)
        val desc = intent.getStringExtra(EXTRA_DESC)!!
        val bool = intent.getBooleanExtra(EXTRA_BOOL, false)

        binding.fieldValue.editText!!.setText((value / 100).toString())
        binding.fieldDesc.editText!!.setText(desc)
        binding.fieldSelect.isChecked = bool

        id = intent.getStringExtra(EXTRA_ID)!!
    }

    private fun isValid(): Boolean {
        val value = binding.fieldValue.editText!!
        val desc = binding.fieldDesc.editText!!
        val checkEditEmpty = { it: EditText ->
            it.run {
                if (text.isBlank()) {
                    error = errorMsg

                    requestFocus()
                }

                text.isNotBlank()
            }
        }

        return checkEditEmpty(value) && checkEditEmpty(desc)
    }

    private fun getValue(value: Editable): Long {
        val res = when (value.contains(".") || value.contains(",")) {
            true -> value.toString().replace(".", "").replace(",", "")
            else -> "${value}00"
        }

        return res.toLong()
    }

    private fun setupFab() {
        binding.okFab.setOnClickListener {
            if (isValid()) {
                val intent = Intent().apply {
                    val value = getValue(binding.fieldValue.editText!!.text)
                    val desc = binding.fieldDesc.editText!!.text.toString()
                    val bool = binding.fieldSelect.isChecked

                    putExtra(EXTRA_VALUE, value)
                    putExtra(EXTRA_DESC, desc)
                    putExtra(EXTRA_BOOL, bool)
                    putExtra(EXTRA_MODE_NEW, mode)
                    putExtra(EXTRA_ID, id)
                }

                setResult(RESULT_OK, intent)

                finish()
            }
        }
    }
}
package com.lordkleiton.desafiomobills.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.lordkleiton.desafiomobills.databinding.ActivityFormBinding
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_BOOL
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_DESC
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_MODE
import com.lordkleiton.desafiomobills.util.AppConst.EXTRA_VALUE

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private var mode = -1
    private val errorMsg = "Campo obrigatório"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMode()

        setupFab()
    }

    private fun setupMode() {
        mode = intent.getIntExtra(EXTRA_MODE, -1)
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
                    putExtra(EXTRA_MODE, mode)
                }

                setResult(RESULT_OK, intent)

                finish()
            }
        }
    }
}
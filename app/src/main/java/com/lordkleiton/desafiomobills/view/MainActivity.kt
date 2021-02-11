package com.lordkleiton.desafiomobills.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.ActivityMainBinding
import com.lordkleiton.desafiomobills.view.fragment.ExpensesFragment
import com.lordkleiton.desafiomobills.view.fragment.HomeFragment
import com.lordkleiton.desafiomobills.view.fragment.IncomesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchFragments(HomeFragment())

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val current = binding.bottomNavigation.selectedItemId

            if (current != it.itemId) {
                val fragment = when (it.itemId) {
                    R.id.menu_item_home -> HomeFragment()
                    R.id.menu_item_income -> IncomesFragment()
                    else -> ExpensesFragment()
                }

                switchFragments(fragment)
            }

            true
        }
    }

    private fun switchFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragments_container, fragment)

            commit()
        }
    }

    //impedindo de voltar pra tela de login
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
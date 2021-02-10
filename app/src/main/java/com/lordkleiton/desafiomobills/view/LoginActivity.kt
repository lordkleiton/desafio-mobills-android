package com.lordkleiton.desafiomobills.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lordkleiton.desafiomobills.R
import com.lordkleiton.desafiomobills.databinding.ActivityLoginBinding
import com.lordkleiton.desafiomobills.util.AppConst.LOGIN_REQUEST_CODE
import com.lordkleiton.desafiomobills.util.AppConst.TAG

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAuth()

        setupButton()

//        val vm = ViewModelProvider(this).get(ExpensesViewModel::class.java)
//
//        vm.save(Despesa()).observe(this, {
//            Log.i("hmm", it.toString())
//        })
    }

    private fun setupAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()

        client = GoogleSignIn.getClient(this, gso)
    }

    private fun setupButton() {
        binding.loginButton.apply {
            setOnClickListener {
                isClickable = false

                hideMsg()

                changeLoading(View.VISIBLE)

                signIn()
            }
        }
    }

    private fun changeLoading(visibility: Int) {
        binding.loginLoading.visibility = visibility
    }

    private fun signIn() {
        val intent = client.signInIntent

        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!

                Log.d(TAG, "userId: ${account.id}")

                auth(account.idToken!!)
            } catch (e: ApiException) {
                onError(e)
            }
        }
    }

    private fun onError(e: Exception) {
        Log.w(TAG, "auth failed: ", e)

        showMsg()

        changeLoading(View.INVISIBLE)

        binding.loginButton.isClickable = true
    }

    private fun hideMsg() {
        binding.loginError.visibility = View.INVISIBLE
    }

    private fun showMsg() {
        binding.loginError.apply {
            visibility = View.VISIBLE

            text = resources.getString(R.string.error_login)
        }
    }

    private fun auth(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.apply {
                        Log.d(TAG, "userName: $displayName")

                        changeLoading(View.INVISIBLE)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                } else {
                    onError(task.exception!!)
                }
            }
    }
}
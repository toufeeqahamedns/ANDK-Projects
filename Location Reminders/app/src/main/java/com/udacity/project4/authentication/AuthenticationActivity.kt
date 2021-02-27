package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

private const val SIGN_IN_RESPONSE_CODE = 1001

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, RemindersActivity::class.java))
            finish()
            return
        }

        val binding: ActivityAuthenticationBinding =
            DataBindingUtil.setContentView<ActivityAuthenticationBinding>(
                this,
                R.layout.activity_authentication
            )

        binding.button.setOnClickListener {
            initiateAuth()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (firebaseAuth.currentUser != null) {
                    startActivity(Intent(this, RemindersActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun initiateAuth() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            Intent(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    providers
                ).build()
            ), SIGN_IN_RESPONSE_CODE
        )
    }
}

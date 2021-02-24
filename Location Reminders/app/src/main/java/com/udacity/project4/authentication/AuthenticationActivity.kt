package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            if(firebaseAuth.currentUser != null){
                startActivity(Intent(this, RemindersActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, RemindersActivity::class.java))
            finish()
            return
        }

        val binding: ActivityAuthenticationBinding = DataBindingUtil.setContentView<ActivityAuthenticationBinding>(this, R.layout.activity_authentication)

        binding.button.setOnClickListener {
            initiateAuth()
        }
    }

    private fun initiateAuth() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startForResult.launch(
            Intent(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers).build())
        )
    }
}

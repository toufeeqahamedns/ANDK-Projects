package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.udacity.project4.utils.FirebaseUserLiveData

class AuthViewModel : ViewModel() {

    val authState = FirebaseUserLiveData().map { firebaseUser ->
        if (firebaseUser != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }
}

enum class AuthState {
    AUTHENTICATED, UNAUTHENTICATED
}
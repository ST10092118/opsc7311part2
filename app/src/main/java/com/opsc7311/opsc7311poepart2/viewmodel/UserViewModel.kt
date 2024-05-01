package com.opsc7311.opsc7311poepart2.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.service.UserService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class UserViewModel: ViewModel() {
    private val userService = UserService()
    val status: MutableLiveData<Pair<RegistrationStatus, String>> = MutableLiveData()

    fun registerUser(context: Context, username: String, email: String, password: String, callback: (RegistrationStatus, String) -> Unit) {
        userService.registerUser(context, username, email, password) { registrationStatus, message ->
            callback(registrationStatus, message)
            status.postValue(Pair(registrationStatus, message))
        }
    }


    fun loginUser(context: Context, username: String, password: String, callback: (RegistrationStatus, String) -> Unit): User? {
        return userService.login(context, username, password) { loginStatus, message ->
            callback(loginStatus, message)
            status.postValue(Pair(loginStatus, message))
        }
    }
}
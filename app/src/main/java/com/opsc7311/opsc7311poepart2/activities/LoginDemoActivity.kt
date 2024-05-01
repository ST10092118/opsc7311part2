package com.opsc7311.opsc7311poepart2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opsc7311.opsc7311poepart2.MainActivity
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.UserViewModel

class LoginDemoActivity : AppCompatActivity() {


    private val userViewModel: UserViewModel by viewModels()
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText

    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_demo)

        usernameText = findViewById(R.id.username_text)
        passwordText = findViewById(R.id.password_text)

        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val username = usernameText.text.toString()
        val password = passwordText.text.toString()

        if(username.isEmpty() || password.isEmpty()){
            usernameText.error = "username cannot be blank"
            passwordText.error = "password cannot be blank"
            return
        }

        userViewModel.status.observe(this){
                status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(this, status.second,
                    Toast.LENGTH_SHORT).show();
                redirectToMain()
            } else {
                Toast.makeText(this, status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        userViewModel.loginUser(this, username, password) { loginStatus, message ->
            if (loginStatus == RegistrationStatus.SUCCESS) {
                Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();
                redirectToMain()
            } else {
                Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();            }
        }
    }
    private fun redirectToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
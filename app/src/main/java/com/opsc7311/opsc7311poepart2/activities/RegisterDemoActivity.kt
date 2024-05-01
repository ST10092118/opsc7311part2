package com.opsc7311.opsc7311poepart2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opsc7311.opsc7311poepart2.MainActivity
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.UserViewModel

class RegisterDemoActivity : AppCompatActivity() {


    private val userViewModel: UserViewModel by viewModels()

    private lateinit var usernameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_demo)

        usernameText = findViewById(R.id.username_text)
        emailText = findViewById(R.id.email_text)
        passwordText = findViewById(R.id.password_text)

        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val username = usernameText.text.toString()
        val password = passwordText.text.toString()
        val email = emailText.text.toString()

        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
            usernameText.error = "username cannot be blank"
            passwordText.error = "password cannot be blank"
            emailText.error = "Email cannot be blank"
            return
        }

        userViewModel.status.observe(this){
                status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(this, status.second,
                    Toast.LENGTH_SHORT).show();
                redirectToLogin()
            } else {
                Toast.makeText(this, status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        userViewModel.registerUser(this, username, email, password) { registrationStatus, message ->
            if (registrationStatus == RegistrationStatus.SUCCESS) {
                Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();
                redirectToLogin()
            } else {
                Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();            }
        }
    }

    private fun redirectToLogin(){
        startActivity(Intent(this, LoginDemoActivity::class.java))
        finish()
    }


}
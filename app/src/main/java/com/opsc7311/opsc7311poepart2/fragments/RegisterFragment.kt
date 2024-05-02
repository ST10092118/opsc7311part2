package com.opsc7311.opsc7311poepart2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.activities.LoginDemoActivity
import com.opsc7311.opsc7311poepart2.activities.RegisterLoginActivity
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.UserViewModel




class RegisterFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var usernameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    private lateinit var registerButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        usernameText = view.findViewById(R.id.username_text)
        emailText = view.findViewById(R.id.email_text)
        passwordText = view.findViewById(R.id.password_text)

        registerButton = view.findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            register()
        }

        return view
    }

    private fun register() {
        val username = usernameText.text.toString()
        val password = passwordText.text.toString()
        val email = emailText.text.toString()


        if(username.isEmpty()){
            Toast.makeText(requireContext(), "username cannot be blank",
                Toast.LENGTH_SHORT).show();
            return
        }

        if(email.isEmpty()){
            Toast.makeText(requireContext(), "Email cannot be blank",
                Toast.LENGTH_SHORT).show();
            return
        }

        if(password.isEmpty()){
            Toast.makeText(requireContext(), "password cannot be blank",
                Toast.LENGTH_SHORT).show();
            return
        }

        userViewModel.status.observe(viewLifecycleOwner){
                status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();
                redirectToLogin()
            } else {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        userViewModel.registerUser(requireContext(), username, email, password) { registrationStatus, message ->
        }
    }

    private fun redirectToLogin(){
        (requireActivity() as RegisterLoginActivity).switchToLoginTab()    }
}
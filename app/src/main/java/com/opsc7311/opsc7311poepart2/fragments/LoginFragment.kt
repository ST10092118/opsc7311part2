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
import com.opsc7311.opsc7311poepart2.MainActivity
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.UserViewModel

class LoginFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText

    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usernameText = view.findViewById(R.id.username_text)
        passwordText = view.findViewById(R.id.password_text)

        loginButton = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            loginUser()
        }

        return view

    }

    private fun loginUser() {
        val username = usernameText.text.toString()
        val password = passwordText.text.toString()

        if(username.isEmpty()){
            Toast.makeText(requireContext(), "username cannot be blank",
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
                redirectToMain()
            } else {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        userViewModel.loginUser(requireContext(), username, password) { loginStatus, message ->
//            if (loginStatus == RegistrationStatus.SUCCESS) {
//                Toast.makeText(requireContext(), message,
//                    Toast.LENGTH_SHORT).show();
//                redirectToMain()
//            } else {
//                Toast.makeText(requireContext(), message,
//                    Toast.LENGTH_SHORT).show();            }
        }
    }
    private fun redirectToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

}
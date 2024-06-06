package com.opsc7311.opsc7311poepart2.database.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class UserService {
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
    private val mAuth = FirebaseAuth.getInstance();
    fun registerUser(
        context: Context,
        username: String,
        email: String,
        password: String,
        callback: (RegistrationStatus, String) -> Unit
    ) {
        // Check if the username already exists

        // This function was adapted from YouTube
        // https://www.youtube.com/watch?v=H_maapn4Q3Q
        // TECH_WORLD
        // https://www.youtube.com/@tech_world_tutorials
        firebaseDatabase.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Username already exists, return FAILURE status
                        callback(RegistrationStatus.FAILURE, "Username already exists")
                    } else {
                        // Username does not exist, proceed with user registration
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUser = mAuth.currentUser
                                    currentUser?.let {
                                        val userId = it.uid
                                        val user = User(userId, email, username, password)
                                        firebaseDatabase.child(userId).setValue(user)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    callback(RegistrationStatus.SUCCESS, "User Registered")
                                                } else {
                                                    callback(
                                                        RegistrationStatus.FAILURE,
                                                        "${task.exception?.message}"
                                                    )
                                                }
                                            }
                                    }
                                } else {
                                    // Registration failed
                                    callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                                }
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error if needed
                    callback(RegistrationStatus.FAILURE, databaseError.message)
                }
            })
    }


    fun login(context: Context, username: String, password: String, callback: (RegistrationStatus, String) -> Unit): User?{
        // This function was adapted from medium
        // https://medium.com/a-practical-guide-to-firebase-on-android/storing-and-retrieving-data-from-firebase-with-kotlin-on-android-91c36680771
        // Nick Skelton
        // https://medium.com/@nickskelton
        var user: User? = null
        firebaseDatabase.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        user = snapshot.getValue(User::class.java)
                        if (user != null) {

                            mAuth.signInWithEmailAndPassword(user!!.email, password)

                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        callback(RegistrationStatus.SUCCESS, "User Logged In")
                                    } else {
                                        callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                                    }
                                }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(RegistrationStatus.FAILURE, databaseError.message)
                }
            })
        return user
    }
    fun fetchAllUsers(callback: (List<User>, String) -> Unit) {
        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                callback(users, "")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(emptyList(), databaseError.message)
            }
        })
    }
}

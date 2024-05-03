package com.opsc7311.opsc7311poepart2.database.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Goal
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class GoalService {
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Goal")
    private val mAuth = FirebaseAuth.getInstance();

    fun createNewGoal(minimum: Int, maximum: Int, callback: (RegistrationStatus, String) -> Unit){
        // This function was adapted from YouTube
        // https://www.youtube.com/watch?v=pLnhnHwLkYo
        // Android Knowledge
        // https://www.youtube.com/@android_knowledge
        val goalId = firebaseDatabase.push().key!!
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val userId = it.uid
            val goal =  Goal(goalId, minimum, maximum, userId)
            firebaseDatabase.child(userId).setValue(goal)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(RegistrationStatus.SUCCESS, "Goal created!!")
                    } else {
                        callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                    }
                }
        }
    }

    fun getGoal(callback: (Goal?) -> Unit){
        // This function was adapted from medium
        // https://medium.com/a-practical-guide-to-firebase-on-android/storing-and-retrieving-data-from-firebase-with-kotlin-on-android-91c36680771
        // Nick Skelton
        // https://medium.com/@nickskelton
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val userId = it.uid

            firebaseDatabase.child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val goal = dataSnapshot.getValue(Goal::class.java)
                    callback(goal)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    callback(null)
                }
            })
        }

    }
}
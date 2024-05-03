package com.opsc7311.opsc7311poepart2.database.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class CategoryService {
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Categories")
    private val mAuth = FirebaseAuth.getInstance();

    fun createNewCategory(name: String, color: String, callback: (RegistrationStatus, String) -> Unit){
        // This function was adapted from YouTube
        // https://www.youtube.com/watch?v=pLnhnHwLkYo
        // Android Knowledge
        // https://www.youtube.com/@android_knowledge
        val categoryId = firebaseDatabase.push().key!!
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val userId = it.uid
            val category =  Category(categoryId, name, color, userId)
            firebaseDatabase.child(categoryId).setValue(category)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(RegistrationStatus.SUCCESS, "Category created!!")
                    } else {
                        callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                    }
                }
        }
    }

    fun getCategories(callback: (List<Category>) -> Unit){
        // This function was adapted from medium
        // https://medium.com/a-practical-guide-to-firebase-on-android/storing-and-retrieving-data-from-firebase-with-kotlin-on-android-91c36680771
        // Nick Skelton
        // https://medium.com/@nickskelton
        val categories = mutableListOf<Category>()
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val userId = it.uid

            firebaseDatabase.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val category = snapshot.getValue(Category::class.java)
                            category?.let { categories.add(it) }
                        }
                        callback(categories)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                        callback(emptyList())
                    }
                })
        }

    }
}
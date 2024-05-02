package com.opsc7311.opsc7311poepart2.database.service

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class TimesheetService {
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Timesheets")
    private val mAuth = FirebaseAuth.getInstance();
    private val imageDb = Firebase.storage.reference.child("timesheetImages")


    fun createNewTimesheet(image: Uri?, timesheet: Timesheet, callback: (RegistrationStatus, String) -> Unit){
        val timesheetId = firebaseDatabase.push().key!!
        val currentUser = mAuth.currentUser

        if(image != null){
            val imageRef = imageDb.child("$timesheetId.jpg")
            imageRef.putFile(image)
                .continueWithTask { taskSnapshot ->
                    if (!taskSnapshot.isSuccessful) {
                        callback(RegistrationStatus.FAILURE, "Error: ${taskSnapshot.exception?.message}")
                    }
                    imageRef.downloadUrl
                        .addOnSuccessListener {downloadUrl ->
                            val imageLink = downloadUrl.toString()
                            currentUser?.let {
                                val userId = it.uid
                                val newTimesheet =  Timesheet(id = timesheetId, name = timesheet.name, description = timesheet.description, date = timesheet.date, startTime =  timesheet.startTime, endTime =  timesheet.endTime, image =  imageLink, categoryId =  timesheet.categoryId, userId = userId)
                                firebaseDatabase.child(timesheetId).setValue(newTimesheet)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            callback(RegistrationStatus.SUCCESS, "Timesheet created!!")
                                        } else {
                                            callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                                        }
                                    }
                            }
                        }
                }
        }else{
            currentUser?.let {
                val userId = it.uid
                val newTimesheet =  Timesheet(id = timesheetId, name = timesheet.name, description = timesheet.description, date = timesheet.date, startTime =  timesheet.startTime, endTime =  timesheet.endTime, image =  "", categoryId =  timesheet.categoryId, userId = userId)
                firebaseDatabase.child(timesheetId).setValue(newTimesheet)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(RegistrationStatus.SUCCESS, "Timesheet created!!")
                        } else {
                            callback(RegistrationStatus.FAILURE, "${task.exception?.message}")
                        }
                    }
            }
        }


    }

    fun getTimesheetEntries(callback: (List<Pair<Timesheet, Category>>) -> Unit){
        val timesheetEntries = mutableListOf<Pair<Timesheet, Category>>()
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val userId = it.uid

            firebaseDatabase.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val timesheets = mutableListOf<Timesheet>()
                        for (snapshot in dataSnapshot.children) {
                            val timesheet = snapshot.getValue(Timesheet::class.java)
                            timesheet?.let { timesheets.add(it) }
                        }

                        if (timesheets.isNotEmpty()) {

                            val categoryDatabase = FirebaseDatabase.getInstance().reference.child("Categories")
                            categoryDatabase.orderByChild("userId").equalTo(userId)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(categorySnapshot: DataSnapshot) {
                                        val categories = mutableMapOf<String, Category>()
                                        for (categorySnap in categorySnapshot.children) {
                                            val category = categorySnap.getValue(Category::class.java)
                                            category?.let { categories[categorySnap.key!!] = it }
                                            Log.w("category", "categories: ${category}")
                                        }

                                        // Combine tasks with categories
                                        timesheets.forEach { task ->
                                            val category = categories[task.categoryId]
                                            category?.let { category ->
                                                timesheetEntries.add(Pair(task, category))
                                            }
                                        }

                                        callback(timesheetEntries)
                                    }

                                    override fun onCancelled(categoryError: DatabaseError) {
                                        // Handle errors
                                        callback(emptyList())
                                        Log.w("TaskDatabaseSource", "loadCategories:onCancelled", categoryError.toException())
                                    }
                                })
                        } else {
                            // If tasks list is empty, pass an empty list to the callback
                            callback(emptyList())
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                        callback(emptyList())
                        Log.w("TaskDatabaseSource", "loadTasks:onCancelled", databaseError.toException())
                    }
                })
        }

    }
}
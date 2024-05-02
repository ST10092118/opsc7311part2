package com.opsc7311.opsc7311poepart2.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.database.service.CategoryService
import com.opsc7311.opsc7311poepart2.database.service.TaskService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class TimesheetViewModel: ViewModel() {

    val taskService = TaskService()
    val _timeshets = MutableLiveData<List<Pair<Timesheet, Category>>>()
    val status: MutableLiveData<Pair<RegistrationStatus, String>> = MutableLiveData()


    fun createNewTimesheet(image: Uri?, timesheet: Timesheet){
        taskService.createNewTimesheet(image, timesheet) { success, message ->
            status.postValue(Pair(success, message))
        }
    }

    fun getTimesheetEntries() {
        taskService.getTimesheetEntries { timesheets ->
            if (timesheets.isNotEmpty()) {
                //registrationStatus.postValue(true)
                _timeshets.postValue(timesheets)
            } else {
                status.postValue(Pair(RegistrationStatus.FAILURE, "No Timesheet entries."))
            }
        }
    }

}
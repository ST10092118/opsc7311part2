package com.opsc7311.opsc7311poepart2.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.database.service.TimesheetService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus

class TimesheetViewModel: ViewModel() {

    val timesheetService = TimesheetService()
    val _timesheets = MutableLiveData<List<Pair<Timesheet, Category>>>()
    val status: MutableLiveData<Pair<RegistrationStatus, String>> = MutableLiveData()


    fun createNewTimesheet(image: Uri?, timesheet: Timesheet){
        timesheetService.createNewTimesheet(image, timesheet) { success, message ->
            status.postValue(Pair(success, message))
        }
    }

    fun getTimesheetEntries() {
        timesheetService.getTimesheetEntries { timesheets ->
            if (timesheets.isNotEmpty()) {
                //registrationStatus.postValue(true)
                _timesheets.postValue(timesheets)
                Log.d("timesheets", timesheets.toString())
            } else {
                status.postValue(Pair(RegistrationStatus.FAILURE, "No Timesheet entries."))
            }
        }
    }

}
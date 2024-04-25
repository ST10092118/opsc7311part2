package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.Dao.TimesheetDao
import com.opsc7311.opsc7311poepart2.database.events.CategoryEvent
import com.opsc7311.opsc7311poepart2.database.events.TimesheetEvent
import com.opsc7311.opsc7311poepart2.database.states.CategoryState
import com.opsc7311.opsc7311poepart2.database.states.TimesheetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TimesheetViewModel(private val dao: TimesheetDao): ViewModel() {
    private val _state = MutableStateFlow(TimesheetState())

    fun onEvent(event: TimesheetEvent) {
        when(event){
            TimesheetEvent.hideObject -> {
                _state.update {
                    it.copy(
                        isAddingTimesheet = false
                    )
                }
            }
            TimesheetEvent.saveTimesheet -> TODO()
            is TimesheetEvent.setCategoryId -> {
                _state.update {
                    it.copy(
                        categoryId = event.categoryId
                    )
                }
            }
            is TimesheetEvent.setDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }
            is TimesheetEvent.setEndTime -> {
                _state.update {
                    it.copy(
                        endTime = event.endTime
                    )
                }
            }
            is TimesheetEvent.setName -> {
                _state.update {
                    it.copy(
                        name = event.timesheetName
                    )
                }
            }
            is TimesheetEvent.setStartTime -> {
                _state.update {
                    it.copy(
                        startTime = event.startTime
                    )
                }
            }
            is TimesheetEvent.setUserId -> {
                _state.update {
                    it.copy(
                        userId = event.UserId
                    )
                }
            }
            TimesheetEvent.showDialog -> {
                _state.update {
                    it.copy(
                        isAddingTimesheet = true
                    )
                }
            }
        }
    }
}
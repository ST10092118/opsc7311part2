package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.Dao.CategoryDao
import com.opsc7311.opsc7311poepart2.database.events.CategoryEvent
import com.opsc7311.opsc7311poepart2.database.states.CategoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel(private val dao: CategoryDao): ViewModel() {

    private val _state = MutableStateFlow(CategoryState())

    fun onEvent(event: CategoryEvent){
        when(event){
            CategoryEvent.hideObject -> _state.update {
                it.copy(
                    isAddingCategory = false
                )
            }
            CategoryEvent.saveCategory -> {

            }
            is CategoryEvent.setCategoryName -> {
                _state.update {
                    it.copy(
                        name = event.categoryName
                    )
                }
            }
            is CategoryEvent.setColor -> {
                _state.update {
                    it.copy(
                        color = event.color
                    )
                }
            }
            is CategoryEvent.setUserId -> {
                _state.update {
                    it.copy(
                        userId = event.UserId
                    )
                }
            }
            CategoryEvent.showDialog -> {
                _state.update {
                    it.copy(
                        isAddingCategory = true
                    )
                }
            }
        }
    }
}
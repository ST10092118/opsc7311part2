package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.service.CategoryService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel: ViewModel() {
    val categoryService = CategoryService()
    val _categories = MutableLiveData<List<Category>>()
    val status: MutableLiveData<Pair<RegistrationStatus, String>> = MutableLiveData()


    fun createNewCategory(name: String, color: String){
        categoryService.createNewCategory(name, color) { success, message ->
            status.postValue(Pair(success, message))
        }
    }

    fun getCategories() {
        categoryService.getCategories { categories ->
            if (categories.isNotEmpty()) {
                //registrationStatus.postValue(true)
                _categories.postValue(categories)
            } else {
                status.postValue(Pair(RegistrationStatus.FAILURE, "No categories."))
            }
        }
    }


}
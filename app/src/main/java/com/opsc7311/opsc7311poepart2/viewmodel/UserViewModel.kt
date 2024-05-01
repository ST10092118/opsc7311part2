package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.opsc7311.opsc7311poepart2.database.Dao.UserDao
import com.opsc7311.opsc7311poepart2.database.events.UserEvent
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.states.CategoryState
import com.opsc7311.opsc7311poepart2.database.states.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao): ViewModel() {
    private val _state = MutableStateFlow(UserState())

    fun onEvent(event: UserEvent){
        when(event){
            is UserEvent.registerUser -> {
                val username = _state.value.username
                val password = _state.value.username

                if(username.isBlank() || password.isBlank()){
                    return
                }
                val user = User(
                    username = username,
                    password = password
                )
                viewModelScope.launch{
                    dao.registerUser(user)
                }
                _state.update {
                    it.copy(
                        isAddingUser = false,
                        username = "",
                        password = ""
                    )
                }
            }

            UserEvent.hideObject -> {
                _state.update {
                    it.copy(
                        isAddingUser = false
                    )
                }
            }
            is UserEvent.setPassword -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            is UserEvent.setUsername -> {
                _state.update {
                    it.copy(
                        username = event.username
                    )
                }
            }
            UserEvent.showDialog -> _state.update {
                it.copy(
                    isAddingUser = true
                )
            }
        }
    }
}
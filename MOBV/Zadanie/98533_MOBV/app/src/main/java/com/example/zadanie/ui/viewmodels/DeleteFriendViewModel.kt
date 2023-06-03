package com.example.zadanie.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zadanie.data.DataRepository
import com.example.zadanie.helpers.Evento
import kotlinx.coroutines.launch

class DeleteFriendViewModel(private val repository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    private val _isDeleted = MutableLiveData<Boolean>(false)
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted

    fun deleteFriend(username: String) {
        viewModelScope.launch {
            repository.deleteFriend(username,
                { _message.postValue(Evento(it)) },
                { _isDeleted.postValue(it) })
        }
    }

    fun show(msg: String) {
        _message.postValue(Evento(msg))
    }
}
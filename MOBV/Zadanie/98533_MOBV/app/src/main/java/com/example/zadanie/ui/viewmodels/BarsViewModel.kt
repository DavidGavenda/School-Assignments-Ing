package com.example.zadanie.ui.viewmodels

import androidx.lifecycle.*
import com.example.zadanie.data.DataRepository
import com.example.zadanie.data.db.model.BarItem
import com.example.zadanie.helpers.Evento
import com.example.zadanie.ui.viewmodels.data.MyLocation
import kotlinx.coroutines.launch

enum class Sort {
    NAME_ASC, NAME_DESC, USERS_ASC, USERS_DESC, DIST_ASC, DIST_DESC
}

class BarsViewModel(private val repository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    private var _sortType: MutableLiveData<Sort> = MutableLiveData(Sort.NAME_ASC)
    val myLocation = MutableLiveData<MyLocation>(null)

    val message: LiveData<Evento<String>>
        get() = _message

    val loading = MutableLiveData(false)

    val bars: LiveData<List<BarItem>> = Transformations.switchMap(_sortType) { sort ->
        liveData {
            loading.postValue(true)
            repository.apiBarList { _message.postValue(Evento(it)) }
            loading.postValue(false)
            if (sort == Sort.DIST_ASC || sort == Sort.DIST_DESC) {
                emitSource(repository.dbSortedByDIST(sort, myLocation.value!!))
            } else {
                emitSource(repository.dbBars(sort))
            }
        }
    }

    fun sortList(sortType: Sort) {
        _sortType.value = sortType
    }

    fun refreshData() {
        viewModelScope.launch {
            loading.postValue(true)
            repository.apiBarList { _message.postValue(Evento(it)) }
            loading.postValue(false)
        }
    }

    fun show(msg: String) {
        _message.postValue(Evento(msg))
    }
}
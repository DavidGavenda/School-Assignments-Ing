package com.example.zadanie.data.db

import androidx.lifecycle.LiveData
import com.example.zadanie.data.db.model.BarItem
import com.example.zadanie.data.db.model.FriendItem
import com.example.zadanie.ui.viewmodels.Sort
import com.example.zadanie.ui.viewmodels.data.MyLocation

class LocalCache(private val dao: DbDao) {
    suspend fun insertBars(bars: List<BarItem>) {
        dao.insertBars(bars)
    }

    suspend fun deleteBars() {
        dao.deleteBars()
    }

    fun getBars(sort: Sort): LiveData<List<BarItem>?> {
        return when (sort) {
            Sort.NAME_DESC -> {
                dao.getBarsByNameDESC()
            }
            Sort.USERS_ASC -> {
                dao.getBarsByUsersASC()
            }
            Sort.USERS_DESC -> {
                dao.getBarsByUsersDESC()
            }
            else -> {
                dao.getBarsByNameASC()
            }
        }
    }

    fun getBarsSortedByDIST(sort: Sort, location: MyLocation): LiveData<List<BarItem>?> {
        return when (sort) {
            Sort.DIST_ASC -> {
                dao.getSortedBarsByDistanceASC(location.lat, location.lon)
            }
            Sort.DIST_DESC -> {
                dao.getSortedBarsByDistanceDESC(location.lat, location.lon)
            }
            else -> {
                dao.getBarsByNameASC()
            }
        }
    }

    suspend fun insertFriends(friends: List<FriendItem>) {
        dao.insertFriends(friends)
    }

    fun getFriends(): LiveData<List<FriendItem>?> = dao.getFriends()

    suspend fun deleteFriends() {
        dao.deleteFriends()
    }


}
package com.example.zadanie.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
class FriendItem(
    @PrimaryKey val id: String, val name: String?, val bar_id: String?, val bar_name: String?
) {
    override fun toString(): String {
        return "FriendItem(id='$id', name='$name', bar_id='$bar_id', bar_name='$bar_name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FriendItem) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (bar_id != other.bar_id) return false
        if (bar_name != other.bar_name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + bar_id.hashCode()
        result = 31 * result + bar_name.hashCode()
        return result
    }
}
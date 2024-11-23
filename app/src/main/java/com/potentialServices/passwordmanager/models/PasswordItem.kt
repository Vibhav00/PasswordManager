package com.potentialServices.passwordmanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.potentialServices.passwordmanager.db.PasswordDatabase.Companion.PASSWORD_TABLE

@Entity(tableName = PASSWORD_TABLE)
data class PasswordItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val userName: String,
    val website: String,
    val password: String,
    val description: String,
    var liked: Boolean,
    var recent: Boolean,
    var lastTime:Long,
    var passwordUsed: Int,
)

package com.potentialServices.passwordmanager.repositories

import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.models.PasswordItem

class PasswordRepository(val db: PasswordDatabase) {


    suspend fun upsert(passwordItem: PasswordItem) = db.getDatabaseDaw().upsert(passwordItem)
    fun getAllPassword() = db.getDatabaseDaw().getAllItems()

    suspend fun deleteOnePass(passwordItem: PasswordItem) = db.getDatabaseDaw().deleteOnePassword(passwordItem)


}
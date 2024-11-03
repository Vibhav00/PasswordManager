package com.potentialServices.passwordmanager.db.dao

import androidx.room.*
import com.potentialServices.passwordmanager.models.PasswordItem


@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(passwordItem: PasswordItem):Long

    @Query("select * from passwordTable")
    fun getAllItems():List<PasswordItem>

    @Delete
    suspend fun deleteOnePassword(passwordItem: PasswordItem)


//    @Query("select from password-table where id= :id")
//    fun getOnePassword(id:Int):PasswordItem
}
package com.potentialServices.passwordmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.potentialServices.passwordmanager.db.dao.PasswordDao
import com.potentialServices.passwordmanager.models.PasswordItem


@Database(entities = [PasswordItem::class],  exportSchema = false,version = 1 )
abstract  class PasswordDatabase: RoomDatabase() {



    abstract fun getDatabaseDaw(): PasswordDao

    companion object {
        @Volatile
        private var instance: PasswordDatabase? = null

        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }






        /**
         *  creating the instance of database
         **/
        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PasswordDatabase::class.java,
            "database_main.db"
        )
            .build()
    }

}
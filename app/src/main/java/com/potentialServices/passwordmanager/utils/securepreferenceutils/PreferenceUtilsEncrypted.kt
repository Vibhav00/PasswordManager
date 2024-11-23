package com.potentialServices.passwordmanager.utils.securepreferenceutils

import android.content.Context
import android.content.SharedPreferences
import com.potentialServices.passwordmanager.security.EncryptionDecryption
import com.potentialServices.passwordmanager.utils.constants.Constants

class PreferenceUtilsEncrypted {

    companion object {
        private lateinit var mSharedPreferences: SharedPreferences


        private const val PASSWORD= "pASSwORD"
        private const val PIN = "PiN"
        private const val DEFAULT_PASS_VALUE = ""
        /**
         *  implementing secure shared preferences which can't be easily reverse engineered .
         **/
        fun getSharedPreferences(context: Context): PreferenceUtilsEncrypted.Companion {
            mSharedPreferences =
                context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

            return this

        }



        /** function to set password and get password **/
        fun getPassword(): String {
            return EncryptionDecryption.decrypt(mSharedPreferences.getString(PASSWORD, DEFAULT_PASS_VALUE).toString())
        }

        fun setPassword(name: String): Boolean {
            mSharedPreferences.edit().putString(PASSWORD, EncryptionDecryption.encrypt(name)).apply()
            return true
        }




        /** function to set pin and get pin **/
        fun getPin(): String {
            return EncryptionDecryption.decrypt(mSharedPreferences.getString(PIN, DEFAULT_PASS_VALUE).toString())
        }

        fun setPin(name: String): Boolean {
            mSharedPreferences.edit().putString(PIN,EncryptionDecryption.encrypt( name)).apply()
            return true
        }




    }
}
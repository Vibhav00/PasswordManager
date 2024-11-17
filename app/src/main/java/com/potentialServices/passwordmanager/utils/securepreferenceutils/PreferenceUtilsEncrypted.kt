package com.potentialServices.passwordmanager.utils.securepreferenceutils

import android.content.Context
import android.content.SharedPreferences
import com.potentialServices.passwordmanager.security.EncryptionDecryption
import com.potentialServices.passwordmanager.utils.constants.Constants

class PreferenceUtilsEncrypted {

    companion object {
        private lateinit var mSharedPreferences: SharedPreferences
        const val NME= "vibhav"
        /**
         *  implementing secure shared preferences which can't be easily reverse engineered .
         **/
        fun getSharedPreferences(context: Context): PreferenceUtilsEncrypted.Companion {
            mSharedPreferences =
                context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

            return this

        }


        /** function to set string and get string **/
        fun getName(): String {
            return mSharedPreferences.getString("vibhav", "initial value").toString()
        }

        fun setName(name: String): Boolean {
            mSharedPreferences.edit().putString("vibhav", name).apply()
            return true
        }




        /** function to set password and get password **/
        fun getPassword(): String {
            return EncryptionDecryption.decrypt(mSharedPreferences.getString("vibhav", "one").toString())
        }

        fun setPassword(name: String): Boolean {
            mSharedPreferences.edit().putString("vibhav", EncryptionDecryption.encrypt(name)).apply()
            return true
        }



        /** function to set password and get password **/
        fun getEncryptionKey(): String {
            return mSharedPreferences.getString("vibhav", "initial value").toString()
        }

        fun setEncryptionKey(name: String): Boolean {
            mSharedPreferences.edit().putString("vibhav", name).apply()
            return true
        }



        /** function to set password and get password **/
        fun getRecoveryQuestion(): String {
            return mSharedPreferences.getString("sqn", "").toString()
        }

        fun setRecoveryQuestion(name: String): Boolean {
            mSharedPreferences.edit().putString("sqn", name).apply()
            return true
        }


        /** function to set password and get password **/
        fun getRecoveryAns(): String {
            return mSharedPreferences.getString("sans", "").toString()
        }

        fun setRecoveryAns(name: String): Boolean {
            mSharedPreferences.edit().putString("sans", name).apply()
            return true
        }




        /** function to set pin and get pin **/
        fun getPin(): String {
            return EncryptionDecryption.decrypt(mSharedPreferences.getString("pin", "NopinIsProvided").toString())
        }

        fun setPin(name: String): Boolean {
            mSharedPreferences.edit().putString("pin",EncryptionDecryption.encrypt( name)).apply()
            return true
        }




    }
}
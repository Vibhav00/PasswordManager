package com.potentialServices.passwordmanager.utils.preferenceutils

import android.content.Context
import android.content.SharedPreferences
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.utils.constants.Constants

class PreferenceUtils {


    companion object {
        private lateinit var mSharedPreferences: SharedPreferences

        /**
         *  implementing shared preferences
         **/
        fun getSharedPreferences(context: Context): PreferenceUtils.Companion {
            mSharedPreferences =
                context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

            return this

        }




        /** funtion to get string **/
        fun getName(): String {
            return mSharedPreferences.getString("vibhav", "initial value").toString()
        }

        fun setName(name: String): Boolean {
            mSharedPreferences.edit().putString("vibhav", name).apply()
            return true
        }





        /** funtion to set theme **/
        fun getTheme(): Int {
            return mSharedPreferences.getInt("theme", R.style.default_theme)
        }

        fun setTheme(theme: Int): Boolean {
            mSharedPreferences.edit().putInt("theme", theme).apply()
            return true
        }


        /** funtion to set language **/
        fun getLang(): String {
            return mSharedPreferences.getString("theme", "check").toString()
        }

        fun setLang(name: String): Boolean {
            mSharedPreferences.edit().putString("theme", name).apply()
            return true
        }



        /** funtion to check if app is locked by 4 pin **/
        fun getLockedByFourPin(): Boolean {
            return mSharedPreferences.getBoolean("pin4", false)
        }

        fun setLockedByFourPin(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean("pin4", pin).apply()
            return true
        }


        /** funtion to check if app is locked by fingerprint **/
        fun getLockedByFingerprint(): Boolean {
            return mSharedPreferences.getBoolean("fingerprint", false)
        }

        fun setLockedByFingereprint(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean("fingerprint", pin).apply()
            return true
        }




        /** funtion to check if app is locked by fingerprint **/
        fun getLockedByPassoword(): Boolean {
            return mSharedPreferences.getBoolean("password", false)
        }

        fun setLockedByPassword(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean("password", pin).apply()
            return true
        }




        /** funtion to check if app is having recovery qn  **/
        fun haveRecoveryQn(): Boolean {
            return mSharedPreferences.getBoolean("rq", false)
        }

        fun setHaveRecoveryQn(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean("rq", pin).apply()
            return true
        }



        /** funtion to check if app is having username  **/
        fun getHaveUsername(): Boolean {
            return mSharedPreferences.getBoolean("username_entered", false)
        }

        fun setHaveUsername(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean("username_entered", pin).apply()
            return true
        }

        /** funtion to save the username  **/
        fun getUsername(): String?{
            return mSharedPreferences.getString("username", "")
        }

        fun setUsername(username :String): Boolean {
            mSharedPreferences.edit().putString("username", username).apply()
            return true
        }


    }
}
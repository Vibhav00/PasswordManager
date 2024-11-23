package com.potentialServices.passwordmanager.utils.preferenceutils

import android.content.Context
import android.content.SharedPreferences
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.utils.constants.Constants
import com.potentialServices.passwordmanager.utils.constants.Constants.ENG

class PreferenceUtils {


    companion object {
        private lateinit var mSharedPreferences: SharedPreferences

        private const val THEME = "Theme"
        private const val LANG = "Lang"
        private const val PIN = "pin"
        private const val FINGER_PRINT = "fingerPrint"
        private const val DEFAULT_VALUE = ""
        private const val HAVE_USERNAME = "HaveUsername"
        private const val USERNAME = "UserName"
        private const val HAVE_PASSWORD = "passWoRd"
        /**
         *  implementing shared preferences
         **/
        fun getSharedPreferences(context: Context): PreferenceUtils.Companion {
            mSharedPreferences =
                context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

            return this

        }






        /** funtion to set theme **/
        fun getTheme(): Int {
            return mSharedPreferences.getInt(THEME, R.style.default_theme)
        }

        fun setTheme(theme: Int): Boolean {
            mSharedPreferences.edit().putInt(THEME, theme).apply()
            return true
        }


        /** funtion to set language **/
        fun getLang(): String {
            return mSharedPreferences.getString(LANG, ENG).toString()
        }

        fun setLang(name: String): Boolean {
            mSharedPreferences.edit().putString(LANG, name).apply()
            return true
        }



        /** funtion to check if app is locked by 4 pin **/
        fun getLockedByFourPin(): Boolean {
            return mSharedPreferences.getBoolean(PIN, false)
        }

        fun setLockedByFourPin(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean(PIN, pin).apply()
            return true
        }


        /** funtion to check if app is locked by fingerprint **/
        fun getLockedByFingerprint(): Boolean {
            return mSharedPreferences.getBoolean(FINGER_PRINT, false)
        }

        fun setLockedByFingereprint(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean(FINGER_PRINT, pin).apply()
            return true
        }




        /** funtion to check if app is locked by fingerprint **/
        fun getLockedByPassoword(): Boolean {
            return mSharedPreferences.getBoolean(HAVE_PASSWORD, false)
        }

        fun setLockedByPassword(pin: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean(HAVE_PASSWORD, pin).apply()
            return true
        }




        /** funtion to check if app is having username  **/
        fun getHaveUsername(): Boolean {
            return mSharedPreferences.getBoolean(HAVE_USERNAME, false)
        }

        fun setHaveUsername(usernameEntered: Boolean): Boolean {
            mSharedPreferences.edit().putBoolean(HAVE_USERNAME, usernameEntered).apply()
            return true
        }

        /** funtion to save the username  **/
        fun getUsername(): String?{
            return mSharedPreferences.getString(USERNAME, DEFAULT_VALUE)
        }

        fun setUsername(username :String): Boolean {
            mSharedPreferences.edit().putString(USERNAME, username).apply()
            return true
        }


    }
}
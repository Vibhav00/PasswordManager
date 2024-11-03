package com.potentialServices.passwordmanager.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )

    private val editor = sharedPreferences.edit()

    private val keyTheme = "theme"

    var theme
        get() = sharedPreferences.getInt(keyTheme, 2)
        set(value) {
            editor.putInt(keyTheme, value)
            editor.apply()
        }

    val themeFlag = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )

}
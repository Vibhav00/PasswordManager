package com.potentialServices.passwordmanager.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.potentialServices.passwordmanager.R
import java.util.Locale

class LargelyUsedFunctions {
    companion object{
        fun setTheme(){

        }
         fun setLanguageToUi(activity: AppCompatActivity, language: String) {
            var local: Locale = Locale(language)
            Locale.setDefault(local)
            var configuration = activity.resources.configuration
            configuration.locale = local
            activity.baseContext.resources.updateConfiguration(
                configuration, activity.baseContext.resources.displayMetrics
            )
        }
        fun checkInternetConnection(){

        }

        fun deleteMessageDialog(context: Context, title: String, message: String, deleteFun: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                deleteFun()
            }

            builder.setNegativeButton(context.getString(R.string.cancel)) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }

            val dialog = builder.create()

            // Set background and animations
            dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_background_all_corner))
            dialog.window?.setWindowAnimations(R.style.ZoomDialogAnimation)

            // Show the dialog first
            dialog.show()

            // Resolve the color from the theme using the attribute ?attr/myPrimaryTextColor
            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.myTabIndicatorColor, typedValue, true)
            val color = typedValue.data

            // Set button colors after the dialog is shown
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(color)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(color)
        }

        fun checkPasswordStrength(password: String): Int {
            // Minimum length requirement
            val minLength = 8

            if (password.length < minLength) return 0

            var score = 0

            // Check for character variety
            val hasLowercase = password.any { it.isLowerCase() }
            val hasUppercase = password.any { it.isUpperCase() }
            val hasDigit = password.any { it.isDigit() }
            val hasSpecialChar = password.any { !it.isLetterOrDigit() }

            // Increment score based on variety
            if (hasLowercase) score += 20
            if (hasUppercase) score += 20
            if (hasDigit) score += 20
            if (hasSpecialChar) score += 20

            // Length-based scoring with exponential growth for longer passwords
            score += when {
                password.length >= 20 -> 40
                password.length >= 16 -> 30
                password.length >= 12 -> 20
                password.length >= 10 -> 10
                else -> 0
            }

            // Check for sequences of repeating characters
            val repeatingCharPenalty = password.windowed(3).count { it[0] == it[1] && it[1] == it[2] } * 5
            score -= repeatingCharPenalty

            // Check for common patterns (e.g., "123", "abc")
//            val commonPatterns = listOf("123", "abc", "password", "qwerty")
//            val patternPenalty = commonPatterns.sumOf { pattern ->
//                if (pattern in password.lowercase(Locale.ROOT)) 10 else 0
//            }
//            score -= patternPenalty

            // Check for consecutive keyboard patterns (e.g., "asdf")
            val keyboardRows = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
            val keyboardPatternPenalty = password.windowed(4).count { window ->
                keyboardRows.any { row -> row.contains(window.toLowerCase()) }
            } * 10
            score -= keyboardPatternPenalty

            // Enforce boundaries for the score
            return score.coerceIn(0, 100)
        }

    }
}
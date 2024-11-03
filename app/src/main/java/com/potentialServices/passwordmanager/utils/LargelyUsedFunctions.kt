package com.potentialServices.passwordmanager.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.potentialServices.passwordmanager.R

class LargelyUsedFunctions {
    companion object{
        fun setTheme(){

        }
        fun setLanguage(){

        }
        fun checkInternetConnection(){

        }

        fun deleteMessageDialog(context: Context, title: String, message: String, deleteFun: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton("OK") { dialog, which ->
                deleteFun()
            }

            builder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
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

    }
}
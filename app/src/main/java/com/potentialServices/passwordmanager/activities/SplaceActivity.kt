package com.potentialServices.passwordmanager.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions.Companion.setLanguageToUi
import com.potentialServices.passwordmanager.utils.constants.Constants.SECURE_KEY_LOC
import com.potentialServices.passwordmanager.utils.constants.Constants.SERIALIZABLE_EXTRA_KEY
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        System.loadLibrary(SECURE_KEY_LOC)
        /** initializing the theme before super.onCreate()  **/
        val savedTheme = PreferenceUtils.getSharedPreferences(this).getTheme()
        setTheme(savedTheme)
        setLanguageToUi(this,PreferenceUtils.getSharedPreferences(this).getLang())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splace)
        setStatusBartextColor(savedTheme)

        /** creating the intent for the Pin Activity **/
        val iHome = Intent(this@SplaceActivity, AppPasswordActivity::class.java)


        val fingerprintLock = PreferenceUtils.getSharedPreferences(this).getLockedByFingerprint()
        val lockedByPin = PreferenceUtils.getSharedPreferences(this).getLockedByFourPin()
        val passwordLock = PreferenceUtils.getSharedPreferences(this).getLockedByPassoword()
        if(fingerprintLock){
            iHome.putExtra(SERIALIZABLE_EXTRA_KEY,AppPasswordEvents.CHECK_FINGERPRINT)
        }else if(lockedByPin)
        {
            iHome.putExtra(SERIALIZABLE_EXTRA_KEY,AppPasswordEvents.CHECK_PIN)
        }else if(passwordLock){
            iHome.putExtra(SERIALIZABLE_EXTRA_KEY,AppPasswordEvents.CHECK_PASSWORD)
        }else{
            iHome.putExtra(SERIALIZABLE_EXTRA_KEY,AppPasswordEvents.CREATE_PASSWORD)
        }
        /** starting the looper for 1.5 second delay  **/

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1500)
            startActivity(iHome)
            finish()
        }
    }

    /** function for full screen mode **/
    private fun  fullScreenMode(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED)
    }

    private fun setStatusBartextColor(savedTheme: Int) {
        if(savedTheme !in arrayOf(R.style.RedTheme,R.style.darkTheme))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
    }
}


// i had contributed  in Theme ,Language,Boimetric lock in the previous repo..

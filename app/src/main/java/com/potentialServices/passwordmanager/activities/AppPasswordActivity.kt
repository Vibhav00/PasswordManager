package com.potentialServices.passwordmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.ActivityAppPasswordActivityBinding
import com.potentialServices.passwordmanager.fragments.AppPasswordFragment
import com.potentialServices.passwordmanager.fragments.CheckPinFragment
import com.potentialServices.passwordmanager.fragments.CreateAppPasswordFragment
import com.potentialServices.passwordmanager.fragments.CreatePinFragment
import com.potentialServices.passwordmanager.fragments.FingerprintFragment
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions.Companion.setLanguageToUi
import com.potentialServices.passwordmanager.utils.constants.Constants.SERIALIZABLE_EXTRA_KEY
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils

class AppPasswordActivity : AppCompatActivity() {
    private lateinit var activityAppPasswordActivityBinding: ActivityAppPasswordActivityBinding
    private var myTask = AppPasswordEvents.DEFAULT
    override fun onCreate(savedInstanceState: Bundle?) {
        val savedTheme = PreferenceUtils.getSharedPreferences(this).getTheme()
        setTheme(savedTheme)
        setLanguageToUi(this,PreferenceUtils.getSharedPreferences(this).getLang())
        super.onCreate(savedInstanceState)
        activityAppPasswordActivityBinding = ActivityAppPasswordActivityBinding.inflate(layoutInflater)
        setContentView(activityAppPasswordActivityBinding.root)
        myTask= intent.getSerializableExtra(SERIALIZABLE_EXTRA_KEY)  as AppPasswordEvents
        handleTask()

    }

    private fun handleTask() {
       when(myTask){
           AppPasswordEvents.CREATE_PASSWORD -> {
              supportFragmentManager.beginTransaction().replace(R.id.passwordContainerFragment,CreateAppPasswordFragment()).commit()
           }
           AppPasswordEvents.CHECK_PASSWORD -> {
               supportFragmentManager.beginTransaction().replace(R.id.passwordContainerFragment,AppPasswordFragment()).commit()
           }
           AppPasswordEvents.CHECK_PIN -> {
               supportFragmentManager.beginTransaction().replace(R.id.passwordContainerFragment,CheckPinFragment()).commit()
           }
           AppPasswordEvents.CREATE_PIN -> {
               supportFragmentManager.beginTransaction().replace(R.id.passwordContainerFragment,CreatePinFragment()).commit()
           }
           AppPasswordEvents.DEFAULT -> {
           }

           AppPasswordEvents.CHECK_FINGERPRINT ->{
               supportFragmentManager.beginTransaction().replace(R.id.passwordContainerFragment,FingerprintFragment()).commit()
           }
       }
    }

}
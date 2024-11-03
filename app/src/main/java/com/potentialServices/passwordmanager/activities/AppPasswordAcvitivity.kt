package com.potentialServices.passwordmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.ActivityAppPasswordAcvitivityBinding
import com.potentialServices.passwordmanager.fragments.AppPasswordFragment
import com.potentialServices.passwordmanager.fragments.CheckPinFragment
import com.potentialServices.passwordmanager.fragments.CreateAppPasswordFragment
import com.potentialServices.passwordmanager.fragments.CreatePinFragment
import com.potentialServices.passwordmanager.fragments.FingerprintFragment
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils

class AppPasswordAcvitivity : AppCompatActivity() {
    private lateinit var activityAppPasswordAcvitivityBinding: ActivityAppPasswordAcvitivityBinding
    private var myTask = AppPasswordEvents.DEFAULT
    override fun onCreate(savedInstanceState: Bundle?) {
        val savedTheme = PreferenceUtils.getSharedPreferences(this).getTheme()
        setTheme(savedTheme)
        super.onCreate(savedInstanceState)
        activityAppPasswordAcvitivityBinding = ActivityAppPasswordAcvitivityBinding.inflate(layoutInflater)
        setContentView(activityAppPasswordAcvitivityBinding.root)
        myTask= intent.getSerializableExtra("task")  as AppPasswordEvents
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
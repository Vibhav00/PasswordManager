package com.potentialServices.passwordmanager.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.ActivityAddPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions.Companion.setLanguageToUi
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel

class AddPasswordActivity : AppCompatActivity() {
    /** Main view model **/
    val mainViewModel: MainViewModel by viewModels {
        MainViewModelProviderFactory(application,PasswordRepository(PasswordDatabase(this)))
    }
    private lateinit var activityAddPassBinding: ActivityAddPasswordBinding
    var icon:Int = R.drawable.icon_child
    override fun onCreate(savedInstanceState: Bundle?) {
        val savedTheme = PreferenceUtils.getSharedPreferences(this).getTheme()
        setTheme(savedTheme)
        setLanguageToUi(this,PreferenceUtils.getSharedPreferences(this).getLang())
        super.onCreate(savedInstanceState)
        activityAddPassBinding  = ActivityAddPasswordBinding.inflate(layoutInflater)
        setContentView(activityAddPassBinding.root)
        setStatusBartextColor(savedTheme)
        activityAddPassBinding.btnBackPress.setOnClickListener {
            this.onBackPressed()
        }

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

     /**
      *  for creating the instance of viewmodel
      *
      * **/

//    private fun initRepoAndViewModel(){
//        /** creating instance of the repository  **/
//        var passwordRepository= PasswordRepository(PasswordDatabase(this))
//
//        /** creating instance of the view model  **/
//        var mainViewModelProviderFactory=
//            MainViewModelProviderFactory(application,passwordRepository)
//        /** injecting the repository in view model  **/
//        mainViewModel= ViewModelProvider(this,mainViewModelProviderFactory)[MainViewModel::class.java]
//    }
}
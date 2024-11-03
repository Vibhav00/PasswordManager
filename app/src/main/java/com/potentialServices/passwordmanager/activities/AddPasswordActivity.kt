package com.potentialServices.passwordmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.ActivityAddPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.repositories.PasswordRepository
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
        setTheme(R.style.default_theme)
        super.onCreate(savedInstanceState)
        activityAddPassBinding  = ActivityAddPasswordBinding.inflate(layoutInflater)
        setContentView(activityAddPassBinding.root)
        activityAddPassBinding.btnBackPress.setOnClickListener {
            this.onBackPressed()
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
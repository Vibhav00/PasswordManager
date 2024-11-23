package com.potentialServices.passwordmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.ActivityEditPassBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel

class EditPassActivity : AppCompatActivity() {
    /** Main view model **/
    lateinit var mainViewModel: MainViewModel
    lateinit var binding:ActivityEditPassBinding
    var index:Int=0
    var icon:Int? = null
    companion object{
        const val EXTRA  = "index"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val savedTheme = PreferenceUtils.getSharedPreferences(this).getTheme()
        setTheme(savedTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityEditPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setToolbar()
        getIntentExtra()
        settingRepoAndViewModel()

    }

//    private fun setToolbar() {
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(true)
//        binding.toolbar.setTitle("Edit Password")
//        binding.toolbar.setTitleTextColor(resources.getColor(android.R.color.white, theme))
//        val upArrow =
//            resources.getDrawable(R.drawable.baseline_arrow_back_24, null) // Set your custom arrow icon
//        supportActionBar?.setHomeAsUpIndicator(upArrow)
//    }

    private fun getIntentExtra(){
        index = intent.getIntExtra(EXTRA,0)!!
    }

    private fun settingRepoAndViewModel(){
        /** creating instance of the repository  **/
        var passwordRepository= PasswordRepository(PasswordDatabase(this))

        /** creating instance of the view model  **/
        var mainViewModelProviderFactory=
            MainViewModelProviderFactory(application,passwordRepository)

        /** injecting the repository in view model  **/
        mainViewModel= ViewModelProvider(this,mainViewModelProviderFactory)[MainViewModel::class.java]
    }

}
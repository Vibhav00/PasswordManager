package com.potentialServices.passwordmanager.viewmodelprovider

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.viewmodels.MainViewModel

class MainViewModelProviderFactory(val app:Application,val passwordRepository: PasswordRepository) :ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(passwordRepository, app) as T
    }
}
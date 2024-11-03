package com.potentialServices.passwordmanager.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.security.EncryptionDecryption
import com.potentialServices.passwordmanager.utils.MainActivityEvents
import com.potentialServices.passwordmanager.utils.SortingOder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

 /**
  *  this is the main view model
  *
  * **/
class MainViewModel(private val passwordRepository: PasswordRepository, application: Application):AndroidViewModel(application) {

    //  this is updated to  update the ui
    val passwordList:MutableLiveData<List<PasswordItem>> = MutableLiveData()


     //  This is to store the List So that no new database call is done
    lateinit var mainList :List<PasswordItem>


    // this is to use native keys
    init {

        getAllPass()
    }


    fun getAllPass(){
        CoroutineScope(Dispatchers.IO).launch {
            val encList = passwordRepository.getAllPassword()
            mainList = getPasswordsEnc(encList)

            passwordList.postValue(mainList)
        }
    }


     // to decrypt the password
    private fun getPasswordsEnc(encList: List<PasswordItem>): List<PasswordItem> {
             return  encList.map {
                 it.copy(password = EncryptionDecryption.decrypt(it.password))
             }
    }

     // to set new Password
    fun setPassword(passwordItem: PasswordItem) = viewModelScope.launch{
        val upsert =  async {   passwordRepository.upsert(encryptedPass(passwordItem))}
         upsert.await()
         getAllPass()
    }

     // function to encrypt password
    private fun encryptedPass(passwordItem: PasswordItem): PasswordItem {
           return  passwordItem.copy(password = EncryptionDecryption.encrypt(passwordItem.password))
    }

     // function to delete password
    fun deletePass(passwordItem: PasswordItem) = viewModelScope.launch {
        val deletePass  = async {  passwordRepository.deleteOnePass(passwordItem)}
         deletePass.await()
         getAllPass()
    }


      /**
       *  these are the business logics
       *  search , sort , filter
       *
       * **/

    fun handleMainEvents(mainActivityEvents: MainActivityEvents){
        when(mainActivityEvents){
            is MainActivityEvents.SearchEvent -> {
                val key = mainActivityEvents.key
                passwordList.postValue(mainList.filter {
                    it.userName.toLowerCase().contains(key.lowercase()) || it.website.toLowerCase().contains(key.toLowerCase()) || it.title.contains(key)
                } )
            }
            is MainActivityEvents.SortByTitle ->{
                when(mainActivityEvents.sortingOder){
                    SortingOder.ASCENDING , SortingOder.NONE ->{
                        val list =  mainList.sortedBy {
                            it.title
                        }
                        passwordList.postValue(list!!);
                    }
                    SortingOder.DESCENDING ->{
                        val list =  mainList.sortedByDescending {
                            it.title
                        }
                        passwordList.postValue(list!!);
                    }
                }

            }
            is MainActivityEvents.SortByUsername->{
                when(mainActivityEvents.sortingOder){
                    SortingOder.ASCENDING , SortingOder.NONE ->{
                        val list =  mainList.sortedBy {
                            it.userName
                        }
                        passwordList.postValue(list!!);
                    }
                    SortingOder.DESCENDING ->{
                        val list =   mainList.sortedByDescending {
                            it.userName
                        }
                        passwordList.postValue(list!!);
                    }
                }
            }
            is MainActivityEvents.SortByWebsite ->{
                when(mainActivityEvents.sortingOder){
                    SortingOder.ASCENDING , SortingOder.NONE ->{
                        val list =   mainList.sortedBy {
                            it.website
                        }
                        passwordList.postValue(list!!);
                    }
                    SortingOder.DESCENDING ->{
                        val list =   mainList.sortedByDescending {
                            it.website
                        }
                        passwordList.postValue(list!!);
                    }
                }
            }
        }
    }
}
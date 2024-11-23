package com.potentialServices.passwordmanager.toast

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.potentialServices.passwordmanager.databinding.PasswordmanagerToastBinding

class PasswordManagerToast {
    companion object{
        // function to create toast
        fun showToast(
            context: Context,
            desc: String,
            duration:Int,
        ) {
            val layoutInflater = LayoutInflater.from(context)
            val binding = PasswordmanagerToastBinding.inflate(layoutInflater)
            val layout = binding.root


            binding.toastDesc.text = desc


            // init toast
            val toast = Toast(context.applicationContext)


            // setting duration
            toast.duration=duration



            // Setting layout to toast
            toast.view = layout


            // showing toast
            toast.show()


        }
    }
}
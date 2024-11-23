package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.FragmentAppPasswordBinding
import com.potentialServices.passwordmanager.toast.PasswordManagerToast
import com.potentialServices.passwordmanager.utils.AppPasswordEvents.*
import com.potentialServices.passwordmanager.utils.securepreferenceutils.PreferenceUtilsEncrypted


class AppPasswordFragment : Fragment() {
    private lateinit var fragmentAppPasswordBinding: FragmentAppPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAppPasswordBinding = FragmentAppPasswordBinding.inflate(layoutInflater)
        return fragmentAppPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setOnClickListners()
    }

    private fun setOnClickListners() {
        fragmentAppPasswordBinding.saveBtn.setOnClickListener { 
            checkPass()
        }
    }

    private fun checkPass() {
        val password = fragmentAppPasswordBinding.etPassword.text.toString()
        if (password.isEmpty()) {
            PasswordManagerToast.showToast(this.requireActivity(),getString(R.string.password_can_not_be_empty),Toast.LENGTH_SHORT)
            return;
        }
        val realPaas =
            PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).getPassword()

        if (password == realPaas) {
            val iHome = Intent(this.requireActivity(), MainActivity::class.java)
            startActivity(iHome)
            this.requireActivity().finish()
            return
        }

        PasswordManagerToast.showToast(this.requireContext(),
            getString(R.string.password_is_not_correct),Toast.LENGTH_SHORT)
        return


    }
}
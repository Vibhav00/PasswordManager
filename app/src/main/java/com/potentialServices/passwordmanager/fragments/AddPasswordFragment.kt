package com.potentialServices.passwordmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.activities.AddPasswordActivity
import com.potentialServices.passwordmanager.databinding.FragmentAddPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.security.EncryptionDecryption
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddPasswordFragment : Fragment() {
    private lateinit var addPasswordBinding: FragmentAddPasswordBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelProviderFactory(this.requireActivity().application, PasswordRepository(PasswordDatabase(this.requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addPasswordBinding = FragmentAddPasswordBinding.inflate(layoutInflater)
        return addPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPasswordBinding.imageSelection.setImageResource((activity as AddPasswordActivity).icon)
        onClickListeners()

    }


    /** Handling all click Listeners  **/
    private fun onClickListeners() {
        addPasswordBinding.imageSelection.setOnClickListener {
            findNavController().navigate(R.id.action_addPasswordFragment_to_iconSelectionFragment)
        }

        addPasswordBinding.saveBtn.setOnClickListener {
            setPass()

        }
        addPasswordBinding.cancelButton.setOnClickListener {
            // Show a confirmation dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Cancel")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("Yes") { dialog, which ->
                    // Go back to the previous screen
                    requireActivity().onBackPressed()
                }
                .setNegativeButton("No", null)
                .show()
        }

    }


    /** Function to save  password  **/

    private fun setPass(){
        val title = addPasswordBinding.etTitle.text.toString()
        val username = addPasswordBinding.etUsername.text.toString()
        val password = addPasswordBinding.etPassword.text.toString()
        val details = addPasswordBinding.etDetails.text.toString()
        val website = addPasswordBinding.etWebsite.text.toString()

        if(title.isEmpty()){
            Toast.makeText(this.requireActivity(),"title Can not be empty ",Toast.LENGTH_SHORT).show()
        }
        if(password.isEmpty()){
            Toast.makeText(this.requireActivity(),"password Can not be empty ",Toast.LENGTH_SHORT).show()
        }



        lifecycleScope.launch(Dispatchers.IO){
            mainViewModel.setPassword(
                PasswordItem(
                    0,
                    title,
                    username,
                    website,
                    password,
                    details,
                    false,
                    false,
                    System.currentTimeMillis(),
                    (activity as AddPasswordActivity).icon
                )
            )

        }.invokeOnCompletion {
            this.requireActivity().finish()
        }


    }


}
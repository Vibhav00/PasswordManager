package com.potentialServices.passwordmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.activities.EditPassActivity
import com.potentialServices.passwordmanager.databinding.FragmentEditPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel


class EditPasswordFragment : Fragment() {


    private lateinit var editPasswordBinding: FragmentEditPasswordBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelProviderFactory(this.requireActivity().application, PasswordRepository(
            PasswordDatabase(this.requireContext())
        )
        )
    }
    private var id: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editPasswordBinding = FragmentEditPasswordBinding.inflate(layoutInflater)
        return editPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDataInEditor()
        onclicklisterners()
    }


    private fun updateDataInEditor() {
        mainViewModel.getAllPass()
        val index = (activity as EditPassActivity).index
        mainViewModel.passwordList.observe(this.requireActivity()) { it ->
            it.find {
                it.id == index
            }.let {
                editPasswordBinding.apply {
                    etTitle.setText(it?.title)
                    etUsername.setText(it?.userName)
                    etPassword.setText(it?.password)
                    etDetails.setText(it?.description)
                    etWebsite.setText(it?.website)
                    if((activity as EditPassActivity).icon==null){
                        (activity as EditPassActivity).icon  = it?.passwordUsed!!
                    }
                    (activity as EditPassActivity).icon?.let { it1 ->
                        imageSelection.setImageResource(
                            it1
                        )
                    }
                    id = it?.id!!
                }
            }
        }
    }

    private fun onclicklisterners() {
        editPasswordBinding.apply {
            saveBtn.setOnClickListener {
                setPass()
            }
            cancelButton.setOnClickListener {
                this@EditPasswordFragment.activity?.onBackPressed()
            }
            imageSelection.setOnClickListener {
                findNavController().navigate(R.id.action_editPasswordFragment_to_iconSelectionFragment2)
            }

        }
    }

    private fun setPass() {
        val title = editPasswordBinding.etTitle.text.toString()
        val username = editPasswordBinding.etUsername.text.toString()
        val password = editPasswordBinding.etPassword.text.toString()
        val details = editPasswordBinding.etDetails.text.toString()
        val website = editPasswordBinding.etWebsite.text.toString()

        if (title.isEmpty()) {
            Toast.makeText(this.requireActivity(), getString(R.string.title_can_not_be_empty), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(
                this.requireActivity(),
                getString(R.string.password_can_not_be_empty),
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        mainViewModel.setPassword(
            PasswordItem(
                id,
                title,
                username,
                website,
                password,
                details,
                false,
                false,
                System.currentTimeMillis(),
                (activity as EditPassActivity).icon!!
            )
        ).invokeOnCompletion {
            this.requireActivity().finish()
        }
    }


}
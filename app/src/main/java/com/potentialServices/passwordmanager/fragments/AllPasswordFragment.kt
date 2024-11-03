package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.activities.EditPassActivity
import com.potentialServices.passwordmanager.adapters.PasswordAdapter
import com.potentialServices.passwordmanager.databinding.FragmentAllPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel

class AllPasswordFragment : Fragment(), PasswordAdapter.OnClickItem {
    private lateinit var allPasswordBinding: FragmentAllPasswordBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelProviderFactory(this.requireActivity().application, PasswordRepository(
            PasswordDatabase(this.requireContext())
        )
        )
    }
    private lateinit var passwordAdapter: PasswordAdapter
    private lateinit var passwordManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        allPasswordBinding = FragmentAllPasswordBinding.inflate(layoutInflater)
        return allPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getAllPass()

        mainViewModel.passwordList.observe(this.requireActivity()) {
            passwordAdapter = PasswordAdapter(it, this)
            passwordManager = LinearLayoutManager(this.requireContext())
            allPasswordBinding.recyclerviewPassword.layoutManager = passwordManager
            allPasswordBinding.recyclerviewPassword.adapter = passwordAdapter
        }

    }


    override fun onClick(index: Int) {
        val iHome = Intent(this.requireContext(), EditPassActivity::class.java)
        iHome.putExtra("index", index)
        startActivity(iHome)
    }

    override fun onUpdate(passwordItem: PasswordItem, flag: Boolean) {
            Log.e("vibhav","called for ${flag} ")
            val temp=passwordItem.copy()
            temp.liked=flag
            mainViewModel.setPassword(temp).apply {
//                this.invokeOnCompletion {
//                    Toast.makeText(this@AllPasswordFragment.requireContext(),"password updated ",Toast.LENGTH_SHORT).show()
//                    mainViewModel.getAllPass()
//                }
            }
    }

    override fun onCopy(passwordItem: PasswordItem) {
        val temp=passwordItem.copy()
        temp.lastTime = System.currentTimeMillis();
        mainViewModel.setPassword(temp).apply {
//            this.invokeOnCompletion {
//                mainViewModel.getAllPass()
//            }
        }
    }

    override fun deleteItem(passwordItem: PasswordItem) {

        LargelyUsedFunctions.deleteMessageDialog(this@AllPasswordFragment.requireContext(),"Delete Password",
            "Are you sure you want to delete this Password "
        ) {
            mainViewModel.deletePass(passwordItem).apply {

//                this.invokeOnCompletion {
//                    mainViewModel.getAllPass()
//                }
            }
        }

    }

    companion object {
        fun newInstance() = AllPasswordFragment().apply {}
    }
}

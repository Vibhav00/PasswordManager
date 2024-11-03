package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
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
import com.potentialServices.passwordmanager.databinding.FragmentLikedPasswordBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel


class LikedPasswordFragment : Fragment() ,PasswordAdapter.OnClickItem{

    private lateinit var likedPasswordBinding: FragmentLikedPasswordBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelProviderFactory(this.requireActivity().application, PasswordRepository(
            PasswordDatabase(this.requireContext())
        )
        )
    }
    private lateinit var passwordAdapter: PasswordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        likedPasswordBinding=FragmentLikedPasswordBinding.inflate(layoutInflater)
        return likedPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getAllPass()

        mainViewModel.passwordList.observe(this.requireActivity()) {
            passwordAdapter = PasswordAdapter(getList(it), this)
            likedPasswordBinding.recyclerviewPassword.layoutManager = LinearLayoutManager(this.requireContext())
            likedPasswordBinding.recyclerviewPassword.adapter = passwordAdapter
        }

    }

    private fun getList(list:List<PasswordItem>):List<PasswordItem>{
        return  list.filter {
            it.liked
        }

    }



    override fun onClick(index: Int) {
        val iHome = Intent(this.requireContext(), EditPassActivity::class.java)
        iHome.putExtra("index", index)
        startActivity(iHome)
    }

    override fun onUpdate(passwordItem: PasswordItem, flag: Boolean) {
        val temp=passwordItem.copy()
        temp.liked=flag
        mainViewModel.setPassword(temp).apply {
            this.invokeOnCompletion {
                Toast.makeText(this@LikedPasswordFragment.requireContext(),"password updated ", Toast.LENGTH_SHORT).show()
//                mainViewModel.getAllPass()
            }
        }

    }
    override fun onCopy(passwordItem: PasswordItem) {
        val temp=passwordItem.copy()
        temp.lastTime = System.currentTimeMillis();
        mainViewModel.setPassword(temp).apply {
            this.invokeOnCompletion {
                mainViewModel.getAllPass()
            }
        }
    }

    companion object {
        fun newInstance() = LikedPasswordFragment().apply {}
    }

    override fun deleteItem(passwordItem: PasswordItem) {
        LargelyUsedFunctions.deleteMessageDialog(this@LikedPasswordFragment.requireContext(),"Delete Password",
            "Are you sure you want to delete this Password "
        ) {
            mainViewModel.deletePass(passwordItem).apply {

//                this.invokeOnCompletion {
//                    mainViewModel.getAllPass()
//                }
            }
        }
    }
}
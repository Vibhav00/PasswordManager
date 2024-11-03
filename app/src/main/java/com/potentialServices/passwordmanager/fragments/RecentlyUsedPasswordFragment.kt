package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.activities.EditPassActivity
import com.potentialServices.passwordmanager.adapters.PasswordAdapter
import com.potentialServices.passwordmanager.databinding.FragmentRecentlyUsedPasswordBinding
import com.potentialServices.passwordmanager.models.PasswordItem
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions
import com.potentialServices.passwordmanager.viewmodels.MainViewModel


class RecentlyUsedPasswordFragment : Fragment() ,PasswordAdapter.OnClickItem{
    lateinit var recentlyUsedPasswordBinding: FragmentRecentlyUsedPasswordBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var passwordAdapter: PasswordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recentlyUsedPasswordBinding = FragmentRecentlyUsedPasswordBinding.inflate(layoutInflater)
        return recentlyUsedPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.getAllPass()

        mainViewModel.passwordList.observe(this.requireActivity()) {
            passwordAdapter = PasswordAdapter(getList(it), this)
            recentlyUsedPasswordBinding.recyclerviewPassword.layoutManager = LinearLayoutManager(this.requireContext())
            recentlyUsedPasswordBinding.recyclerviewPassword.adapter = passwordAdapter
        }

    }

    private fun getList(list:List<PasswordItem>):List<PasswordItem>{
        return  list.filter {
            it.lastTime > (System.currentTimeMillis()-1000*60*60*5)
        }.sortedByDescending {
            it.lastTime
        }
    }


    companion object {
        fun newInstance() = RecentlyUsedPasswordFragment().apply {}
    }

    override fun onClick(index: Int) {
        val iHome = Intent(this.requireContext(), EditPassActivity::class.java)
        iHome.putExtra("index", index)
        startActivity(iHome)
    }

    override fun onCopy(passwordItem: PasswordItem) {
        val temp=passwordItem.copy()
        temp.lastTime = System.currentTimeMillis();
        temp.passwordUsed = temp.passwordUsed+1
        mainViewModel.setPassword(temp).apply {
            this.invokeOnCompletion {
//                mainViewModel.getAllPass()
            }
        }
    }

    override fun onUpdate(passwordItem: PasswordItem, flag: Boolean) {
        val temp=passwordItem.copy()
        temp.liked=flag
        mainViewModel.setPassword(temp).apply {
            this.invokeOnCompletion {
                Toast.makeText(this@RecentlyUsedPasswordFragment.requireContext(),"password updated ", Toast.LENGTH_SHORT).show()
                mainViewModel.getAllPass()
            }
        }
    }

    override fun deleteItem(passwordItem: PasswordItem) {
        LargelyUsedFunctions.deleteMessageDialog(this@RecentlyUsedPasswordFragment.requireContext(),"Delete Password",
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
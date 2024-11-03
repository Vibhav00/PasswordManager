package com.potentialServices.passwordmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.activities.AddPasswordActivity
import com.potentialServices.passwordmanager.activities.EditPassActivity
import com.potentialServices.passwordmanager.databinding.FragmentIconSelectionBinding

class IconSelectionFragment : Fragment() {
    private lateinit var fragmentIconSelectionBinding: FragmentIconSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentIconSelectionBinding = FragmentIconSelectionBinding.inflate(layoutInflater)
        return fragmentIconSelectionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(activity){
            is AddPasswordActivity ->  handleClick()
            is EditPassActivity -> handleClick2()
        }




    }

    private fun  handleClick() {
        Toast.makeText(this.requireActivity(),"icon selected for add",Toast.LENGTH_SHORT).show()
        fragmentIconSelectionBinding.document.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_document ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.call.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_phone ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.analytics.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_analytics ; activity?.onBackPressed() }

        fragmentIconSelectionBinding.apple.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_apple ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.child.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_child ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.code.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_code ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.discord.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_discord ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.github.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_github ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.gmail.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_gmail ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.gaming.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_gaming ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.instagram.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_instagram ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.linkedin.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_linkedin ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Health.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_health ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.home.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_home ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Music.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_music ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.love.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_love ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Netflix.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_netflix ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.network.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_network ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.fb.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_facebook ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.snapchat.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_snapchat ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.telegram.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_telegram ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.whatsapp.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_whatsapp ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.work.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_work ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.photo.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icon_photo ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.X.setOnClickListener { (activity as AddPasswordActivity).icon = R.drawable.icons_twitterx ; activity?.onBackPressed() }
    }


    private fun handleClick2() {
        Toast.makeText(this.requireActivity(),"icon selected for edit ",Toast.LENGTH_SHORT).show()
        fragmentIconSelectionBinding.document.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_document ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.call.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_phone ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.analytics.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_analytics ; activity?.onBackPressed() }

        fragmentIconSelectionBinding.apple.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_apple ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.child.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_child ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.code.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_code ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.discord.setOnClickListener{(activity as EditPassActivity).icon = R.drawable.icon_discord ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.github.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_github ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.gmail.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_gmail ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.gaming.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_gaming ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.instagram.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_instagram ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.linkedin.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_linkedin ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Health.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_health ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.home.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_home ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.love.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_love ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.network.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_network ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Music.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_music ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.Netflix.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_netflix ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.fb.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_facebook ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.shopping.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_shopping ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.snapchat.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_snapchat ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.telegram.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_telegram ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.whatsapp.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_whatsapp ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.work.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_work ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.photo.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icon_photo ; activity?.onBackPressed() }
        fragmentIconSelectionBinding.X.setOnClickListener { (activity as EditPassActivity).icon = R.drawable.icons_twitterx ; activity?.onBackPressed() }
    }

}
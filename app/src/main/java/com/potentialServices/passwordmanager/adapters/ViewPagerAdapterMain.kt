package com.potentialServices.passwordmanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.potentialServices.passwordmanager.fragments.AllPasswordFragment
import com.potentialServices.passwordmanager.fragments.LikedPasswordFragment
import com.potentialServices.passwordmanager.fragments.RecentlyUsedPasswordFragment


/** Adapter to switch between different fragments  **/
class ViewPagerAdapterMain(fragment: FragmentActivity):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return  3;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AllPasswordFragment.newInstance();
            1 -> LikedPasswordFragment.newInstance();
            2 -> RecentlyUsedPasswordFragment.newInstance()
            else ->AllPasswordFragment.newInstance();
        }

    }
}
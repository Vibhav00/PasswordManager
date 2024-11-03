package com.potentialServices.passwordmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.FragmentPinBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PinFragment : Fragment() {

    private lateinit var fragmentPinBinding: FragmentPinBinding
    var pos =0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPinBinding = FragmentPinBinding.inflate(layoutInflater)
        return fragmentPinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickListner()
    }

    fun handlePosAndDot(){
        lifecycleScope.launch(Dispatchers.Main) {
            if(pos==3){
                pos+=1
                setDotstoSee()
               delay(500)
                pos=0;
                setDotstoSee()

            }else{
                pos+=1
                setDotstoSee()
            }


        }



    }

    private fun handleClickListner() {
        fragmentPinBinding.llBtnFirst.children.forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(),value.toString(),Toast.LENGTH_SHORT).show()
                handlePosAndDot()

            }
        }
        fragmentPinBinding.llBtnSecond.children.forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(),value.toString(),Toast.LENGTH_SHORT).show()
                handlePosAndDot()
            }
        }
        fragmentPinBinding . llBtnThird . children . forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(), value.toString(), Toast.LENGTH_SHORT).show()
                handlePosAndDot()
            }
        }
        fragmentPinBinding . llBtnFourth . children . forEachIndexed { index,it->
            if(index==1){
                it.setOnClickListener {
                    val value = it.tag;
                    Toast.makeText(this.requireContext(), value.toString(), Toast.LENGTH_SHORT).show()
                    handlePosAndDot()
                }
            }

        }


    }

    private fun setDotstoSee() {
        if (pos == 0) {

            fragmentPinBinding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else if (pos == 1) {
            fragmentPinBinding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)

        } else if (pos == 2) {
            fragmentPinBinding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            fragmentPinBinding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else if (pos == 3) {
            fragmentPinBinding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else {
            fragmentPinBinding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            fragmentPinBinding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
        }
    }


}
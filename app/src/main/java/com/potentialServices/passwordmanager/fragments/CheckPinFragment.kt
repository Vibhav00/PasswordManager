package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.activities.AppPasswordAcvitivity
import com.potentialServices.passwordmanager.databinding.FragmentCheckPinBinding
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.securepreferenceutils.PreferenceUtilsEncrypted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CheckPinFragment : Fragment() {
    private lateinit var  binding: FragmentCheckPinBinding
    private var pos =0;
    private var tempPin:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckPinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
              handleClickListner()

    }

    fun startPasswordFragment(){
        val iHome = Intent(this@CheckPinFragment.requireActivity(), AppPasswordAcvitivity::class.java)
        iHome.putExtra("task", AppPasswordEvents.CHECK_PASSWORD)
        startActivity(iHome)
        this@CheckPinFragment.requireActivity().finish()
    }



    private fun handlePosAndDot(){
        lifecycleScope.launch(Dispatchers.Main) {
            if(pos==3){
                pos+=1
                setDotstoSee()
                checkPin()
                delay(500)
                pos=0;
                setDotstoSee()

            }else{
                pos+=1
                setDotstoSee()
            }
        }

    }

    private fun checkPin() {
        try{
            if(PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).getPin() == tempPin){
                val intent = Intent(this.requireActivity(), MainActivity::class.java)
                startActivity(intent)
                this.requireActivity().finish();
            }else{
                Toast.makeText(this.requireContext(),"Wrong pin ",Toast.LENGTH_SHORT).show()
                tempPin =""
            }
        }catch (e : Exception){

        }

    }

    private fun handleClickListner() {
        binding.llBtnFirst.children.forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(),value.toString(), Toast.LENGTH_SHORT).show()
                tempPin += value
                handlePosAndDot()

            }
        }
        binding.llBtnSecond.children.forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(),value.toString(), Toast.LENGTH_SHORT).show()
                tempPin += value
                handlePosAndDot()
            }
        }
        binding . llBtnThird . children . forEach {
            it.setOnClickListener {
                val value = it.tag;
                Toast.makeText(this.requireContext(), value.toString(), Toast.LENGTH_SHORT).show()
                tempPin += value
                handlePosAndDot()
            }
        }
        binding . llBtnFourth . children . forEachIndexed { index,it->
            if(index==1){
                it.setOnClickListener {
                    val value = it.tag;
                    Toast.makeText(this.requireContext(), value.toString(), Toast.LENGTH_SHORT).show()
                    tempPin += value
                    handlePosAndDot()
                }
            }

        }
        binding.passkey.setOnClickListener {
            startPasswordFragment()
        }

    }

    private fun setDotstoSee() {
        if (pos == 0) {

            binding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else if (pos == 1) {
            binding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)

        } else if (pos == 2) {
            binding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
            binding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else if (pos == 3) {
            binding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_not_selected)
        } else {
            binding.dot1.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot2.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot3.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
            binding.dot4.background =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.pin_dot_selected)
        }
    }
}
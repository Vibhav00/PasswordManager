package com.potentialServices.passwordmanager.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.FragmentCreatePinBinding
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.utils.securepreferenceutils.PreferenceUtilsEncrypted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CreatePinFragment : Fragment() {

    private lateinit var binding:FragmentCreatePinBinding
    private var createPinItem = CreatePinItem()
    var pos =0;
    var tempPin:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickListner()
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

      if(createPinItem.firstRoundCompleted){
          createPinItem.confirmPin = tempPin
          if(createPinItem.firstPin == createPinItem.confirmPin){
              PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).setPin(createPinItem.firstPin)
              PreferenceUtils.getSharedPreferences(this.requireContext())
              Toast.makeText(this.requireContext(),"Pin added Successfully ",Toast.LENGTH_SHORT).show()
              Log.e("the result ",createPinItem.toString())
              createPinItem = CreatePinItem()
              this.requireActivity().finish()
          }
      }else{
          createPinItem.firstRoundCompleted = true
          createPinItem.firstPin = String(tempPin.toCharArray());
          Log.e("the result ",createPinItem.toString())
          tempPin = ""

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
        val rb = binding.rbPinActive as RadioButton
        rb.setOnCheckedChangeListener { compoundButton, b ->
            run {
                if (rb.isChecked) {
                        PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFourPin(true)
                    PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFingereprint(false)
                } else {
                    PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFourPin(false)
                }

            }
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
data class CreatePinItem(
    var firstRoundCompleted:Boolean = false,
    var secondRoundCompleted:Boolean = false,
    var firstPin:String = "",
    var confirmPin:String = ""
)
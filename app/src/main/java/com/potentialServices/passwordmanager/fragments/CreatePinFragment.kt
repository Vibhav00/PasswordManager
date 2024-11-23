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
import com.potentialServices.passwordmanager.toast.PasswordManagerToast
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.utils.securepreferenceutils.PreferenceUtilsEncrypted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CreatePinFragment : Fragment() {

    private lateinit var binding:FragmentCreatePinBinding
    private var createPinItem = CreatePinItem()
    private var pos =0;
    var tempPin:String = ""
    var lockedByFourPin=false

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
        lockedByFourPin = PreferenceUtils.getSharedPreferences(this.requireContext()).getLockedByFourPin()
        setButtonUi()
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

    override fun onDestroyView() {
        super.onDestroyView()
        if(lockedByFourPin)
            PasswordManagerToast.showToast(this.requireContext(),
                getString(R.string.your_pin_is_still_active),Toast.LENGTH_SHORT)

    }

    private fun checkPin() {

      if(createPinItem.firstRoundCompleted){
          createPinItem.confirmPin = tempPin
          if(createPinItem.firstPin == createPinItem.confirmPin){
              PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).setPin(createPinItem.firstPin)
              PreferenceUtils.getSharedPreferences(this.requireContext())
              PasswordManagerToast.showToast(this.requireContext(),
                  getString(R.string.pin_added_successfully),Toast.LENGTH_SHORT)
              this.requireActivity().finish()
          }else{
              PasswordManagerToast.showToast(this.requireContext(),
                  getString(R.string.confirm_pin_was_wrong),Toast.LENGTH_SHORT)
              binding.tvHint.text = getString(R.string.enternewpin)
              createPinItem = CreatePinItem()
              tempPin = ""
          }
      }else{
          createPinItem.firstRoundCompleted = true
          createPinItem.firstPin = String(tempPin.toCharArray());
          tempPin = ""
          lifecycleScope.launch (Dispatchers.Main){
              delay(500)
              binding.tvHint.text = getString(R.string.enternewpinagain)
          }

      }
    }

    private fun handleClickListner() {
        binding.llBtnFirst.children.forEach {
            it.setOnClickListener {
                if(lockedByFourPin){
                    val value = it.tag;
                    tempPin += value
                    handlePosAndDot()
                }else{
                    PasswordManagerToast.showToast(this.requireContext(),
                        getString(R.string.please_activate_pin_lock),Toast.LENGTH_SHORT)
                }


            }
        }
        binding.llBtnSecond.children.forEach {
            it.setOnClickListener {
                if(lockedByFourPin){
                    val value = it.tag;
                    tempPin += value
                    handlePosAndDot()
                }else{
                    PasswordManagerToast.showToast(this.requireContext(),
                        getString(R.string.please_activate_pin_lock),Toast.LENGTH_SHORT)
                }

            }
        }
        binding . llBtnThird . children . forEach {
            it.setOnClickListener {
                if(lockedByFourPin){
                    val value = it.tag;
                    tempPin += value
                    handlePosAndDot()
                }else{
                    PasswordManagerToast.showToast(this.requireContext(),
                        getString(R.string.please_activate_pin_lock),Toast.LENGTH_SHORT)
                }

            }
        }
        binding . llBtnFourth . children . forEachIndexed { index,it->
            if(index==1){
                it.setOnClickListener {
                    if(lockedByFourPin){
                        val value = it.tag;
                        tempPin += value
                        handlePosAndDot()
                    }else{
                        PasswordManagerToast.showToast(this.requireContext(),
                            getString(R.string.please_activate_pin_lock),Toast.LENGTH_SHORT)
                    }

                }
            }

        }

        binding.btnLocked.setOnClickListener {
                if (lockedByFourPin) {
                    lockedByFourPin = false
                    setButtonUi()

                    PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFourPin(false)
                    PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).setPin("")

                } else {
                    lockedByFourPin = true
                    setButtonUi()
                    PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFourPin(true)
                    PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByFingereprint(false)
                }
            tempPin = ""
            createPinItem= CreatePinItem()
            pos =0
            setDotstoSee()
        }

    }

    private fun setButtonUi(){
        if(lockedByFourPin){
            binding.tvHint.text=getString(R.string.enternewpin)
            binding.btnLocked.text = getString(R.string.deactivate)

        }else{
            binding.tvHint.text= getString(R.string.please_activate_pin_lock_to_use)
            binding.btnLocked.text = getString(R.string.activate)
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
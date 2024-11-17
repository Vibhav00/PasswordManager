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
        Toast.makeText(this.requireContext(),"Your Pin Lock is Still Active ",Toast.LENGTH_SHORT).show()

    }

    private fun checkPin() {

      if(createPinItem.firstRoundCompleted){
          createPinItem.confirmPin = tempPin
          if(createPinItem.firstPin == createPinItem.confirmPin){
              PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext()).setPin(createPinItem.firstPin)
              PreferenceUtils.getSharedPreferences(this.requireContext())
              Toast.makeText(this.requireContext(),"Pin added Successfully ",Toast.LENGTH_SHORT).show()
              this.requireActivity().finish()
          }else{
              Toast.makeText(this.requireContext(),"Confirm Pin was wrong  ",Toast.LENGTH_SHORT).show()
              binding.tvHint.text = "Enter New Pin "
              createPinItem = CreatePinItem()
              tempPin = ""
          }
      }else{
          createPinItem.firstRoundCompleted = true
          createPinItem.firstPin = String(tempPin.toCharArray());
          Log.e("the result ",createPinItem.toString())
          tempPin = ""
          lifecycleScope.launch (Dispatchers.Main){
              delay(500)
              binding.tvHint.text = "Enter New Pin Again "
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
                    Toast.makeText(this.requireContext(),"Please Activate Pin Lock", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this.requireContext(),"Please Activate Pin Lock", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this.requireContext(),"Please Activate Pin Lock", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this.requireContext(),"Please Activate Pin Lock", Toast.LENGTH_SHORT).show()
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
            binding.tvHint.text="Enter New Pin "
            binding.btnLocked.text = "Deactivate"
        }else{
            binding.tvHint.text="Please Activate Pin Lock to Use "
            binding.btnLocked.text = "Activate"
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
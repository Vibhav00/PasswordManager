package com.potentialServices.passwordmanager.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.databinding.EntereUsernameDialogBinding
import com.potentialServices.passwordmanager.databinding.FragmentCreateAppPasswordBinding
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.utils.securepreferenceutils.PreferenceUtilsEncrypted
import kotlin.random.Random


class CreateAppPasswordFragment : Fragment() {

    private lateinit var  binding : FragmentCreateAppPasswordBinding
    private var createPasswordItem = CreatePasswordItem()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentCreateAppPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUsernameDialog()
        setonClickListners()
        val passwordLock = PreferenceUtils.getSharedPreferences(this.requireContext()).getLockedByPassoword()
        if(passwordLock){
            setTitleAndDes("Enter Previous  Password", "Please Enter Your Previous Password ")
        }else{
             createPasswordItem.previousPasswordVerified = true
            setTitleAndDes("New Password","Enter your new Password ")
        }
    }

    private fun setTitleAndDes(s: String, s1: String) {
        binding.apply {
            tvHeading.setText(s)
            tvDesc.setText(s1)
            etPassword.setText("")

        }

    }

    private fun setUsernameDialog() {
        if(!PreferenceUtils.getSharedPreferences(this.requireContext()).getHaveUsername()){
             var entereUsernameDialogBinding = EntereUsernameDialogBinding.inflate(layoutInflater)
            val dialog = Dialog(this.requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(entereUsernameDialogBinding.root)

            // Set dialog layout parameters
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER

            dialog.window!!.attributes = lp

            dialog.show()

            entereUsernameDialogBinding.apply {
                saveBtn.setOnClickListener {
                    val usename = this.etUsername.text.toString()
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setHaveUsername(true)
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setUsername(usename)
                    Toast.makeText(
                        this@CreateAppPasswordFragment.requireContext(),
                        "username is saved successfully ",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }

                skipUsernameText.setOnClickListener {
                    val usename = generateRandomUsername()
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setHaveUsername(true)
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setUsername(usename)
                    Toast.makeText(
                        this@CreateAppPasswordFragment.requireContext(),
                        "Random username is assigned  ",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }

            }

        }else{

        }
    }
    private fun generateRandomUsername(length: Int = 8): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { characters[Random.nextInt(characters.length)] }
            .joinToString("")
    }

    private fun setonClickListners() {
        binding.apply {
            saveBtn.setOnClickListener {
                if(createPasswordItem.previousPasswordVerified){
                    Log.e("thisdfs ","lskdjfsjlkdjflsjlfjsdjdfl")
                    if(createPasswordItem.firstPasswordAdded){
                        val text = this.etPassword.text.toString()
                        createPasswordItem.confirmPassword = text;
                        checkThePasswordAndAddSave()
                    }else{
                        val text = this.etPassword.text.toString()
                        createPasswordItem.firstPassword = text;
                        createPasswordItem.firstPasswordAdded = true//tyu
                        etPassword.setText("")
                        setTitleAndDes("New Password","Enter you new password again")
                    }
                }else{
                    val text = this.etPassword.text.toString()
                    if(text.isBlank() || text.isEmpty()){
                        Toast.makeText(this@CreateAppPasswordFragment.requireContext(),"emptly ",Toast.LENGTH_SHORT).show()

                    }else{
                        val pass  = PreferenceUtilsEncrypted.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                            .getPassword()
                        if(pass.equals(text)){
                            createPasswordItem.previousPasswordVerified = true;
                            setTitleAndDes("New Password","Enter your new Password ")

                        }else{
                            setTitleAndDes("Enter Previous  Password", "Please Enter Your Previous Password ")
                            Toast.makeText(this@CreateAppPasswordFragment.requireContext(),"Previous Password is not correct ",Toast.LENGTH_SHORT).show()
                        }

                    }
                }



            }
            cancelButton.setOnClickListener {
                if(createPasswordItem.previousPasswordVerified){
                    setTitleAndDes("New Password","Enter your new Password ")
                }else{
                    setTitleAndDes("Enter Previous  Password", "Please Enter Your Previous Password ")
                }
                etPassword.setText("")

            }
        }
    }

    private fun checkThePasswordAndAddSave() {
        if(createPasswordItem.firstPassword == createPasswordItem.confirmPassword){
            PreferenceUtilsEncrypted.getSharedPreferences(this.requireContext())
                .setPassword(createPasswordItem.firstPassword)
            PreferenceUtils.getSharedPreferences(this.requireContext()).setLockedByPassword(true)
            val iHome = Intent(this.requireActivity(), MainActivity::class.java)
            startActivity(iHome)
            Toast.makeText(this.requireContext(),"Password Updated Successfully ",Toast.LENGTH_SHORT).show()
            this.requireActivity().finish()
        }else{
            Toast.makeText(this.requireContext(),"Confirm Password is not same",Toast.LENGTH_SHORT).show()
            createPasswordItem= CreatePasswordItem(previousPasswordVerified = true);
            setTitleAndDes("New Password","Enter your new Password ")

        }
    }
}
data class  CreatePasswordItem(
    var previousPasswordVerified:Boolean = false,
    var firstPassword:String = "",
    var confirmPassword:String = "",
    var firstPasswordAdded:Boolean = false,
    var secondPasswordAdded:Boolean = false
)
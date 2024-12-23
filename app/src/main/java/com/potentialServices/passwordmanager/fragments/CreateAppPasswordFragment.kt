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
import androidx.core.content.ContextCompat
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.EntereUsernameDialogBinding
import com.potentialServices.passwordmanager.databinding.FragmentCreateAppPasswordBinding
import com.potentialServices.passwordmanager.toast.PasswordManagerToast
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions.Companion.checkPasswordStrength
import com.potentialServices.passwordmanager.utils.constants.Constants.A_Z
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
            setTitleAndDes(getString(R.string.enter_previous_password), getString(R.string.please_enter_your_previous_password))
        }else{
             createPasswordItem.previousPasswordVerified = true
            setTitleAndDes(getString(R.string.new_password),getString(R.string.enter_your_new_password))
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

            dialog.window!!.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@CreateAppPasswordFragment.requireContext(),
                    R.drawable.pop_up_background
                )
            )
            dialog.window!!.attributes = lp

            dialog.show()

            entereUsernameDialogBinding.apply {
                saveBtn.setOnClickListener {
                    val usename = this.etUsername.text.toString()
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setHaveUsername(true)
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setUsername(usename)
                    PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),
                        getString(
                            R.string.username_is_saved_successfully
                        ),Toast.LENGTH_SHORT)
                    dialog.dismiss()
                }

                skipUsernameText.setOnClickListener {
                    val usename = generateRandomUsername()
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setHaveUsername(true)
                    PreferenceUtils.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                        .setUsername(usename)
                    PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),
                        getString(
                            R.string.random_username_is_assigned
                        ),Toast.LENGTH_SHORT)

                    dialog.dismiss()
                }

            }

        }else{

        }
    }
    private fun generateRandomUsername(length: Int = 8): String {
        val characters = A_Z
        return (1..length)
            .map { characters[Random.nextInt(characters.length)] }
            .joinToString("")
    }

    private fun setonClickListners() {
        binding.apply {
            saveBtn.setOnClickListener {
                if(createPasswordItem.previousPasswordVerified){
                    if(createPasswordItem.firstPasswordAdded){
                        val text = this.etPassword.text.toString()
                        createPasswordItem.confirmPassword = text;
                        checkThePasswordAndAddSave()
                    }else{
                        val text = this.etPassword.text.toString()
                        if(text.length<8){
                            PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),"Password could not be less than 8 character",Toast.LENGTH_SHORT)
                            return@setOnClickListener
                        }
                        if(checkPasswordStrength(text) <40){
                            PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),"Please choose strong password ",Toast.LENGTH_SHORT)
                            return@setOnClickListener
                        }
                        createPasswordItem.firstPassword = text;
                        createPasswordItem.firstPasswordAdded = true//tyu
                        etPassword.setText("")
                        setTitleAndDes(getString(R.string.new_password),
                            getString(R.string.enter_you_new_password_again))
                    }
                }else{
                    val text = this.etPassword.text.toString()
                    if(text.isBlank() || text.isEmpty()){
//                        Toast.makeText(this@CreateAppPasswordFragment.requireContext(),"emptly ",Toast.LENGTH_SHORT).show()

                    }else{
                        val pass  = PreferenceUtilsEncrypted.getSharedPreferences(this@CreateAppPasswordFragment.requireContext())
                            .getPassword()
                        if(pass.equals(text)){
                            createPasswordItem.previousPasswordVerified = true;
                            setTitleAndDes(getString(R.string.new_password),
                                getString(R.string.enter_your_new_password))

                        }else{
                            setTitleAndDes(getString(R.string.enter_previous_password),
                                getString(R.string.please_enter_your_previous_password))
                            Toast.makeText(this@CreateAppPasswordFragment.requireContext(),
                                getString(
                                    R.string.previous_password_is_not_correct
                                ),Toast.LENGTH_SHORT).show()
                        }

                    }
                }



            }
            cancelButton.setOnClickListener {
                if(createPasswordItem.previousPasswordVerified){
                    setTitleAndDes(getString(R.string.new_password),getString(R.string.enter_your_new_password))
                }else{
                    setTitleAndDes(getString(R.string.enter_previous_password), getString(R.string.please_enter_your_previous_password))
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
            PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),
                getString(R.string.password_updated_successfully),Toast.LENGTH_SHORT)
            startActivity(iHome)
            this.requireActivity().finish()
        }else{
            PasswordManagerToast.showToast(this@CreateAppPasswordFragment.requireContext(),
                getString(R.string.confirm_password_is_not_same),Toast.LENGTH_SHORT)
            createPasswordItem= CreatePasswordItem(previousPasswordVerified = true);
            setTitleAndDes(getString(R.string.new_password),getString(R.string.enter_your_new_password))

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
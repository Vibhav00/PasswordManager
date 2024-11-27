package com.potentialServices.passwordmanager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.potentialServices.passwordmanager.MainActivity
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.activities.AppPasswordActivity
import com.potentialServices.passwordmanager.databinding.FragmentFingerprintBinding
import com.potentialServices.passwordmanager.toast.PasswordManagerToast
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.constants.Constants.SERIALIZABLE_EXTRA_KEY
import java.util.concurrent.Executor


class FingerprintFragment : Fragment() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var binding : FragmentFingerprintBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFingerprintBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        executor = ContextCompat.getMainExecutor(this.requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    PasswordManagerToast.showToast(this@FingerprintFragment.requireContext(),
                        getString(
                            R.string.open_via_password
                        ),Toast.LENGTH_SHORT)

                    val iHome = Intent(this@FingerprintFragment.requireActivity(), AppPasswordActivity::class.java)
                    iHome.putExtra(SERIALIZABLE_EXTRA_KEY, AppPasswordEvents.CHECK_PASSWORD)
                    startActivity(iHome)
                    this@FingerprintFragment.requireActivity().finish()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    PasswordManagerToast.showToast(this@FingerprintFragment.requireContext(),
                        getString(
                            R.string.authentication_succeed
                        ),Toast.LENGTH_SHORT)
                    // Navigate to MainActivity after successful authentication
                    val intent = Intent(this@FingerprintFragment.requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    this@FingerprintFragment.requireActivity().finish()  // Optionally finish the current activity

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    PasswordManagerToast.showToast(this@FingerprintFragment.requireContext(),
                        getString(
                            R.string.authentication_failed
                        ),Toast.LENGTH_SHORT)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_login))
            .setSubtitle(getString(R.string.login_with_biometric))
            .setNegativeButtonText(getString(R.string.use_app_password))
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
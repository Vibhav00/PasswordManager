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
import com.potentialServices.passwordmanager.activities.AppPasswordAcvitivity
import com.potentialServices.passwordmanager.databinding.FragmentFingerprintBinding
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
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
                    Toast.makeText(
                        this@FingerprintFragment.requireContext(),
                        "Open Via Password ",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    val iHome = Intent(this@FingerprintFragment.requireActivity(), AppPasswordAcvitivity::class.java)
                    iHome.putExtra("task", AppPasswordEvents.CHECK_PASSWORD)
                    startActivity(iHome)
                    this@FingerprintFragment.requireActivity().finish()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@FingerprintFragment.requireContext(), "Authentication Succeed", Toast.LENGTH_SHORT)
                        .show()

                    // Navigate to MainActivity after successful authentication
                    val intent = Intent(this@FingerprintFragment.requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    this@FingerprintFragment.requireActivity().finish()  // Optionally finish the current activity

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@FingerprintFragment.requireContext(), "Authentication Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Login with biometric")
            .setNegativeButtonText("Use App Password ")
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
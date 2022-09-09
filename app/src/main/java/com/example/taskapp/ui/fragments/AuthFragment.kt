package com.example.taskapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentAuthBinding

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initClicker()
        checkUser()
        userRegistrationDate()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("auth", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("auth", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("auth", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun userRegistrationDate() {
        val date: DateFormat = SimpleDateFormat("MMM dd yyyy, h:mm")
        val dateFormatted = date.format(Calendar.getInstance().time)
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("userDateRegistration", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("dateRegistration", dateFormatted.toString()).apply()
    }

    private fun initClicker() {
        binding.btnSendSms.setOnClickListener {
            startPhoneNumberVerification(binding.etNumber.text.toString())
        }
        binding.btnEnterCode.setOnClickListener {
            verifyPhoneNumberWithCode(storedVerificationId, binding.etConfirmNumber.text.toString())
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    findNavController().navigate(R.id.createProfileFragment)
                    val user = task.result?.user
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            findNavController().navigate(R.id.createProfileFragment)
        }
    }
}
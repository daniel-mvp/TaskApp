package com.example.taskapp.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentConfirmPasswordBinding
import com.google.firebase.ktx.Firebase

class ConfirmPasswordFragment : Fragment() {

    private lateinit var binding: FragmentConfirmPasswordBinding
    private val fireStore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            if (checkForInternet(requireContext())) {
                fireStore.collection("user").get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (binding.etConfirmPassword.text.toString() == document.get("password")) {
                            findNavController().navigate(R.id.homeFragment)
                            Toast.makeText(
                                requireContext(),
                                "Вы успешно вошли в аккаунт!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(requireContext(), "Пароль неверный!", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                Toast.makeText(requireContext(), "Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Disconnected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network)

            return when {
                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false

            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
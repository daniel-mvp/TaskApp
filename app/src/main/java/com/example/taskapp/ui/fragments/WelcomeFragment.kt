package com.example.taskapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentWelcomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val fireStore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireStore.collection("user").get().addOnSuccessListener { result ->
            for (document in result) {
                binding.tvWelcome.text = "Welcome \n ${document.get("name")}"
            }
        }.addOnFailureListener { exception ->
            Log.e("firestore", "Error get documents", exception)
        }
        binding.btnApply.setOnClickListener {
            findNavController().navigate(R.id.onBoardingFragment)
        }
    }

}
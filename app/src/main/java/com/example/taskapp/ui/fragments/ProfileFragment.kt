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
import com.bumptech.glide.Glide
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private val fireStore = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("IntentReset")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addUserInfo()
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference.child("myImages")
        val userImage = storageRef!!.listAll()
        userImage.addOnCompleteListener { result ->
            val items = result.result!!.items
            items.forEachIndexed { index, storageReference ->
                storageReference.downloadUrl.addOnSuccessListener {
                    Log.d("item", "$it")
                    //загрузка фотки пользователя с firebase
                    Glide.with(binding.ivProfileImage).load(it).into(binding.ivProfileImage)
                }
            }
        }

    }


    private fun addUserInfo() {
        //userRegistrationDate
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("userDateRegistration", Context.MODE_PRIVATE)
        val userDateRegistration =
            sharedPreferences.getString("dateRegistration", "ошибочка:(").toString()
        binding.tvUserLoggedIn.text = userDateRegistration
        fireStore.collection("user").get().addOnSuccessListener { result ->
            for (document in result) {
                with(binding) {
                    //user name
                    tvUserName.text = document.get("name").toString()
                    //user password
                    tvUserPassword.text = document.get("password").toString()
                }
            }
        }
        //quantity task user
        fireStore.collection("task").get().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                result.result?.apply {
                    binding.tvUserNumberOfTask.text = size().toString()
                }
            } else {
                result.exception?.message?.let {
                    print(it)
                }
            }
        }
    }

    private fun downloadImage(reference: StorageReference) {
        reference.downloadUrl.addOnSuccessListener {
            val url: String = it.toString()
            Glide.with(requireContext()).load(url).into(binding.ivProfileImage)
        }.addOnFailureListener {
            // Handle any errors
        }

    }
}
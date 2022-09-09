package com.example.taskapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentCreateProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class CreateProfileFragment : Fragment() {

    private lateinit var binding: FragmentCreateProfileBinding
    private val fireStore = Firebase.firestore
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicker()
        checkUser()

        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference
    }

    private fun initClicker() {
        with(binding) {
            btnAccept.setOnClickListener {
                uploadImage()
                val sharedPreferences: SharedPreferences =
                    requireContext().getSharedPreferences("isUserLogin", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("name", etNickUser.text.toString()).apply()
                val user = hashMapOf(
                    "name" to etNickUser.text.toString(),
                    "password" to etPassword.text.toString().trim()
                )
                fireStore.collection("user").add(user).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Вы успешно зарегистрировались!",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.welcomeFragment)
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Повторите попытку", Toast.LENGTH_LONG).show()
                }

            }
            ivAddUserImage.setOnClickListener {
                launchGallery()
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            imageUri = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                binding.ivAddUserImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val ref = storageRef?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(imageUri!!)

        } else {
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkUser() {
        fireStore.collection("user").get().addOnSuccessListener { result ->
            for (document in result) {
                val sharedPreferences: SharedPreferences =
                    requireContext().getSharedPreferences("isUserLogin", Context.MODE_PRIVATE)
                val checkLogin = sharedPreferences.getString("name", "Анонимус")
                if (checkLogin.toString() == document.get("name")) {
                    findNavController().navigate(R.id.confirmPasswordFragment)
                }
            }
        }
    }
}
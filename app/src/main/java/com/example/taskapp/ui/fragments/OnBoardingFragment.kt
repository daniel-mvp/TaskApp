package com.example.taskapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentOnBoardingBinding
import com.example.taskapp.ui.adapters.OnBoardingAdapter
import com.example.taskapp.ui.fragments.models.OnBoardingModel
import com.example.taskapp.ui.inter.OnItemClicker

class OnBoardingFragment : Fragment(), OnItemClicker {

    private lateinit var binding: FragmentOnBoardingBinding
    private val list = arrayListOf<OnBoardingModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.add(OnBoardingModel("animation/save_time.json", "Экономь время", "Дальше"))
        list.add(OnBoardingModel("animation/reach_your_goal.json", "Достигай целей", "Дальше"))
        list.add(OnBoardingModel("animation/develop.json", "Развивайся", "Начинаем"))
        binding.viewPager.adapter = OnBoardingAdapter(list, this)
        binding.dotsIndicator.attachTo(binding.viewPager)

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("board_preferences", Context.MODE_PRIVATE)
        val isShow = sharedPreferences.getBoolean("isShow", false)
        if (isShow) {
            findNavController().navigate(R.id.clearBackStack)
        }

    }

    override fun onClick() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("board_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isShow", true).apply()
        findNavController().navigate(R.id.clearBackStack)

    }

    override fun onClickNext() {
        onNext()
    }

    private fun onNext() {
        val adapter = binding.viewPager.adapter
        val currentPosition = binding.viewPager.currentItem
        val nextPosition = currentPosition + 1
        if (nextPosition < adapter?.itemCount!!) {
            binding.viewPager.currentItem = nextPosition
        } else
            binding.viewPager.currentItem = 0
    }
}
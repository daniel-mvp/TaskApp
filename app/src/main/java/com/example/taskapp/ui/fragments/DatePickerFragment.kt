package com.example.taskapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentDatePickerBinding

class DatePickerFragment : DialogFragment(){
    private lateinit var binding: FragmentDatePickerBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDatePickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        binding.calendarViewRealizarReserva.setOnDateChangeListener{view, year, month, dayOfMonth->
            val msg =  "" + dayOfMonth + "." + (month + 1) + "." + year

            val bundle = Bundle()
            bundle.putString("key", msg.toString())

            setFragmentResult("myKey", bundle)
        }
    }


}

package com.vovan.diplomaapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.TitleFragmentDirections
import com.vovan.diplomaapp.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_title,
            container,
            false)

        binding.sensorsButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToDataFragment())
        }


        binding.ledButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToLedControllerFragment())
        }

        binding.listButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToSensorsDataList())
        }



        return binding.root
    }

}
package com.vovan.diplomaapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.TitleFragmentDirections
import com.vovan.diplomaapp.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTitleBinding.inflate(
            inflater,
            container,
            false).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding){
        sensorsButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToDataFragment())
        }

        ledButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToLedControllerFragment())
        }

        listButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToSensorsDataList())
        }
    }

}
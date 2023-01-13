package com.ultimate.ultimatesophos.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projects.sophosapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    interface WelcomeFragmentListener {
        fun goToSendDocuments()
        fun goToWatchDocuments()
        fun goToOffices()
    }

    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initialize() {
        binding.sendDocumentsButton.setOnClickListener {
            (activity as WelcomeFragmentListener).goToSendDocuments()
        }
        binding.watchDocumentsButton.setOnClickListener {
            (activity as WelcomeFragmentListener).goToWatchDocuments()
        }
        binding.officesButton.setOnClickListener {
            (activity as WelcomeFragmentListener).goToOffices()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

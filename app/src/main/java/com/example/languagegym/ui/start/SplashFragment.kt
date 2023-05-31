package com.example.languagegym.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.languagegym.MainActivity
import com.example.languagegym.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       findNavController().navigate(R.id.action_splashFragment_to_categoryFragment)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setToolbarVisibility(false)
    }
    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.setToolbarVisibility(true)
    }

}
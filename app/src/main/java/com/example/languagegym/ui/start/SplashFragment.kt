package com.example.languagegym.ui.start

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.languagegym.MainActivity
import com.example.languagegym.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
    /**If user is not authorized send to login fragment else send to home fragment **/
        Handler().postDelayed({
            if (user != null) {
                findNavController().navigate(R.id.action_splashFragment_to_categoryFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)
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
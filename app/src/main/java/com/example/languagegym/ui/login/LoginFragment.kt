package com.example.languagegym.ui.login

import android.content.ContentValues.TAG
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.languagegym.MainActivity
import com.example.languagegym.databinding.FragmentLoginBinding
import com.example.languagegym.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.loginWithGoogle.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        //todo updateUI(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "onActivityResult: firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "onActivityResult: Google sign in failed", e)
                }
            } else {
                Log.w("onActivityResult: ", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "firebaseAuthWithGoogle: success")
                        findNavController().navigate(R.id.action_loginFragment_to_categoryFragment)
                    } else {
                        Log.w(TAG, "firebaseAuthWithGoogle: failure", task.exception)
                        Snackbar.make(requireView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        showLoginFailed(R.string.login_failed)
                    }
                }
        } else {
            Log.w(TAG, "ID token is null")
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setToolbarVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.setToolbarVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
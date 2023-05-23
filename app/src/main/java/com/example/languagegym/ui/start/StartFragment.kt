package com.example.languagegym.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.languagegym.R

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //todo change it
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_start, container, false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // the back button pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
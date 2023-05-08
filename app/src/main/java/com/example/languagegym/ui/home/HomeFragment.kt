package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.R
import com.example.languagegym.data.Word
import com.example.languagegym.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private fun showDetails(word: Word) {
        val detailsFragment = DetailsFragment()
        val bundle = Bundle()
        bundle.putParcelable("word", word)
        detailsFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val words = mutableListOf<Word>()
        for (i in 1..50) {
            val word = Word(
                "Word $i",
                "Translation $i",
                "Part of speech $i",
                "Gender $i",
                listOf("Declension 1 $i", "Declension 2 $i"),
                listOf("Synonym 1 $i", "Synonym 2 $i"),
                null,
                50
            )
            words.add(word)
        }
        val recyclerView: RecyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecyclerViewAdapter(requireContext(), words)
        adapter.setClickListener(object : RecyclerViewAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                showDetails(words[position])
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
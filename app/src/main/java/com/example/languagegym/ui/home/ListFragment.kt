package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.R
import com.example.languagegym.helpers.DictionaryDatabaseHelper
import com.example.languagegym.model.WordModel
import com.example.languagegym.databinding.FragmentListBinding
import com.example.languagegym.ui.add.AddWordFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment : Fragment(), AddWordFragment.OnWordAddedListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var databaseHelper: DictionaryDatabaseHelper
    private lateinit var fab: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fab = binding.fab
        fab.hide()

        databaseHelper = DictionaryDatabaseHelper(requireContext())

        setupSpinner()

        fab.setOnClickListener {
            val addWordFragment = AddWordFragment()
            addWordFragment.onWordAddedListener = this
            findNavController().navigate(R.id.action_nav_home_to_addWordFragment)
        }

        return binding.root
    }

    fun loadWordsByFilter(filter: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val words = withContext(Dispatchers.IO) {
                    if (filter == "All") {
                        databaseHelper.getAllWords()
                    } else {
                        databaseHelper.getWordsByPartOfSpeech(filter)
                    }
                }
                setupRecyclerView(words)
                updateRecyclerView(words)
                fab.show()
                showToast("Words loaded successfully")
            } catch (e: Exception) {
                showToast("Failed to load words")
            }
        }
    }


    private fun setupRecyclerView(words: List<WordModel>) {
        adapter = RecyclerViewAdapter(
            requireContext(),
            words,
            object : RecyclerViewAdapter.OnWordItemClickListener {

                override fun onWordItemClick(word: WordModel) {
                    val bundle = Bundle().apply {
                        putParcelable("word", word)
                    }
                    findNavController().navigate(R.id.action_nav_home_to_detailsFragment, bundle)
                }

                override fun onWordItemDeleteClick(word: WordModel) {
                    //todo implementation
                }
            })
        recyclerView.adapter = adapter
    }

    private fun updateRecyclerView(allWords: List<WordModel>) {
        adapter.updateData(allWords)
    }


    private fun setupSpinner() {
        val spinner = binding.spinnerFilter

        val partsOfSpeech = listOf(
            "All",
            "Noun",
            "Verb",
            "Adjective",
            "Adverb",
            "Preposition",
            "Conjunction",
            "Interjection"
        )

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, partsOfSpeech)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPartOfSpeech = partsOfSpeech[position]
                loadWordsByFilter(selectedPartOfSpeech)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showToast(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        databaseHelper.close()
        _binding = null
    }

    override fun onWordAdded() {
        Toast.makeText(
            requireContext(),
            "onWordAdded()",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
        updateRecyclerView(databaseHelper.getAllWords())
    }
}
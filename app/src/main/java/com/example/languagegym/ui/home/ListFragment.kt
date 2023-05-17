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
import com.example.languagegym.data.DictionaryDatabaseHelper
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.FragmentListBinding
import com.example.languagegym.ui.add.AddWordFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment : Fragment(), AddWordFragment.OnWordAddedListener {

    private var _binding: FragmentListBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var databaseHelper: DictionaryDatabaseHelper
    private lateinit var fab: FloatingActionButton
    private val binding get() = _binding!!
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

        loadAllWordsToList()

        fab.setOnClickListener {
            val addWordFragment = AddWordFragment()
            addWordFragment.onWordAddedListener = this  // Установка слушателя
            findNavController().navigate(R.id.action_nav_home_to_addWordFragment)
        }

        setupSpinnerAndRecyclerView()

        return binding.root
    }

    private fun loadAllWordsToList() {
        GlobalScope.launch(Dispatchers.IO) {
            val words = databaseHelper.getAllWords()
            withContext(Dispatchers.Main) {
                setupRecyclerView(words)
                updateRecyclerView(words)
                fab.show()
            }
        }
    }


     fun filterByPartOfSpeech(partOfSpeech: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val words = databaseHelper.getWordsByPartOfSpeech(partOfSpeech)
            withContext(Dispatchers.Main) {
                setupRecyclerView(words)
                updateRecyclerView(words)
                fab.show()
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


    private fun setupSpinnerAndRecyclerView() {
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

        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, partsOfSpeech)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPartOfSpeech = partsOfSpeech[position]
                if (selectedPartOfSpeech == "All") {
                    loadAllWordsToList()
                } else {
                    filterByPartOfSpeech(selectedPartOfSpeech)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
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
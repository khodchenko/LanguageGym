package com.example.languagegym.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.MainActivity
import com.example.languagegym.R
import com.example.languagegym.model.WordModel
import com.example.languagegym.databinding.FragmentListBinding
import com.example.languagegym.model.DictionaryDatabase
import com.example.languagegym.model.WordDao
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
    private lateinit var wordDao: WordDao
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        fab = binding.fab

        setupRecyclerView()

        wordDao = DictionaryDatabase.getInstance(requireContext()).wordDao()

        setupSpinner()

        fab.setOnClickListener {
            navigateToAddWordFragment()
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setContextToolbarButtonClickListener {
            showToast("ListFragment")
            showPartOfSpeechFilterDialog()
        }
    }

    private fun showPartOfSpeechFilterDialog() {
        val filterOptions = arrayOf("Noun", "Verb", "Adjective", "Adverb")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Filter by Part of Speech")
            .setItems(filterOptions) { dialog, which ->
                val partOfSpeech = filterOptions[which]
                loadDataByFilter(partOfSpeech)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecyclerViewAdapter(
            requireContext(),
            emptyList(),
            object : RecyclerViewAdapter.OnWordItemClickListener {
                override fun onWordItemClick(word: WordModel) {
                    navigateToDetailsFragment(word)
                }

                override fun onWordItemDeleteClick(word: WordModel) {
                    deleteWord(word)
                }
            }
        )
        recyclerView.adapter = adapter
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

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            partsOfSpeech
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPartOfSpeech = partsOfSpeech[position]
                loadDataByFilter(selectedPartOfSpeech)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadDataByFilter(filter: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val words = withContext(Dispatchers.IO) {
                    if (filter == "All") {
                        wordDao.getAllWords()
                    } else {
                        wordDao.getWordsByPartOfSpeech(filter)
                    }
                }
                updateRecyclerView(words)
                fab.show()
                showToast("Words loaded successfully")
            } catch (e: Exception) {
                showToast("Failed to load words")
            }
        }
    }

    private fun updateRecyclerView(allWords: List<WordModel>) {
        adapter.updateData(allWords)
    }

    private fun navigateToAddWordFragment() {
        val addWordFragment = AddWordFragment()
        addWordFragment.onWordAddedListener = this
        findNavController().navigate(R.id.action_nav_home_to_addWordFragment)
    }

    private fun navigateToDetailsFragment(word: WordModel) {
        val bundle = Bundle().apply {
            putParcelable("word", word)
        }
        findNavController().navigate(R.id.action_nav_home_to_detailsFragment, bundle)
    }

    private fun deleteWord(word: WordModel) {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.deleteWord(word)
            val updatedWords = wordDao.getAllWords()
            updateRecyclerView(updatedWords)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWordAdded() {
        Toast.makeText(requireContext(), "onWordAdded()", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
        loadDataByFilter("All")
    }
}
package com.example.languagegym.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.languagegym.R
import com.example.languagegym.helpers.TextToSpeechHelper
import com.example.languagegym.ui.model.WordModel
import com.example.languagegym.databinding.FragmentDetailsBinding
import com.example.languagegym.ui.model.DictionaryDatabase
import com.example.languagegym.ui.model.WordDao
import com.example.languagegym.ui.add.AddWordFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var isEditMode = false
    private lateinit var wordDao: WordDao
    private lateinit var textToSpeechHelper: TextToSpeechHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        wordDao = DictionaryDatabase.getInstance(requireContext()).wordDao()
        textToSpeechHelper = TextToSpeechHelper(requireContext())
        binding.editFab.setOnClickListener {
            toggleEditMode()
        }

        binding.buttonWordDelete.setOnClickListener {
            val word = arguments?.getParcelable<WordModel>("word")
            if (word != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    wordDao.deleteWord(word)
                }
                findNavController().navigateUp()
            }
        }
        binding.buttonWordEdit.setOnClickListener {
            val word = arguments?.getParcelable<WordModel>("word")
            val bundle = Bundle().apply {
                putParcelable("word", word)
            }
            val addWordFragment = AddWordFragment()
            addWordFragment.arguments = bundle
            findNavController().navigate(R.id.action_nav_details_to_addWordFragment, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val wordToBind: WordModel? = arguments?.getParcelable<WordModel>("word")

        // Bind word data to view
        wordToBind?.run {
            binding.word.text = word
            binding.translation.text = translation
            binding.partOfSpeech.text = partOfSpeech
            binding.gender.text = gender
//            binding.declension.text = declension.joinToString(", ")
//            binding.synonyms.text = synonyms.joinToString(", ")
            binding.progressBarLearning.progress = learningProgress

            //todo fix Load image
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_error)
                .into(binding.ivWordImage)

            binding.ivTextToSpeech.setOnClickListener {
                val wordToSpeak = word
                textToSpeechHelper.speak(wordToSpeak)
            }
        }
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        if (isEditMode) {
            binding.editFab.setImageResource(R.drawable.ic_fab_cancel)
        } else {
            binding.editFab.setImageResource(R.drawable.ic_edit)
        }

        if (isEditMode) {
           binding.buttonWordEdit.visibility = View.VISIBLE
            binding.buttonWordDelete.visibility = View.VISIBLE
        } else {
            hideEditButtons()
        }
    }

    private fun hideEditButtons() {
        binding.buttonWordEdit.visibility = View.GONE
       binding.buttonWordDelete.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        textToSpeechHelper.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
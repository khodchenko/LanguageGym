package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.languagegym.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val word = arguments(?.getParcelable<)Word>("word")

        // Bind word data to view
//        word?.let {
//            binding.word.text = it.word
//            binding.translation.text = it.translation
//            binding.partOfSpeech.text = it.partOfSpeech
//            binding.gender.text = it.gender
//            binding.declension.text = it.declension.joinToString(", ")
//            binding.synonyms.text = it.synonyms.joinToString(", ")
//            binding.progressBarLearning.progress = it.learningProgress
//
//            // Load image
//            Glide.with(binding.imageUrl)
//                .load(it.imageUrl)
//                .placeholder(R.drawable.ic_menu_camera)
//                .error(R.drawable.ic_error)
//                .into(binding.imageUrl)
 //   }
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
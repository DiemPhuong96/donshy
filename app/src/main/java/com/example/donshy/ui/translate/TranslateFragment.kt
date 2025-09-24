package com.example.donshy.ui.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.ViewModel.translate.TranslateViewModel
import com.example.donshy.ViewModel.translate.TranslateViewModelFactory
import com.example.donshy.data.model.LanguageProvider
import com.example.donshy.databinding.TranslateFragmentBinding
import com.example.donshy.utils.Result


class TranslateFragment : Fragment() {
    private var _binding: TranslateFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel =
            ViewModelProvider(this, TranslateViewModelFactory())[TranslateViewModel::class.java]
        val languages = LanguageProvider.getSupportedLanguages()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            languages.map { it.displayName }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerSourceLanguage.adapter = adapter
        binding.spinnerTargetLanguage.adapter = adapter
        binding.imgChange.setOnClickListener {
            val targetLanguagePosition = binding.spinnerTargetLanguage.selectedItemPosition
            val sourceLanguagePosition = binding.spinnerSourceLanguage.selectedItemPosition
            binding.spinnerSourceLanguage.setSelection(targetLanguagePosition)
            binding.spinnerTargetLanguage.setSelection(sourceLanguagePosition)
        }
        binding.btnTranslate.setOnClickListener {
            val sourcePos = binding.spinnerSourceLanguage.selectedItemPosition
            val targetPos = binding.spinnerTargetLanguage.selectedItemPosition
            viewModel.translateText(
                text = binding.edOriginalText.text.toString(),
                sourceLanguage = languages[sourcePos].code,
                targetLanguage = languages[targetPos].code
            )
        }
        viewModel.translateResult.observe(viewLifecycleOwner) { translatedResult ->
            when (translatedResult) {
                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.txtTranslationError.visibility = View.GONE
                    binding.txtTranslation.visibility = View.VISIBLE
                    binding.txtTranslation.text = translatedResult.data
                }

                is Result.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.txtTranslationError.visibility = View.VISIBLE
                    binding.txtTranslation.visibility = View.GONE
                    binding.txtTranslationError.text = translatedResult.message
                }

                is Result.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TranslateFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}
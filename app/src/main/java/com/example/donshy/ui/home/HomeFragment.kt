package com.example.donshy.ui.home

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donshy.ViewModel.home.HomeViewModel
import com.example.donshy.ViewModel.home.HomeViewModelFactory
import com.example.donshy.databinding.HomeFragmentBinding
import com.example.donshy.utils.Result
import java.util.Locale

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var wordAdapter: WordAdapter
    private lateinit var tts: TextToSpeech

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tts = TextToSpeech(requireContext()) { status ->
            if(status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(requireContext(), "TTS not support", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "TTS initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel = ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class]
        wordAdapter = WordAdapter(emptyList(), tts)
        binding.rcWords.adapter = wordAdapter
        binding.rcWords.layoutManager = LinearLayoutManager(requireContext())
        binding.btnSave.setOnClickListener {
            viewModel.addWords(
                binding.edInputWords.text.toString(),
                binding.edInputMeaning.text.toString()
            )
        }
        viewModel.getWords()
        viewModel.addWordState.observe(requireActivity()) { addWordState ->
            when (addWordState) {
                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    viewModel.getWords()
                }

                is Result.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.txtAddWordError.text = addWordState.message
                    binding.txtAddWordError.visibility = View.VISIBLE
                }

                is Result.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }

        }
        viewModel.getWordState.observe(requireActivity()) { getWordState ->

            when (getWordState) {
                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    wordAdapter.updateWord(getWordState.data)
                }

                is Result.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.txtAddWordError.text = getWordState.message
                    binding.txtAddWordError.visibility = View.VISIBLE
                }

                is Result.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onDestroyView() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroyView()
        _binding = null
    }
}
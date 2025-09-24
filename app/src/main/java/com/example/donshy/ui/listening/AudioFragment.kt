package com.example.donshy.ui.listening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donshy.ViewModel.audio.AudioViewModel
import com.example.donshy.ViewModel.audio.AudioViewModelFactory
import com.example.donshy.data.model.AudioType
import com.example.donshy.databinding.AudioFragmentBinding

class AudioFragment() : Fragment() {
    private var _binding: AudioFragmentBinding? = null
    private lateinit var viewModel: AudioViewModel
    private lateinit var adapter: AudioAdapter
    val binding get() = _binding!!
    private lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AudioFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var audioType = AudioType.TOEIC.type
        arguments?.let {
            audioType = it.getString(AUDIO_TYPE, AudioType.TOEIC.type)
        }
        viewModel = ViewModelProvider(this, AudioViewModelFactory(requireContext()))[AudioViewModel::class]
        viewModel.loadAllAudio(audioType)
        player = ExoPlayer.Builder(requireActivity()).build()
        adapter = AudioAdapter(
            emptyList(),
            onPlayClick = {
                val mediaItem = MediaItem.fromUri(it)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            },
            onPauseClick = {
                player.pause()
            }
        )
        binding.recAudio.adapter = adapter
        binding.recAudio.layoutManager = LinearLayoutManager(requireActivity())
        viewModel.audioPath.observe(requireActivity()) { audioPathState ->
            adapter.updateAudios(audioPathState)
        }

    }

    companion object {
        const val AUDIO_TYPE = "AudioType"
        fun newInstance(audioType: String) =
            AudioFragment().apply {
                arguments = Bundle().apply {
                    putString(AUDIO_TYPE, audioType)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}
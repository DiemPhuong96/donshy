package com.example.donshy.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donshy.data.model.Word
import com.example.donshy.databinding.WordItemBinding
import com.example.donshy.R
import java.util.UUID

class WordAdapter(private var words: List<Word>, private val tts: TextToSpeech) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    private var currentSpeakerPosition: Int? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    override fun onBindViewHolder(
        holder: WordViewHolder,
        position: Int
    ) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    init {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                mainHandler.post {
                    currentSpeakerPosition?.let { notifyItemChanged(it) }
                }
            }

            override fun onDone(utteranceId: String?) {
                mainHandler.post {
                    currentSpeakerPosition?.let {
                        currentSpeakerPosition = null
                        notifyItemChanged(it)
                    }
                }
            }

            override fun onError(p0: String?) {
                Log.d("error", "tts error")
            }

        })
    }
    inner class WordViewHolder(private val binding: WordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(word: Word) {
            binding.txtWord.text = word.word
            binding.txtMeaning.text = word.meaning
            binding.imgSpeaker.setOnClickListener {
                val oldPosition = currentSpeakerPosition
                currentSpeakerPosition = adapterPosition
                val params = Bundle()
                val utteranceId = UUID.randomUUID().toString()
                tts.speak(word.word, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
                oldPosition?.let { notifyItemChanged(it) }
                notifyItemChanged(adapterPosition)
            }
            if (currentSpeakerPosition == adapterPosition) {
                binding.imgSpeaker.setImageResource(R.drawable.icon_speaker)
            } else {
                binding.imgSpeaker.setImageResource(R.drawable.icon_mute)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWord(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }
}
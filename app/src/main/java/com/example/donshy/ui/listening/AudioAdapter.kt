package com.example.donshy.ui.listening

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.donshy.R
import com.example.donshy.data.model.Audio
import com.example.donshy.databinding.AudioItemBinding

class AudioAdapter(private var audios: List<Audio>, private var onPlayClick: (String) -> Unit, private var onPauseClick: (String) -> Unit) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioViewHolder {
        val binding = AudioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioViewHolder(binding)
    }
    private lateinit var player: ExoPlayer

    override fun onBindViewHolder(
        holder: AudioViewHolder,
        position: Int
    ) {
        holder.bind(audios[position])
    }

    override fun getItemCount(): Int {
        return audios.size
    }

    inner class AudioViewHolder(private val binding: AudioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(audio: Audio) {
            binding.txtAudioName.text = audio.name
            player = ExoPlayer.Builder(itemView.context).build()
            binding.icPlay.setImageResource(
                if (audio.isPlaying) R.drawable.icon_pause
                else R.drawable.icon_play
            )
            binding.icPlay.setOnClickListener {
                audio.isPlaying = !audio.isPlaying
                if (audio.isPlaying) {
                    audios.forEachIndexed {index, a ->
                        if (a != audio && a.isPlaying) {
                            a.isPlaying = false
                            notifyItemChanged(index)
                        }
                    }
                    onPlayClick(audio.path)
                } else {
                    onPauseClick(audio.path)
                }
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    notifyItemChanged(pos)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAudios(newAudios: List<Audio>) {
        audios = newAudios
        notifyDataSetChanged()
    }
}
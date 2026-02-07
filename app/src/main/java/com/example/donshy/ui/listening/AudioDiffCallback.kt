package com.example.donshy.ui.listening

import androidx.recyclerview.widget.DiffUtil
import com.example.donshy.data.model.Audio

class AudioDiffCallback: DiffUtil.ItemCallback<Audio>() {
    override fun areItemsTheSame(
        oldItem: Audio,
        newItem: Audio
    ): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(
        oldItem: Audio,
        newItem: Audio
    ): Boolean {
        return oldItem == newItem
    }
}
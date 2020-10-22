package com.evans.playnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.evans.playnow.databinding.ItemSwipeSongBinding

class SwipeSongAdapter : BaseSongAdapter<ItemSwipeSongBinding>() {

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun getAdapterBinding(inflater: LayoutInflater, container: ViewGroup) =
        ItemSwipeSongBinding.inflate(inflater, container, false)

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val song = songs[position]
        binding.apply {
            val text = "${song.title} - ${song.subtitle}"
            txtSongTitle.text = text

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}
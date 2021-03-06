package com.evans.playnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import com.evans.playnow.databinding.ItemSongBinding
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter<ItemSongBinding>() {

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun getAdapterBinding(inflater: LayoutInflater, container: ViewGroup) =
        ItemSongBinding.inflate(inflater, container, false)

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val song = songs[position]
        binding.apply {
            txtSongTitle.text = song.title
            txtSongSubtitle.text = song.subtitle
            glide.load(song.imgUrl).into(songImg)

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}
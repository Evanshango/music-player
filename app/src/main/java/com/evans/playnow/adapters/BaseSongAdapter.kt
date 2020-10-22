package com.evans.playnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.evans.playnow.data.entities.Song

abstract class BaseSongAdapter<B : ViewBinding> :
    RecyclerView.Adapter<BaseSongAdapter<B>.SongHolder>() {

    protected lateinit var binding: B

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        binding = getAdapterBinding(LayoutInflater.from(parent.context), parent)
        return SongHolder(binding)
    }

    override fun getItemCount() = songs.size

    protected var onItemClickListener: ((Song) -> Unit)? = null

    fun setItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    inner class SongHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    protected val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    abstract fun getAdapterBinding(inflater: LayoutInflater, container: ViewGroup): B

    protected abstract val differ: AsyncListDiffer<Song>
}
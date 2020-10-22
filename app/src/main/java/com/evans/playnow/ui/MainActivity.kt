package com.evans.playnow.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.evans.playnow.adapters.SwipeSongAdapter
import com.evans.playnow.data.entities.Song
import com.evans.playnow.databinding.ActivityMainBinding
import com.evans.playnow.exoplayer.toSong
import com.evans.playnow.helpers.Status.*
import com.evans.playnow.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    @Inject
    lateinit var glide: RequestManager

    private var currPlayingSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToObservers()

        binding.apply {
            songViewpager.adapter = swipeSongAdapter
        }
    }

    private fun switchViewPagerToCurrSong(song: Song) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (newItemIndex != -1) {
            binding.songViewpager.currentItem = newItemIndex
            currPlayingSong = song
        }
    }

    private fun subscribeToObservers() {
        binding.apply {
            mainViewModel.mediaItems.observe(this@MainActivity) {
                it?.let { result ->
                    when (result.status) {
                        SUCCESS -> {
                            result.data?.let { songs ->
                                swipeSongAdapter.songs = songs
                                if (songs.isNotEmpty()) {
                                    glide.load((currPlayingSong ?: songs[0]).imgUrl)
                                        .into(ivCurrSong)
                                }
                                switchViewPagerToCurrSong(currPlayingSong ?: return@observe)
                            }
                        }
                        ERROR, LOADING -> Unit
                    }
                }
            }
            mainViewModel.curPlayingSong.observe(this@MainActivity) {
                if (it == null) return@observe

                currPlayingSong = it.toSong()
                glide.load(currPlayingSong?.imgUrl).into(ivCurrSong)
                switchViewPagerToCurrSong(currPlayingSong ?: return@observe)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
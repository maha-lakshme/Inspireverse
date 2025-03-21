package com.maha.inspireverse.main.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.chip.Chip
import com.maha.inspireverse.R
import com.maha.inspireverse.adapter.ThemeAdapter
import com.maha.inspireverse.databinding.FragmentThemeBinding
import com.maha.inspireverse.databinding.ItemThemeCardBinding
import com.maha.inspireverse.main.GenerateQuotes
import com.maha.inspireverse.model.ThemeModel
import com.maha.inspireverse.viewmodel.QuotesViewModel
import com.maha.inspireverse.viewmodel.ThemeSelectionViewModel

class ThemeFragment : Fragment() {

    private lateinit var binding: FragmentThemeBinding
    private lateinit var exoPlayer: ExoPlayer
    private val viewModel: QuotesViewModel by lazy {
        ViewModelProvider(requireActivity()).get(QuotesViewModel::class.java)
    }

    private lateinit var themeAdapter: ThemeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThemeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getImageFromApi("serene lakes celestial patterns")

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            )
            .build()
        exoPlayer = ExoPlayer.Builder(requireContext()).setLoadControl(loadControl).build()
        binding.playerView.player = exoPlayer
        // Disable controls
        binding.playerView.useController = false

        // Create a MediaItem for the video in res/raw
        val videoUri = Uri.parse("android.resource://com.maha.insiprevrese/" + R.raw.animated)
        val mediaItem = MediaItem.fromUri(videoUri)

        // Add the MediaItem to ExoPlayer
        exoPlayer.setMediaItem(mediaItem)
         // Enable looping
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        // Prepare and start playback
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.fetchVideos()
        viewModel.videosUrl.observe(viewLifecycleOwner, Observer { videofilesList->
            println("Small Video URL: ${videofilesList.toString()}")
        })

        viewModel.themeList.observe(viewLifecycleOwner, Observer { themeModelList ->
            themeAdapter = ThemeAdapter(themeModelList, onThemeClicked = { themeModel ->
                Log.d("Theme Clicked--->", themeModel.fontName)

                // Save the selected theme data in SharedPreferences
                val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("custom_background_url", themeModel.imageUrl)
                    putString("custom_font", themeModel.fontName)
                    apply()
                }
                findNavController().navigate(R.id.navigation_home)
// Optionally notify the rest of your app that a new theme is chosen.
                // You could use a shared ViewModel or an event bus for this.
                // For example:
                // sharedThemeViewModel.updateTheme(themeModel)
            })
            recyclerView.adapter = themeAdapter
        })

        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checId ->
            for (id in checId) {
                val chip = group.findViewById<Chip>(id)
                val chipText = chip?.text ?: "Unknown"
                Log.d("CHIPS Selected---", chipText.toString())
                if (chipText.toString().equals("Motivation")) {
                    viewModel.getImageFromApi(GenerateQuotes.generateMotivationalQuery())

                } else if (chipText.toString().equals("Popular")) {
                    viewModel.getImageFromApi(GenerateQuotes.generatePopularQuery())
                } else if (chipText.toString().equals("Success")) {
                    viewModel.getImageFromApi(GenerateQuotes.generateSuccessQuery())
                } else if (chipText.toString().equals("Love")) {
                    viewModel.getImageFromApi(GenerateQuotes.generateLoveQuery())
                } else if (chipText.toString().equals("Relationship")) {
                    viewModel.getImageFromApi(GenerateQuotes.generateRelationshipQuery())
                }
            }
            //Log.d("CHIPS Selected---",checId.toString())
        }

        // Observe loading state and show/hide progress bar
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        // Observe errors and show toast
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.e("Error", error)
        })


        return binding.root
    }
    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release() // Release resources to prevent memory leaks
    }
}
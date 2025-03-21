package com.maha.inspireverse.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import com.maha.inspireverse.R
import com.maha.inspireverse.databinding.ActivityQuoteDetailBinding

class QuoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set up the shared element transition before calling super.onCreate()
//        window.sharedElementEnterTransition = TransitionInflater.from(this)
//            .inflateTransition(android.R.transition.move)
//        window.sharedElementReturnTransition = TransitionInflater.from(this)
//            .inflateTransition(android.R.transition.move)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        // Hide the ActionBar if present
        supportActionBar?.hide()

//Custom transition//
        val transition = TransitionInflater.from(this)
            .inflateTransition(R.transition.change_image_transform)
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 500L // Adjust this value for speed



        super.onCreate(savedInstanceState)
            postponeEnterTransition()
        binding = ActivityQuoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Receive data from the intent
        val quoteText = intent.getStringExtra("quote_text") ?: "No Quote Provided"
        val imageUrl = intent.getStringExtra("image_url") ?: ""

        // Bind data to layout
        binding.quoteText = quoteText
        binding.imageUrl = imageUrl

        // Start the postponed transition after the image has loaded
        binding.detailImageView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}


package com.maha.inspireverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maha.inspireverse.R
import com.maha.inspireverse.databinding.ItemFullscreenQuoteBinding
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote
import com.maha.inspireverse.viewmodel.QuotesViewModel

class FullScreenQuotesAdapter(
    private var quotesWithImages: List<Pair<Quote, Photos>>,private val viewModel: QuotesViewModel
) : RecyclerView.Adapter<FullScreenQuotesAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFullscreenQuoteBinding>(
            inflater,
            R.layout.item_fullscreen_quote,
            parent,
            false
        )
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotesWithImages[position])
    }
    // Call this method if you need to update the dataset explicitly:
    fun updateItems(newItems:  List<Pair<Quote, Photos>>) {
        quotesWithImages = newItems
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = quotesWithImages.size

   inner class QuoteViewHolder(
        private val binding: ItemFullscreenQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quoteWithImage: Pair<Quote, Photos>) {
            binding.root.setOnClickListener{
                println("Tapped on item at position: $position")
            }
            val (quote, photo) = quoteWithImage
            binding.quote = quote
            binding.photo = photo
//            Log.e("INSIDE CUSTOM ADAPTER photo------>", photo.src.portrait)
//            Log.e("INSIDE CUSTOM ADAPTER font------>", quote.fontTypeFace)

            binding.btnLike.isSelected =quote.isSaved
            binding.btnLike.setOnClickListener(){

                val scaleUp = ScaleAnimation(
                    0.9f, 1f, // Start and end values for the X axis scaling
                    0.9f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point of Y scaling
                ).apply {
                    duration = 100
                    fillAfter = true
                }
                binding.btnLike.startAnimation(scaleUp)
                Log.d("Liked---", "clicked Like")
                val isLikedNow = !quote.isSaved // Toggle the current state
                binding.btnLike.isSelected = isLikedNow // Update button UI state
                viewModel.toggleLike(quoteWithImage, isLikedNow) // Call the toggleLike function
                quote.isSaved = isLikedNow // Update the local data state (optional)
            }
            }
        }
}

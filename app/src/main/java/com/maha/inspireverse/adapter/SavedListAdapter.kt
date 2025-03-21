package com.maha.inspireverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maha.inspireverse.R
import com.maha.inspireverse.databinding.ItemImageBinding
import com.maha.inspireverse.model.Quote
import com.maha.inspireverse.viewmodel.QuotesViewModel

class SavedListAdapter(private var likedQuotesList :List<Quote>,private val viewModel: QuotesViewModel): RecyclerView.Adapter<SavedListAdapter.SavedQuotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            inflater,
            R.layout.item_image,
            parent,
            false
        )
        return SavedQuotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
     return  likedQuotesList.size
    }

    override fun onBindViewHolder(holder: SavedQuotesViewHolder, position: Int) {
        holder.bind(likedQuotesList[position])
    }

  inner class SavedQuotesViewHolder(private val binding:ItemImageBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            Log.d("LikedList adapter----", quote.quote)
            binding.quote = quote.quote
            binding.imageUrl = quote.photoUrl
            binding.btnLike.isSelected = quote.isSaved

            binding.btnLike.setOnClickListener {
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
                    Log.d("Liked---", quote.id)
                    val isLikedNow = !quote.isSaved // Toggle the current state
                    binding.btnLike.isSelected = isLikedNow // Update button UI state
                    viewModel.toggleLikeAndDislike(quote.id, isLikedNow) // Call the toggleLike function
                    quote.isSaved = isLikedNow // Update the local data state (optional)

            }

            }
        }



    // A method to update the list
    fun updateList(newList: List<Quote>) {
        likedQuotesList = newList
        notifyDataSetChanged()
    }
}
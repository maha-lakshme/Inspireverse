package com.maha.inspireverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maha.inspireverse.R
import com.maha.inspireverse.databinding.ItemImageBinding
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote

class QuoteAndPicsAdapter(
    private val quoteImg: List<Pair<Quote, Photos>>,
    private val onSave: (String, Boolean) -> Unit,
    private val onDownload: (String) -> Unit,
    private val onQuoteCardClick: (Quote, Photos, ImageView) -> Unit
) : RecyclerView.Adapter<QuoteAndPicsAdapter.QuoteAndPicsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteAndPicsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            inflater, R.layout.item_image, parent, false
        )
        return QuoteAndPicsViewHolder(binding, onSave, onDownload, onQuoteCardClick)
    }

    override fun getItemCount(): Int {
        return quoteImg.size
    }

    override fun onBindViewHolder(holder: QuoteAndPicsViewHolder, position: Int) {
        holder.bind(quoteImg[position])
    }

    class QuoteAndPicsViewHolder(
        private val binding: ItemImageBinding,
        private val onSave: (String, Boolean) -> Unit,
        private val onDownload: (String) -> Unit,
        private val onQuoteCardClick: (Quote, Photos, ImageView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quoteImg: Pair<Quote, Photos>) {
            val (quote, photo) = quoteImg
            binding.imageUrl = photo.src.portrait
            binding.quote = quote.quote
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

                // Save the quotes and pics in firebase//
                quote.isSaved = !quote.isSaved
                binding.btnLike.isSelected = quote.isSaved
                if (quote.isSaved) {
                    Log.e("Quote saved", "Quote isSaved: ${quote.isSaved}")
                } else {
                    Log.e("Quote removed", "Quote isSaved: ${quote.isSaved}")
                }
                onSave(quote.id.toString(), quote.isSaved)
            }

            // Download in local storeage//
//            binding.btnDownload.setOnClickListener {
//                Log.d("Save Clicked---","saved")
//                onDownload(quote.quote)
//            }

            //open the quotes pics //
            binding.root.setOnClickListener{
                onQuoteCardClick(quote,photo,binding.imageView)
            }
        }
    }
}

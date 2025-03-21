package com.maha.inspireverse.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.maha.inspireverse.R
import com.bumptech.glide.Glide
import com.maha.inspireverse.databinding.ItemImageBinding

class ImagesAdapter(private val images: List<Pair<String,String>>) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemImageBinding
        if (convertView == null) {

            val inflater = LayoutInflater.from(parent?.context)
            binding = DataBindingUtil.inflate(inflater, R.layout.item_image, parent, false)
        } else {
            binding = DataBindingUtil.getBinding(convertView)!!
        }

        val (imageUrl, quote) = images[position]
        Log.d("images url ----- ",imageUrl)
        binding.imageUrl = imageUrl
        binding.quote = quote
        binding.executePendingBindings()
        return binding.root
    }
}


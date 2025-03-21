package com.maha.inspireverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maha.inspireverse.R
import com.maha.inspireverse.databinding.ItemThemeCardBinding
import com.maha.inspireverse.model.ThemeModel

class ThemeAdapter(
    private var themesList: List<ThemeModel>,
    private val onThemeClicked: (ThemeModel) -> Unit
) :
    RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemThemeCardBinding>(
            inflater,
            R.layout.item_theme_card,
            parent,
            false
        )
        return ThemeViewHolder(binding, onThemeClicked)
    }

    override fun getItemCount(): Int {
        return themesList.size
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(themesList[position])
    }

    inner class ThemeViewHolder(
        private val binding: ItemThemeCardBinding,
        private val onThemeClicked: (ThemeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(themesListIem: ThemeModel) {
            Log.e("Theme Iems", "Image Res: ${themesListIem.fontName}")
            binding.theme = themesListIem
            binding.cardViewTheme.setOnClickListener {
                onThemeClicked(themesListIem)
            }
            binding.executePendingBindings()
        }

    }
}
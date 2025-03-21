package com.maha.inspireverse.main

import android.app.Activity
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.maha.inspireverse.R
import android.transition.TransitionInflater
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    // Obtain the Activity from the view's context
    val activity = view.context as? Activity

    // Postpone the transition
    activity?.postponeEnterTransition()
    if (!imageUrl.isNullOrEmpty()) {

        Glide.with(view.context)
            .load(imageUrl)
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    view.setImageDrawable(resource)
                    activity?.startPostponedEnterTransition()
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    activity?.startPostponedEnterTransition()
                    return false
                }
            })
            .into(view)
    } else {
        view.setImageResource(R.drawable.bg_6)
        activity?.startPostponedEnterTransition()
    }
}

    @BindingAdapter("typeface")
    fun setTypeface(textView: TextView, fontName: String?) {
        val context = textView.context // Get the Context from the TextView
        if (!fontName.isNullOrEmpty()) {
            val typeface: Typeface? = when (fontName) {
                "ambery_garden" -> ResourcesCompat.getFont(context, R.font.ambery_garden)
                "bigshoulders" -> ResourcesCompat.getFont(context, R.font.bigshoulders)
                "haloberry" -> ResourcesCompat.getFont(context, R.font.haloberry)
                "chuckwe" -> ResourcesCompat.getFont(context, R.font.chuckwe)
                "charm" -> ResourcesCompat.getFont(context, R.font.charm)
                "jonova_regular" -> ResourcesCompat.getFont(context, R.font.jonova_regular)
                "labybu" -> ResourcesCompat.getFont(context, R.font.labybu)
                "langer" -> ResourcesCompat.getFont(context, R.font.langer)
                "courgette_regular" -> ResourcesCompat.getFont(context, R.font.courgette_regular)
                "delius" -> ResourcesCompat.getFont(context, R.font.delius)
                "montserrat_regular" -> ResourcesCompat.getFont(context, R.font.montserrat_regular)
                "satisfy" -> ResourcesCompat.getFont(context, R.font.satisfy)
                "tangerine_regular" -> ResourcesCompat.getFont(context, R.font.tangerine_regular)
                else -> Typeface.create(fontName, Typeface.BOLD) // Default fonts
            }
            textView.typeface = typeface
        }
    }


//@BindingAdapter("imageRes")
//fun loadImage(view: ImageView, imageRes: Int) {
//    Glide.with(view.context)
//        .load(imageRes)
//        .placeholder(R.drawable.placeholder_img) // Optional placeholder
//        .error(R.drawable.heart) // Optional error image
//        .into(view)
//}
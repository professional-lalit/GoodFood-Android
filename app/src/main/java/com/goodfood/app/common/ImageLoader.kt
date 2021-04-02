package com.goodfood.app.common

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.goodfood.app.R


/**
 * Created by Lalit N. Hajare (Android Developer) on 27/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
object ImageLoader {

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, url: String?) {
        url?.let {
            if (it.isNotEmpty()) {
                Glide.with(imageView.context)
                    .load(it)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .circleCrop()
                    .into(imageView)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:setImage")
    fun setImage(imageView: ImageView, url: String?) {
        url?.let {
            if (it.isNotEmpty()) {
                Glide.with(imageView.context)
                    .load(it)
                    .placeholder(R.drawable.dark_landscape_placeholder)
                    .into(imageView)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:loadImageFile")
    fun setImageFile(imageView: ImageView, uri: String?) {
        if (uri?.isNotEmpty() == true) {
            Glide.with(imageView.context)
                .load(Uri.parse(uri))
                .placeholder(R.drawable.dark_landscape_placeholder)
                .into(imageView)
        }
    }


}
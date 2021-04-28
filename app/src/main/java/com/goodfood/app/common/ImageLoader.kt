package com.goodfood.app.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.goodfood.app.R
import java.io.File


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
    @BindingAdapter("app:loadCircularImage")
    fun loadCircularImage(imageView: ImageView, url: String?) {
        url?.let {
            if (it.isNotEmpty()) {
                val imageLink = it.replace("localhost","10.0.2.2")
                Glide.with(imageView.context)
                    .load(imageLink)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .circleCrop()
                    .into(imageView)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:loadImageUrl")
    fun setImage(imageView: ImageView, url: String?) {
        url?.let {
            if (it.isNotEmpty()) {
                val imageLink = it.replace("localhost","10.0.2.2")
                Glide.with(imageView.context)
                    .load(imageLink)
                    .placeholder(R.drawable.dark_landscape_placeholder)
                    .into(imageView)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:loadImageFile")
    fun setImageFile(imageView: ImageView, uri: String?) {
        if (uri?.isNotEmpty() == true) {
            val fileUri = Uri.parse(uri)
            val bmp = BitmapFactory.decodeFile(fileUri.path)
            Glide.with(imageView.context)
                .load(bmp)
                .apply(
                    RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .placeholder(R.drawable.dark_landscape_placeholder)
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("app:loadImageUri")
    fun setImageUri(imageView: ImageView, uri: Uri?) {
        uri?.let {
            Glide.with(imageView.context)
                .load(uri)
                .apply(
                    RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .placeholder(R.drawable.dark_landscape_placeholder)
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("app:loadBmp")
    fun setBmpImage(imageView: ImageView, bmp: Bitmap?) {
        bmp?.let {
            Glide.with(imageView.context)
                .load(bmp)
                .into(imageView)
        }
    }


}
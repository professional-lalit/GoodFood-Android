package com.goodfood.app.common

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.goodfood.app.R
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo


/**
 * Created by Lalit N. Hajare (Android Developer) on 31/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

@BindingAdapter("app:setDiscount")
fun appendDiscountText(textView: TextView, discount: Int) {
    textView.text = "$discount%"
}

@BindingAdapter("app:setActionItemIcon")
fun setActionItemIcon(imageView: ImageView, model: Any) {
    if (model is RecipePhoto) {
        imageView.setImageResource(R.drawable.ic_camera)
    } else if (model is RecipeVideo) {
        imageView.setImageResource(R.drawable.ic_movie_clapper)
    }
}
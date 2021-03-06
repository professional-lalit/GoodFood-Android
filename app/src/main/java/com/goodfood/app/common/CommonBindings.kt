package com.goodfood.app.common

import android.R
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import com.goodfood.app.models.domain.Reaction
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.models.domain.User
import com.goodfood.app.ui.common.widgets.RecipeBottomStrip
import com.goodfood.app.ui.common.widgets.RecipeImagePagerLayout
import com.goodfood.app.ui.common.widgets.UserProfileLayout


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

@BindingAdapter("app:loadProgress")
fun setUploadProgress(progressBar: ProgressBar, progress: Int) {
    progressBar.progress = progress
}

@BindingAdapter("app:loadProgress")
fun setUploadProgress(textView: TextView, progress: Int) {
    textView.text = "$progress% uploaded"
}

@BindingAdapter("app:recipeBottomStripData")
fun setRecipeBottomStripData(recipeBottomStrip: RecipeBottomStrip, recipe: Recipe) {
    recipeBottomStrip.setData(recipe)
}

@BindingAdapter("app:recipeData")
fun setRecipeData(
    userProfileLayout: UserProfileLayout,
    recipe: Recipe?
) {
    userProfileLayout.setRecipeData(recipe)
}

@BindingAdapter("app:userData")
fun setUserData(
    userProfileLayout: UserProfileLayout,
    user: User?
) {
    userProfileLayout.setProfileData(user)
}

@BindingAdapter("app:setRecipeImages")
fun setRecipeImages(
    recipeImagePagerLayout: RecipeImagePagerLayout,
    imageUrls: List<String>?
) {
    if (imageUrls?.isNotEmpty() == true) {
        recipeImagePagerLayout.visibility = View.VISIBLE
        recipeImagePagerLayout.setData(imageUrls)
    } else {
        recipeImagePagerLayout.visibility = View.GONE
    }
}

@BindingAdapter("app:replyVisibility")
fun setReplyVisiblity(img: ImageView, list: List<Any>) {
    if (list.isNotEmpty()) {
        img.visibility = View.VISIBLE
    } else {
        img.visibility = View.GONE
    }
}


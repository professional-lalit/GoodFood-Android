package com.goodfood.app.ui.home.fragments.create_recipe

import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.ui.common.BaseViewModel
import java.io.File


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class CreateRecipeViewModel: BaseViewModel() {

    private val photosMap = mapOf<RecipePhoto, File>()
    private val videosMap = mapOf<RecipeVideo, File>()

}
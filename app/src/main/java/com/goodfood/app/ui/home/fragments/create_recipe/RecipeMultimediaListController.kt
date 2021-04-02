package com.goodfood.app.ui.home.fragments.create_recipe

import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.recipePhotoData
import com.goodfood.app.recipeVideoData


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeMultimediaListController : TypedEpoxyController<List<Any>>() {

    override fun buildModels(list: List<Any>?) {
        list?.forEach { itemData ->
            when (itemData) {
                is RecipePhoto -> {
                    addPhoto(itemData)
                }
                is RecipeVideo -> {
                    addVideo(itemData)
                }
            }
        }
    }

    private fun addPhoto(itemData: RecipePhoto) {
        recipePhotoData {
            id(itemData.hashCode())
            data(itemData)
        }
    }

    private fun addVideo(itemData: RecipeVideo) {
        recipeVideoData {
            id(itemData.hashCode())
            data(itemData)
        }
    }


}
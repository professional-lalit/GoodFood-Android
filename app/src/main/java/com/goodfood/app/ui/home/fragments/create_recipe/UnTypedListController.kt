package com.goodfood.app.ui.home.fragments.create_recipe

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.goodfood.app.events.sendClickEvent
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.MediaState
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.recipePhotoData
import com.goodfood.app.recipeVideoData


/**
 * Created by Lalit N. Hajare (Android Developer) on 03/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class UnTypedListController : EpoxyController() {

    var list: MutableList<RecipePhoto>? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        list?.forEach { itemData ->
            when (itemData) {
                is RecipePhoto -> {
                    addPhoto(itemData)
                }
            }
        }
    }

    private fun addPhoto(itemData: RecipePhoto) {
        recipePhotoData {
            id(itemData.hashCode())
            data(itemData)
            clickListener(object : IClickListener {
                override fun onClick(view: View, model: Any?) {
                    sendClickEvent(viewId = view.id, model)
                    itemData.state = MediaState.MEDIA_TO_BE_SET
                }
            })
        }
    }

    private fun addVideo(itemData: RecipeVideo) {
        recipeVideoData {
            id(itemData.hashCode())
            data(itemData)
            clickListener(object : IClickListener {
                override fun onClick(view: View, model: Any?) {
                    sendClickEvent(viewId = view.id, model)
                    if (!itemData.isActionItem) {
                        itemData.state = MediaState.MEDIA_TO_BE_SET
                    }
                }
            })
        }
    }
}
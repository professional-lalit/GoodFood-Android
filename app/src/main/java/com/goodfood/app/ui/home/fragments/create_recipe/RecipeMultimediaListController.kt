package com.goodfood.app.ui.home.fragments.create_recipe

import android.view.View
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.R
import com.goodfood.app.databinding.ItemRecipePhotoDataBinding
import com.goodfood.app.events.sendClickEvent
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.MediaState
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
        val model = RecipePhotoModel_().id(itemData.hashCode())
        model.recipePhoto = itemData
        model.clickListener = object : IClickListener {
            override fun onClick(view: View, model: Any?) {
                sendClickEvent(viewId = view.id, model)
                if (!itemData.isActionItem) {
                    itemData.state = MediaState.MEDIA_TO_BE_SET
                }else{
                    itemData.state = MediaState.NOT_UPLOADING
                }
            }
        }
        model.addTo(this)
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

@EpoxyModelClass
abstract class RecipePhotoModel() : DataBindingEpoxyModel() {
    var recipePhoto: RecipePhoto? = null
    var clickListener: IClickListener? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe_photo_data
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemRecipePhotoDataBinding
        binding.data = recipePhoto
        binding.clickListener = clickListener
    }
}
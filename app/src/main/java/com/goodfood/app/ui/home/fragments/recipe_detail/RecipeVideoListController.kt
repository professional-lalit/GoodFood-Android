package com.goodfood.app.ui.home.fragments.recipe_detail

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.events.sendClickEvent
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.recipeData
import com.goodfood.app.videoRecipeDetails


/**
 * Created by Lalit N. Hajare (Android Developer) on 28/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeVideoListController : TypedEpoxyController<List<Recipe.Video>>() {

    override fun buildModels(data: List<Recipe.Video>?) {
        data?.forEach {
            videoRecipeDetails {
                id(it.hashCode())
                data(it)
                clickListener(object : IClickListener {
                    override fun onClick(view: View, model: Any?) {
                        sendClickEvent(view.id, model)
                    }
                })
            }
        }
    }
}
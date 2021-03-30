package com.goodfood.app.ui.home.fragments.explore

import android.util.Log
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.sendClickEvent
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.recipeData
import org.greenrobot.eventbus.EventBus


/**
 * Created by Lalit N. Hajare (Android Developer) on 31/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeController : TypedEpoxyController<List<Recipe>>() {

    override fun buildModels(data: List<Recipe>?) {
        data?.forEach {
            addRecipe(it)
        }
    }

    private fun addRecipe(itemData: Recipe) {
        recipeData {
            id(itemData.hashCode())
            data(itemData)
            itemClickListener(object : IClickListener {
                override fun onClick(view: View, model: Any?) {
                    sendClickEvent(view.id, model)
                }
            })
        }
    }
}
package com.goodfood.app.ui.home.fragments.explore

import android.util.Log
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.sendClickEvent
import com.goodfood.app.inspirationalData
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.BooleanQuestion
import com.goodfood.app.models.domain.Inspiration
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.recipeData
import com.goodfood.app.surveyData
import com.goodfood.app.ui.common.comments.RecipeModel_
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
class ExploreListController : TypedEpoxyController<List<Any>>() {

    var clickListener: IClickListener? = null

    override fun buildModels(data: List<Any>) {
        data.forEach {
            when (it) {
                is Recipe -> addRecipe(it)
                is BooleanQuestion -> addBooleanQuestion(it)
                is Inspiration -> addInspiration(it)
            }
        }
    }

    private fun addInspiration(itemData: Inspiration) {
        inspirationalData {
            id(itemData.hashCode())
            data((itemData))
        }
    }

    private fun addBooleanQuestion(itemData: BooleanQuestion) {
        surveyData {
            id(itemData.hashCode())
            data(itemData)
        }
    }

    private fun addRecipe(itemData: Recipe) {
        val model = RecipeModel_().id(itemData.hashCode())
        model.recipe = itemData
        model.clickListener = clickListener
        model.addTo(this)
    }
}
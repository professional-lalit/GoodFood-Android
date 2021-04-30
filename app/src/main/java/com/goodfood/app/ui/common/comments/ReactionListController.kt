package com.goodfood.app.ui.common.comments

import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.common.CustomApplication
import com.goodfood.app.models.domain.Comment
import com.goodfood.app.models.domain.Reaction
import com.goodfood.app.recipeLeftComment
import com.goodfood.app.recipeRightComment


/**
 * Created by Lalit N. Hajare (Android Developer) on 1/5/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class ReactionListController : TypedEpoxyController<List<Reaction>>() {

    override fun buildModels(data: List<Reaction>?) {
        data?.forEach {
            val model = ReactionModel_().id(it.hashCode())
            model.reaction = it
            model.addTo(this)
        }
    }

}
package com.goodfood.app.ui.home.fragments.explore

import com.airbnb.epoxy.EpoxyAdapter
import com.goodfood.app.models.domain.Recipe


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeAdapter : EpoxyAdapter() {

    fun setData(list: List<Recipe>, isLoading: Boolean) {
        enableDiffing()
        list.forEach {
            models.add(RecipeModel(it))
        }
        notifyModelsChanged()
    }

}
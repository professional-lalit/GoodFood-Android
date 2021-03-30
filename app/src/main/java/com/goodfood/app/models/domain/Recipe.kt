package com.goodfood.app.models.domain

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.goodfood.app.R
import com.goodfood.app.databinding.ItemRecipeBinding


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class Recipe(
    val recipeTitle: String,
    val recipeBrief: String,
    val recipePoster: String? = null,
    val discount: String
) : EpoxyModel<View>() {

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe
    }

    override fun bind(view: View) {
        super.bind(view)
        val txtTitle = view.findViewById<TextView>(R.id.txt_recipe_title)
        txtTitle.text = recipeTitle
    }

    override fun unbind(view: View) {
        super.unbind(view)
        val txtTitle = view.findViewById<TextView>(R.id.txt_recipe_title)
        txtTitle.text = ""
    }

}
package com.goodfood.app.ui.common.comments

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.goodfood.app.R
import com.goodfood.app.databinding.ItemRecipeDataBinding
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.Recipe


/**
 * Created by Lalit N. Hajare (Android Developer) on 1/5/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

@EpoxyModelClass
open class RecipeModel : DataBindingEpoxyModel() {

    var recipe: Recipe? = null
    var itemClickListener: IClickListener? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe_data
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemRecipeDataBinding
        binding.data = recipe

        val controller = CommentListController()
        controller.setData(recipe!!.comments)
        binding.recyclerComments.setController(controller)

        binding.itemClickListener = itemClickListener
    }
}
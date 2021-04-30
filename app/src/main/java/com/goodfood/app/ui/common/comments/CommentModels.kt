package com.goodfood.app.ui.common.comments

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.goodfood.app.R
import com.goodfood.app.databinding.ItemRecipeLeftCommentBinding
import com.goodfood.app.databinding.ItemRecipeRightCommentBinding
import com.goodfood.app.databinding.ItemRecipeRightSingleCommentBinding
import com.goodfood.app.models.domain.Comment


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
open class RightCommentModel : DataBindingEpoxyModel() {

    var comment: Comment? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe_right_comment
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemRecipeRightCommentBinding
        binding.comment = comment

        comment?.reactions?.let {
            val controller = ReactionListController()
            controller.setData(comment?.reactions)
            binding.recyclerInnerComments.setController(controller)
        }
    }

}

@EpoxyModelClass
open class RightSingleCommentModel : DataBindingEpoxyModel() {

    var comment: Comment? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe_right_single_comment
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemRecipeRightSingleCommentBinding
        binding.comment = comment
    }

}

@EpoxyModelClass
open class LeftCommentModel : DataBindingEpoxyModel() {

    var comment: Comment? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_recipe_left_comment
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemRecipeLeftCommentBinding
        binding.comment = comment

        comment?.reactions?.let {
            val controller = ReactionListController()
            controller.setData(comment?.reactions)
            binding.recyclerInnerComments.setController(controller)
        }
    }

}
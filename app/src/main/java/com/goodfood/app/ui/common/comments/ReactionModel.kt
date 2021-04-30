package com.goodfood.app.ui.common.comments

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.goodfood.app.R
import com.goodfood.app.databinding.ItemInnerCommentBinding
import com.goodfood.app.models.domain.Reaction


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
open class ReactionModel : DataBindingEpoxyModel() {
    var reaction: Reaction? = null

    override fun getDefaultLayout(): Int {
        return R.layout.item_inner_comment
    }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding as ItemInnerCommentBinding
        binding.reaction = reaction

        reaction?.reactions?.let {
            val controller = ReactionListController()
            controller.setData(reaction?.reactions)
            binding.recyclerReactions.setController(controller)
        }
    }
}
package com.goodfood.app.ui.common.comments

import com.airbnb.epoxy.TypedEpoxyController
import com.goodfood.app.common.CustomApplication
import com.goodfood.app.models.domain.Comment
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
class CommentListController : TypedEpoxyController<List<Comment>>() {

    override fun buildModels(data: List<Comment>?) {
        data?.forEach {
            if (it.user.userId == CustomApplication.userId) {
                if (it.reactions?.isNotEmpty() == true) {
                    addMyComment(it)
                } else {
                    addMySingleComment(it)
                }
            } else {
                addOtherUserComment(it)
            }
        }
    }

    private fun addMySingleComment(comment: Comment) {
        val model = RightSingleCommentModel_().id(comment.hashCode())
        model.comment = comment
        model.addTo(this)
    }

    private fun addMyComment(comment: Comment) {
        val model = RightCommentModel_().id(comment.hashCode())
        model.comment = comment
        model.addTo(this)
    }

    private fun addOtherUserComment(comment: Comment) {
        val model = LeftCommentModel_().id(comment.hashCode())
        model.comment = comment
        model.addTo(this)
    }
}
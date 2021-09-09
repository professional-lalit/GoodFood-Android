package com.goodfood.app.ui.common.dialogs

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodfood.app.databinding.DialogCommentsBinding
import com.goodfood.app.models.domain.Comment
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.comments.CommentListController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class CommentsBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCommentsBinding
    private lateinit var recipe: Recipe

    companion object {
        const val TAG = "CommentsBottomDialog"
        private const val RECIPE_OBJ = "recipe_obj"

        fun newInstance(recipe: Recipe): CommentsBottomDialog {
            return CommentsBottomDialog().apply {
                arguments = Bundle().apply { putParcelable(RECIPE_OBJ, recipe) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommentsBinding.inflate(inflater)
        return binding.root
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        recipe = args?.getParcelable(RECIPE_OBJ)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = CommentListController()
        controller.setData(recipe.comments)
        binding.recyclerComments.setController(controller)

        binding.txtRecipeTitle.text = recipe.recipeTitle
        binding.imgClose.setOnClickListener { dismiss() }
    }


}
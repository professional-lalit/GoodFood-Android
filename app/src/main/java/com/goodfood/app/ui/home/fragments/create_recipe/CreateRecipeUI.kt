package com.goodfood.app.ui.home.fragments.create_recipe

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.goodfood.app.models.request_dtos.CreateRecipeRequestDTO
import com.goodfood.app.models.request_dtos.LoginRequestDTO


/**
 * Created by Lalit N. Hajare (Android Developer) on 12/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class CreateRecipeUI : BaseObservable() {

    @Bindable
    var title: String = "Sample recipe, from Mumbai"
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var description: String = "This is a sample recipe from Mumbai"
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var price: String = "915"
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var isRecipeDataUploading: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var isRecipeImageDataUploading: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var isRecipeVideoDataUploading: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    fun getRequestDTO(): CreateRecipeRequestDTO {
        return CreateRecipeRequestDTO(
            CreateRecipeRequestDTO.RecipeData(
                title,
                description,
                price.toDouble()
            )
        )
    }
}
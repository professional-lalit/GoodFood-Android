package com.goodfood.app.ui.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.goodfood.app.R;
import com.goodfood.app.databinding.LayoutUserProfileBinding;
import com.goodfood.app.models.domain.Recipe;
import com.goodfood.app.models.domain.User;

import javax.annotation.Nullable;

/**
 * Created by Lalit N. Hajare (Android Developer) on 18/4/21.
 * <p>
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
public class UserProfileLayout extends LinearLayout {

    private LayoutUserProfileBinding binding;
    private float imageSize;
    private int textSize;

    public UserProfileLayout(Context context) {
        super(context);
    }

    public UserProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.UserProfileLayout, 0, 0
        );

        imageSize = attributeArray.getDimension(R.styleable.UserProfileLayout_profileImageSize, 0f);
        textSize = attributeArray.getInt(R.styleable.UserProfileLayout_profileUsernameTextSize, 0);

        attributeArray.recycle();

    }

    public void setRecipeData(@Nullable Recipe recipe) {
        binding = LayoutUserProfileBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );
        LayoutParams params = new LayoutParams((int) imageSize, (int) imageSize);
        binding.imgUser.setLayoutParams(params);

        binding.txtUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        binding.txtRecipePrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        binding.txtRecipePrice.setVisibility(VISIBLE);
        binding.setUser(recipe.getProfile());
        binding.setRecipe(recipe);
    }

    public void setProfileData(@Nullable User user) {
        binding = LayoutUserProfileBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );
        LayoutParams params = new LayoutParams((int) imageSize, (int) imageSize);
        binding.imgUser.setLayoutParams(params);
        binding.txtUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        binding.txtRecipePrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        binding.txtRecipePrice.setVisibility(GONE);
        binding.setUser(user);
    }

}

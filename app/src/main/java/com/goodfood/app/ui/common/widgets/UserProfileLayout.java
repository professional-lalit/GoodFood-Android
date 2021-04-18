package com.goodfood.app.ui.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.goodfood.app.databinding.LayoutUserProfileBinding;
import com.goodfood.app.models.domain.User;

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

    public UserProfileLayout(Context context) {
        super(context);
    }

    public UserProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(User user) {
        binding = LayoutUserProfileBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );
        binding.setUser(user);
    }

}

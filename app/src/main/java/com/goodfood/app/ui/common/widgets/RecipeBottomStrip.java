package com.goodfood.app.ui.common.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.goodfood.app.R;
import com.goodfood.app.databinding.LayoutReceiptItemBottomStripBinding;
import com.goodfood.app.models.domain.Recipe;

/**
 * Created by Lalit N. Hajare (Android Developer) on 18/4/21.
 * <p>
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
public class RecipeBottomStrip extends RelativeLayout {

    private LayoutReceiptItemBottomStripBinding binding;

    public RecipeBottomStrip(Context context) {
        super(context);
    }

    public RecipeBottomStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    public void setData(Recipe recipe) {
        binding = LayoutReceiptItemBottomStripBinding.inflate(
                LayoutInflater.from(getContext()), this, true
        );
        binding.setRecipe(recipe);
        animateView();
    }

    private void animateView() {
        binding.txtPhotoCount.setAlpha(0);
        binding.txtVideoCount.setAlpha(0);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                ViewCompat.animate(binding.txtPhotoCount).alpha(1).setDuration(500);
                ViewCompat.animate(binding.txtVideoCount).alpha(1).setDuration(500);
                return false;
            }
        });
    }
}

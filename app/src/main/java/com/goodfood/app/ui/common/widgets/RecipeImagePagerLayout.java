package com.goodfood.app.ui.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.goodfood.app.R;
import com.goodfood.app.databinding.ItemRecipeImagePagerBinding;
import com.goodfood.app.databinding.LayoutRecipeImagesPagerBinding;

import java.util.List;

/**
 * Created by Lalit N. Hajare (Android Developer) on 20/4/21.
 * <p>
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
public class RecipeImagePagerLayout extends RelativeLayout {

    private LayoutRecipeImagesPagerBinding binding;

    public RecipeImagePagerLayout(Context context) {
        super(context);
    }

    public RecipeImagePagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private List<String> imageUrls;
    private int currentPage;

    public void setData(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        binding = LayoutRecipeImagesPagerBinding.inflate(LayoutInflater.from(getContext()), this);
        binding.pagerRecipeImages.setAdapter(new RecipeImageAdapter(this.imageUrls));

        binding.indicator.setViewPager(binding.pagerRecipeImages);
    }

    public void nextPage() {
        currentPage = binding.pagerRecipeImages.getCurrentItem();
        int totalPages = imageUrls.size();
        if (currentPage < (totalPages - 1)) {
            currentPage += 1;
        } else {
            currentPage = 0;
        }
        binding.pagerRecipeImages.setCurrentItem(currentPage);
    }


    private static class RecipeImageAdapter extends RecyclerView.Adapter<RecipeImageAdapter.RecipePagerViewHolder> {

        private List<String> imageUrls;

        public RecipeImageAdapter(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        @NonNull
        @Override
        public RecipePagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeImageAdapter.RecipePagerViewHolder(
                    ItemRecipeImagePagerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecipePagerViewHolder holder, int position) {
            holder.onBind(imageUrls.get(position));
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }

        private class RecipePagerViewHolder extends RecyclerView.ViewHolder {

            private ItemRecipeImagePagerBinding binding;

            public RecipePagerViewHolder(@NonNull ItemRecipeImagePagerBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void onBind(String url) {
                binding.setImageUrl(url);
            }

        }
    }
}

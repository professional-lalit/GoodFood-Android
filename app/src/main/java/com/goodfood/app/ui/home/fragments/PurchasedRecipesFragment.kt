package com.goodfood.app.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentPurchasedRecipesBinding
import com.goodfood.app.ui.common.BaseFragment

class PurchasedRecipesFragment : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle? = null): PurchasedRecipesFragment {
            val fragment = PurchasedRecipesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun addObservers() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPurchasedRecipesBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
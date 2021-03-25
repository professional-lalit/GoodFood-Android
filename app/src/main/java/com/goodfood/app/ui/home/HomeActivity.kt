package com.goodfood.app.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityHomeBinding
import com.goodfood.app.databinding.ActivitySignupBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.signup.SignupActivity
import com.goodfood.app.ui.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private val homeViewModel by viewModels<HomeViewModel>()

    companion object {

        fun <B : ViewDataBinding, VM : BaseViewModel, T : BaseActivity<B, VM>> openScreen(
            activity: T,
            bundle: Bundle? = null
        ) {
            val intent = Intent(activity, HomeActivity::class.java)
            bundle?.let { intent.putExtras(bundle) }
            activity.startActivity(intent)
        }

    }

    override fun navigateTo(navigable: Navigable) {
        TODO("Not yet implemented")
    }

    override fun setUp() {
        viewModel = homeViewModel
        binding = ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun setObservers() {
    }
}
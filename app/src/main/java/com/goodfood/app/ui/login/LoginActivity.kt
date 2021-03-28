package com.goodfood.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityLoginBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.forgot_password.ForgotPasswordActivity
import com.goodfood.app.ui.home.HomeActivity
import com.goodfood.app.ui.signup.SignupActivity
import com.goodfood.app.utils.Extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    companion object {

        fun <B : ViewDataBinding, VM : BaseViewModel, T : BaseActivity<B, VM>> openScreen(
            activity: T,
            bundle: Bundle? = null
        ) {
            val intent = Intent(activity, LoginActivity::class.java)
            bundle?.let { intent.putExtras(bundle) }
            activity.startActivity(intent)
        }

    }

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun setUp() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = loginViewModel
    }

    override fun navigateTo(navigable: Navigable) {
        when (navigable) {
            BaseViewModel.Navigables.SIGNUP -> SignupActivity.openScreen(this)
            BaseViewModel.Navigables.FORGOT_PWD -> ForgotPasswordActivity.openScreen(this)
            BaseViewModel.Navigables.HOME -> {
                HomeActivity.openScreen(this)
                finish()
            }
        }
    }

    override fun setObservers() {
        viewModel.errorData.observe(this, {
            showToast(it.message)
        })
        viewModel.loginResponse.observe(this, { response ->
            if (response.userId.isNotEmpty()) {
                showToast(response.message ?: "Login successful")
            }
        })
    }

}